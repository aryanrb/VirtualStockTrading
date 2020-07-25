package com.aryan.virtualtrading.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aryan.virtualtrading.R;
import com.aryan.virtualtrading.RetrofitUrl;
import com.aryan.virtualtrading.adapters.PortfolioListAdapter;
import com.aryan.virtualtrading.api.BalanceAPI;
import com.aryan.virtualtrading.api.PortfolioAPI;
import com.aryan.virtualtrading.api.UserAPI;
import com.aryan.virtualtrading.models.BalanceModel;
import com.aryan.virtualtrading.models.PortfolioModel;
import com.aryan.virtualtrading.models.UserModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment{

    RecyclerView rvTopInvestments;
    TextView name, balance, pl, investment;
    private List<PortfolioModel> portfolioList;
    public UserModel user;
    float totalBalance = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        name = root.findViewById(R.id.tvHomeName);
        balance = root.findViewById(R.id.tvValueAmount);
        investment = root.findViewById(R.id.tvInvestedAmt);
        pl = root.findViewById(R.id.tvProfitLossAmt);
        rvTopInvestments = root.findViewById(R.id.rvTopInvestments);
        getUserProfile();


        return root;
    }

    public void getUserProfile(){

        UserAPI userAPI = RetrofitUrl.getInstance().create(UserAPI.class);
        Call<UserModel> usersCall = userAPI.getUserProfile(RetrofitUrl.token);

        usersCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Error loading profile!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                user = response.body();
                name.setText(user.getUsername());
                getPortfolio(user.get_id());
                getBalance(user);

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(getContext(), "Error loading profile...", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void getPortfolio(String id) {

        PortfolioAPI portfolioAPI  = RetrofitUrl.getInstance().create(PortfolioAPI.class);
        Call<List<PortfolioModel>> portfolioCall = portfolioAPI.getMyPortfolio(RetrofitUrl.token, id);

        portfolioCall.enqueue(new Callback<List<PortfolioModel>>() {
            @Override
            public void onResponse(Call<List<PortfolioModel>> call, Response<List<PortfolioModel>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                portfolioList = response.body();
                filterTopInv(portfolioList);
                List<PortfolioModel> topThreeList = getTopThree(portfolioList);
                if(topThreeList.size() != 0) {
                    totalBalance = setBalanceValue(portfolioList);
                    PortfolioListAdapter adapter = new PortfolioListAdapter(topThreeList);
                    rvTopInvestments.setAdapter(adapter);
                    rvTopInvestments.setLayoutManager(new LinearLayoutManager(getContext()));
                }

            }

            @Override
            public void onFailure(Call<List<PortfolioModel>> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage() + "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterTopInv(List<PortfolioModel> list){
        Collections.sort(list, new Comparator<PortfolioModel>() {
            @Override
            public int compare(PortfolioModel o1, PortfolioModel o2) {
                return Float.valueOf(o2.getCurrentPrice() * o2.getShareBalance()).compareTo(o1.getCurrentPrice() * o1.getShareBalance());
                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

            }
        });
    }

    private List<PortfolioModel> getTopThree(List<PortfolioModel> list){
        List<PortfolioModel> newList = new ArrayList<>();
        if(list.size() == 0){
            return newList;
        }
        else if(list.size() == 1){
            newList.add(list.get(0));
        }
        else if(list.size() == 2) {
            for (int i = 0; i < 2; i++) {
                newList.add(list.get(i));
            }
        }
        else if(list.size() > 2) {
            for (int i = 0; i < 3; i++) {
                newList.add(list.get(i));
            }
        }
        return newList;
    }

    private void getBalance(final UserModel userModel){
        BalanceAPI balanceAPI = RetrofitUrl.getInstance().create(BalanceAPI.class);
        Call<BalanceModel> balanceCall = balanceAPI.getBalanceDetail(RetrofitUrl.token, userModel.get_id());

        balanceCall.enqueue(new Callback<BalanceModel>() {
            @Override
            public void onResponse(Call<BalanceModel> call, Response<BalanceModel> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code:" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BalanceModel userBalance = response.body();
                if(userBalance == null){
                    userBalance = new BalanceModel(100000f, 100000f, 100000f, 0f, userModel);

                }
                showAmt(userBalance);

            }

            @Override
            public void onFailure(Call<BalanceModel> call, Throwable t) {
                Toast.makeText(getContext(), "Error "+ t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAmt(BalanceModel bal){
        balance.setText("Rs. "+bal.getvCoinBalance());
        if(getProfitLoss(bal) < 0){
            pl.setTextColor(Color.parseColor("#ff3835"));
        }
        else{
            pl.setTextColor(Color.parseColor("#3eff3b"));
        }
        pl.setText("Rs. "+getProfitLoss(bal));
        investment.setText("Rs. "+bal.getvCoinInvested());
    }

    public float getProfitLoss(BalanceModel bal){
        return (totalBalance + bal.getvCoinBalance() - bal.getInitialVCoin());
    }

    public float setBalanceValue(List<PortfolioModel> portfolioModelList) {
        float total = 0;
        for(PortfolioModel portfolioModel: portfolioModelList){
            total = (portfolioModel.getCompany().getLastPrice() * portfolioModel.getShareBalance()) + total;
        }
        return total;
    }
}