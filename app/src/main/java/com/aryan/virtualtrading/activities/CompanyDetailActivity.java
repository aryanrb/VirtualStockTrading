package com.aryan.virtualtrading.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aryan.virtualtrading.R;
import com.aryan.virtualtrading.RetrofitUrl;
import com.aryan.virtualtrading.api.BalanceAPI;
import com.aryan.virtualtrading.api.MarketAPI;
import com.aryan.virtualtrading.api.PortfolioAPI;
import com.aryan.virtualtrading.api.TradeAPI;
import com.aryan.virtualtrading.models.BalanceModel;
import com.aryan.virtualtrading.models.MarketModel;
import com.aryan.virtualtrading.models.PortfolioModel;
import com.aryan.virtualtrading.models.TradeModel;
import com.aryan.virtualtrading.models.UserModel;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvCompanyName, tvCompanySymbol, tvSharePrice, tvVolumeValue, tvCapitalValue, tvEpsValue, tvPeRatioValue;
    private EditText etStockQty;
    private Button btnBuy, btnSell;
    private MarketModel selectedCompany;
    private String companyId = "";
    UserModel user1 = MainActivity.userProfile;
    BalanceModel balance = MainActivity.userBalance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);

        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvCompanySymbol = findViewById(R.id.tvCompanySymbol);
        tvSharePrice = findViewById(R.id.tvSharePrice);
        etStockQty = findViewById(R.id.etStockQty);
        btnBuy = findViewById(R.id.btnBuy);
        btnSell = findViewById(R.id.btnSell);

        tvVolumeValue = findViewById(R.id.tvVolumeValue);
        tvCapitalValue = findViewById(R.id.tvCapitalValue);
        tvEpsValue = findViewById(R.id.tvEpsValue);
        tvPeRatioValue = findViewById(R.id.tvPeRatioValue);

        Intent intent = getIntent();
        companyId = intent.getStringExtra("companyidfordetail");

        getCompanyDetail();
        btnBuy.setOnClickListener(this);
        btnSell.setOnClickListener(this);
    }


    private void getCompanyDetail(){
        MarketAPI companyAPI = RetrofitUrl.getInstance().create(MarketAPI.class);
        Call<MarketModel> companyCall = companyAPI.getCompanyDetail(companyId);

        companyCall.enqueue(new Callback<MarketModel>() {
            @Override
            public void onResponse(Call<MarketModel> call, Response<MarketModel> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(CompanyDetailActivity.this, "Error "+ response.body(), Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedCompany = response.body();
                tvCompanyName.setText(selectedCompany.getName());
                tvCompanySymbol.setText(selectedCompany.getSymbol());
                tvSharePrice.setText(selectedCompany.getSharePrice() + "");
                tvVolumeValue.setText(selectedCompany.getOutShares() + "");
                tvCapitalValue.setText(selectedCompany.getCapital() + "");
                tvEpsValue.setText(selectedCompany.getEps() + "");
                tvPeRatioValue.setText(selectedCompany.getPeRatio() + "");
            }

            @Override
            public void onFailure(Call<MarketModel> call, Throwable t) {
                Toast.makeText(CompanyDetailActivity.this, "Error "+ t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBuy:
                buyShares();
                break;

            case R.id.btnSell:
                sellShares();
                break;

        }
    }

    private void buyShares(){

        TradeModel trade = new TradeModel(selectedCompany.get_id(), user1.get_id(), "buying", "Buy a share", "complete", Calendar.getInstance().getTime(), 0, 0, Integer.parseInt(etStockQty.getText().toString()), selectedCompany.getSharePrice());
        if(checkBalance(Integer.parseInt(etStockQty.getText().toString()), selectedCompany.getSharePrice())) {
            TradeAPI tradeAPI = RetrofitUrl.getInstance().create(TradeAPI.class);
            Call<Void> tradeCall = tradeAPI.addTrade(RetrofitUrl.token, trade);

            tradeCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(CompanyDetailActivity.this, "Code " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    manageBalance(Integer.parseInt(etStockQty.getText().toString()), selectedCompany.getSharePrice(), "buy");
                    Toast.makeText(CompanyDetailActivity.this, "Bought successully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(CompanyDetailActivity.this, t.getLocalizedMessage() + "error", Toast.LENGTH_SHORT).show();
                }
            });
            checkPortfolio(user1.get_id());
        }
        else{
            Toast.makeText(this, "Sorry, Balance not enough!", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkPortfolio(String userId){
        PortfolioAPI portfolioAPI = RetrofitUrl.getInstance().create(PortfolioAPI.class);
        Call<PortfolioModel> portfolioCall = portfolioAPI.getMyPortfolioCo(RetrofitUrl.token, userId, companyId);

        portfolioCall.enqueue(new Callback<PortfolioModel>() {
            @Override
            public void onResponse(Call<PortfolioModel> call, Response<PortfolioModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CompanyDetailActivity.this, "Code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(response.body() != null) {
                    editPortfolio(user1.get_id(), response.body().getShareBalance());
                }
                else {
                    newPortfolio(user1.get_id());
                }

            }

            @Override
            public void onFailure(Call<PortfolioModel> call, Throwable t) {
                Toast.makeText(CompanyDetailActivity.this, t.getLocalizedMessage() + "error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void editPortfolio(String userId, int lastshares){
        PortfolioModel portfolio = new PortfolioModel(userId, lastshares + Integer.parseInt(etStockQty.getText().toString()), selectedCompany.getSharePrice(), selectedCompany.getSharePrice(), selectedCompany.getSharePrice());
        PortfolioAPI portfolioAPI = RetrofitUrl.getInstance().create(PortfolioAPI.class);
        Call<Void> portfolioCall = portfolioAPI.updatePortfolio(RetrofitUrl.token ,userId, selectedCompany.get_id(), portfolio);

        portfolioCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CompanyDetailActivity.this, "Code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CompanyDetailActivity.this, t.getLocalizedMessage() + "error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void newPortfolio(String userId){
        PortfolioModel portfolio = new PortfolioModel(selectedCompany, userId, Integer.parseInt(etStockQty.getText().toString()), selectedCompany.getSharePrice(), selectedCompany.getSharePrice(), selectedCompany.getSharePrice());

        PortfolioAPI portfolioAPI = RetrofitUrl.getInstance().create(PortfolioAPI.class);
        Call<Void> portfolioCall = portfolioAPI.addPortfolio(RetrofitUrl.token, portfolio);

        portfolioCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CompanyDetailActivity.this, "Code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CompanyDetailActivity.this, t.getLocalizedMessage() + "error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    
    public boolean checkBalance(int qty, float amt){
        if(balance.getvCoinBalance() < qty*amt){
            return false;
        }
        return true;
    }

    public void manageBalance(int q, float a, String status){
        BalanceModel updatedBalance;
        if(status.equals("buy"))
            updatedBalance = new BalanceModel(balance.getInitialVCoin(), balance.getvCoinBalance() - (q*a), balance.getTotalValue(), balance.getvCoinInvested() + (q*a));
        else
            updatedBalance = new BalanceModel(balance.getInitialVCoin(), balance.getvCoinBalance() + (q*a), balance.getTotalValue(), balance.getvCoinInvested());

        BalanceAPI balanceAPI = RetrofitUrl.getInstance().create(BalanceAPI.class);
        Call<Void> balanceCall = balanceAPI.updateBalance(RetrofitUrl.token, user1.get_id(), updatedBalance);

        balanceCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(CompanyDetailActivity.this, "Error loading balance "+ response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CompanyDetailActivity.this, "Error loading balance "+ t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sellShares(){
        TradeModel trade = new TradeModel(selectedCompany.get_id(), user1.get_id(), "selling", "Sold a share", "complete", Calendar.getInstance().getTime(), 0, Integer.parseInt(etStockQty.getText().toString()), 0, selectedCompany.getSharePrice());
//        if(checkSellingBalance) {
            TradeAPI tradeAPI = RetrofitUrl.getInstance().create(TradeAPI.class);
            Call<Void> tradeCall = tradeAPI.addTrade(RetrofitUrl.token, trade);

            tradeCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(CompanyDetailActivity.this, "Code " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    manageBalance(Integer.parseInt(etStockQty.getText().toString()), selectedCompany.getSharePrice(), "sold");
                    Toast.makeText(CompanyDetailActivity.this, "Sold successully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(CompanyDetailActivity.this, t.getLocalizedMessage() + "error", Toast.LENGTH_SHORT).show();
                }
            });
            checkPortfolioForSales(user1.get_id(), Integer.parseInt(etStockQty.getText().toString()), selectedCompany.get_id());
//        }
//        else{
//            Toast.makeText(this, "Sorry, Balance not enough!", Toast.LENGTH_SHORT).show();
//        }
    }

    public void checkPortfolioForSales(String userId, final int qty, String id){
        PortfolioAPI portfolioAPI = RetrofitUrl.getInstance().create(PortfolioAPI.class);
        Call<PortfolioModel> portfolioCall = portfolioAPI.getMyPortfolioCo(RetrofitUrl.token, userId, id);

        portfolioCall.enqueue(new Callback<PortfolioModel>() {
            @Override
            public void onResponse(Call<PortfolioModel> call, Response<PortfolioModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CompanyDetailActivity.this, "Code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(response.body() != null) {
                    int quantity = response.body().getShareBalance();
                    if(qty > quantity){
                        Toast.makeText(CompanyDetailActivity.this, "You don't own much shares to sell", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        deductPortfolio(user1.get_id(), response.body().getShareBalance());
                    }
                }
                else {
                    Toast.makeText(CompanyDetailActivity.this, "You don't own shares of this company", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<PortfolioModel> call, Throwable t) {
                Toast.makeText(CompanyDetailActivity.this, t.getLocalizedMessage() + "error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void deductPortfolio(String userId, int lastshares){
        PortfolioModel portfolio = new PortfolioModel(userId, lastshares - Integer.parseInt(etStockQty.getText().toString()), selectedCompany.getSharePrice(), selectedCompany.getSharePrice(), selectedCompany.getSharePrice());
        PortfolioAPI portfolioAPI = RetrofitUrl.getInstance().create(PortfolioAPI.class);
        Call<Void> portfolioCall = portfolioAPI.updatePortfolio(RetrofitUrl.token ,userId, selectedCompany.get_id(), portfolio);

        portfolioCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CompanyDetailActivity.this, "Code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CompanyDetailActivity.this, t.getLocalizedMessage() + "error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
