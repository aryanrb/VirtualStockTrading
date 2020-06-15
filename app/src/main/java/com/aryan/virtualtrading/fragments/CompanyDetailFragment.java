package com.aryan.virtualtrading.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aryan.virtualtrading.R;
import com.aryan.virtualtrading.RetrofitUrl;
import com.aryan.virtualtrading.activities.MainActivity;
import com.aryan.virtualtrading.api.MarketAPI;
import com.aryan.virtualtrading.api.PortfolioAPI;
import com.aryan.virtualtrading.api.TradeAPI;
import com.aryan.virtualtrading.models.MarketModel;
import com.aryan.virtualtrading.models.PortfolioModel;
import com.aryan.virtualtrading.models.TradeModel;
import com.aryan.virtualtrading.models.UserModel;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyDetailFragment extends Fragment implements View.OnClickListener{

    private TextView tvCompanyName, tvCompanySymbol, tvSharePrice;
    private EditText etStockQty;
    private Button btnBuy, btnSell;
    private MarketModel selectedCompany;
    private int doIHave = 0;
    private String companyId = "5ee1c0eca934e5070c15c87e";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_company_detail, container, false);

        tvCompanyName = root.findViewById(R.id.tvCompanyName);
        tvCompanySymbol = root.findViewById(R.id.tvCompanySymbol);
        tvSharePrice = root.findViewById(R.id.tvSharePrice);
        etStockQty = root.findViewById(R.id.etStockQty);
        btnBuy = root.findViewById(R.id.btnBuy);
        btnSell = root.findViewById(R.id.btnSell);

        getCompanyDetail();
        btnBuy.setOnClickListener(this);
        btnSell.setOnClickListener(this);

        return root;
    }

    private void getCompanyDetail(){
        MarketAPI companyAPI = RetrofitUrl.getInstance().create(MarketAPI.class);
        Call<MarketModel> companyCall = companyAPI.getCompanyDetail(companyId);

        companyCall.enqueue(new Callback<MarketModel>() {
            @Override
            public void onResponse(Call<MarketModel> call, Response<MarketModel> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Error "+ response.body(), Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedCompany = response.body();
                tvCompanyName.setText(selectedCompany.getName());
                tvCompanySymbol.setText(selectedCompany.getSymbol());
                tvSharePrice.setText(selectedCompany.getSharePrice() + "");
            }

            @Override
            public void onFailure(Call<MarketModel> call, Throwable t) {
                Toast.makeText(getContext(), "Error "+ t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
        MainActivity main = new MainActivity();
        UserModel user = main.getUserProfile();

        TradeModel trade = new TradeModel(selectedCompany.get_id(), user.get_id(), "buying", "Buy a share", "complete", Calendar.getInstance().getTime(), 0, 0, Integer.parseInt(etStockQty.getText().toString()), selectedCompany.getSharePrice());

        TradeAPI tradeAPI = RetrofitUrl.getInstance().create(TradeAPI.class);
        Call<Void> tradeCall = tradeAPI.addTrade(RetrofitUrl.token, trade);

        tradeCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                etStockQty.setText("");
                Toast.makeText(getContext(), "Bought successully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage() + "error", Toast.LENGTH_SHORT).show();
            }
        });

        checkPortfolio(user.get_id());
        if(doIHave == 0){
            newPortfolio(user.get_id());

        }
        else if(doIHave > 0){
            editPortfolio(user.get_id());
        }

    }

    public void checkPortfolio(String userId){
        PortfolioAPI portfolioAPI = RetrofitUrl.getInstance().create(PortfolioAPI.class);
        Call<PortfolioModel> portfolioCall = portfolioAPI.getMyPortfolioCo(RetrofitUrl.token, userId, companyId);

        portfolioCall.enqueue(new Callback<PortfolioModel>() {
            @Override
            public void onResponse(Call<PortfolioModel> call, Response<PortfolioModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                doIHave = response.body().getShareBalance();


            }

            @Override
            public void onFailure(Call<PortfolioModel> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage() + "error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void editPortfolio(String userId){
        PortfolioModel portfolio = new PortfolioModel(selectedCompany.get_id(), userId, doIHave + Integer.parseInt(etStockQty.getText().toString()), selectedCompany.getSharePrice(), selectedCompany.getSharePrice(), selectedCompany.getSharePrice());
        PortfolioAPI portfolioAPI = RetrofitUrl.getInstance().create(PortfolioAPI.class);
        Call<Void> portfolioCall = portfolioAPI.updatePortfolio(RetrofitUrl.token, selectedCompany.get_id(), userId, portfolio);

        portfolioCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                etStockQty.setText("1");

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage() + "error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void newPortfolio(String userId){
        PortfolioModel portfolio = new PortfolioModel(selectedCompany.get_id(), userId, Integer.parseInt(etStockQty.getText().toString()), selectedCompany.getSharePrice(), selectedCompany.getSharePrice(), selectedCompany.getSharePrice());

        PortfolioAPI portfolioAPI = RetrofitUrl.getInstance().create(PortfolioAPI.class);
        Call<Void> portfolioCall = portfolioAPI.addPortfolio(RetrofitUrl.token, portfolio);

        portfolioCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                etStockQty.setText("2");

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage() + "error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void sellShares(){

    }
}
