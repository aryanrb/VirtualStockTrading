package com.aryan.virtualtrading.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aryan.virtualtrading.R;
import com.aryan.virtualtrading.RetrofitUrl;
import com.aryan.virtualtrading.activities.CompanyDetailActivity;
import com.aryan.virtualtrading.adapters.MarketListAdapter;
import com.aryan.virtualtrading.api.MarketAPI;
import com.aryan.virtualtrading.models.MarketModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketFragment extends Fragment implements MarketListAdapter.OnCompanyListener {

    List<MarketModel> list;
    MarketListAdapter mAdapter;
    EditText etSearch;
    private RecyclerView rvMarket;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_market, container, false);
        etSearch = root.findViewById(R.id.etSearchMarket);
        rvMarket = root.findViewById(R.id.stockListView);
        getMarket();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterSearch(s.toString());
            }
        });
        return root;
    }

    private void getMarket() {

        MarketAPI marketAPI = RetrofitUrl.getInstance().create(MarketAPI.class);
        Call<List<MarketModel>> marketCall = marketAPI.getMarket();

        marketCall.enqueue(new Callback<List<MarketModel>>() {
            @Override
            public void onResponse(Call<List<MarketModel>> call, Response<List<MarketModel>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Error loading fixtures" , Toast.LENGTH_SHORT).show();
                    return;
                }
                list = response.body();
                mAdapter = new MarketListAdapter(list, getContext(), MarketFragment.this);
                rvMarket.setAdapter(mAdapter);
                rvMarket.setLayoutManager(new LinearLayoutManager(getContext()));
                refresh(5000);
            }

            @Override
            public void onFailure(Call<List<MarketModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Error loading fixtures" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCompanyClick(int position, String id) {
        Intent intent = new Intent(getContext(), CompanyDetailActivity.class);
        intent.putExtra("companyidfordetail", id);
        startActivity(intent);
    }

    public void filterSearch(String name){

        ArrayList<MarketModel> filterList=new ArrayList<>();
        for(MarketModel company: list){
            if( company.getSymbol().toLowerCase().contains(name.toLowerCase())){
                filterList.add(company);
            }
        }
        mAdapter.filterCompany(filterList);
    }

    //for refreshing the activity every 5 second
    public void refresh(int milliseconds){
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getMarket();
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }
}