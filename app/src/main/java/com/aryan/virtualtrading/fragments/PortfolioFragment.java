package com.aryan.virtualtrading.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aryan.virtualtrading.activities.CompanyDetailActivity;
import com.aryan.virtualtrading.activities.MainActivity;
import com.aryan.virtualtrading.adapters.PortfolioListAdapter;
import com.aryan.virtualtrading.api.MarketAPI;
import com.aryan.virtualtrading.adapters.MarketListAdapter;
import com.aryan.virtualtrading.api.PortfolioAPI;
import com.aryan.virtualtrading.models.MarketModel;
import com.aryan.virtualtrading.R;
import com.aryan.virtualtrading.RetrofitUrl;
import com.aryan.virtualtrading.models.PortfolioModel;
import com.aryan.virtualtrading.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortfolioFragment extends Fragment implements MarketListAdapter.OnCompanyListener {

    private RecyclerView rvPortfolio;
    private List<PortfolioModel> portfolioList;
    public UserModel user = MainActivity.userProfile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_portfolio, container, false);

        rvPortfolio = root.findViewById(R.id.rvPortfolio);
        getPortfolio();
        return root;
    }

    private void getPortfolio() {

        PortfolioAPI portfolioAPI  = RetrofitUrl.getInstance().create(PortfolioAPI.class);
        Call<List<PortfolioModel>> portfolioCall = portfolioAPI.getMyPortfolio(RetrofitUrl.token, user.get_id());

        portfolioCall.enqueue(new Callback<List<PortfolioModel>>() {
            @Override
            public void onResponse(Call<List<PortfolioModel>> call, Response<List<PortfolioModel>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                portfolioList = response.body();
//                MarketListAdapter adapter = new MarketListAdapter(portfolioList, getContext(), PortfolioFragment.class);
                PortfolioListAdapter adapter = new PortfolioListAdapter(portfolioList);
                rvPortfolio.setAdapter(adapter);
                rvPortfolio.setLayoutManager(new LinearLayoutManager(getContext()));

            }

            @Override
            public void onFailure(Call<List<PortfolioModel>> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage() + "error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCompanyClick(int position, String name) {

    }
}