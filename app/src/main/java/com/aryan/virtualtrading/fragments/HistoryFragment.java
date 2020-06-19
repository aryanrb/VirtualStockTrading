package com.aryan.virtualtrading.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.aryan.virtualtrading.R;
import com.aryan.virtualtrading.RetrofitUrl;
import com.aryan.virtualtrading.activities.MainActivity;
import com.aryan.virtualtrading.adapters.HistoryListAdapter;
import com.aryan.virtualtrading.api.TradeAPI;
import com.aryan.virtualtrading.models.TradeModel;
import com.aryan.virtualtrading.models.UserModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    RecyclerView rvTradeHistory;
    EditText etSearch;
    UserModel user = MainActivity.userProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        rvTradeHistory = view.findViewById(R.id.rvTradeHistory);
        etSearch = view.findViewById(R.id.etSearchHistory);
        getHistory();

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
        return view;
    }

    public void getHistory(){
        TradeAPI tradeAPI = RetrofitUrl.getInstance().create(TradeAPI.class);
        final Call<List<TradeModel>> historyList = tradeAPI.getTradeHistory(RetrofitUrl.token, user.get_id());

        historyList.enqueue(new Callback<List<TradeModel>>() {
            @Override
            public void onResponse(Call<List<TradeModel>> call, Response<List<TradeModel>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code:" + response.code() , Toast.LENGTH_SHORT).show();
                    return;
                }

                List<TradeModel> tradeList = response.body();
                HistoryListAdapter adapter = new HistoryListAdapter(tradeList);
                rvTradeHistory.setAdapter(adapter);
                rvTradeHistory.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onFailure(Call<List<TradeModel>> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage() + "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void filterSearch(String name){
//
//        ArrayList<TradeModel> filterList=new ArrayList<>();
//        for(TradeModel history: list){
//            if( history.getCompany().toLowerCase().contains(name.toLowerCase())){
//                filterList.add(history);
//            }
//        }
//        adapter.filterPlayers(filterList);
    }
}
