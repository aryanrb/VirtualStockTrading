package com.aryan.virtualtrading.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aryan.virtualtrading.R;
import com.aryan.virtualtrading.models.PortfolioModel;
import com.aryan.virtualtrading.models.TradeModel;

import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.HistoryListViewHolder>{

    List<TradeModel> historyList;

    public HistoryListAdapter(List<TradeModel> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.portfolio_stock, parent, false);

        return new HistoryListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryListViewHolder holder, int position) {

        final TradeModel history = historyList.get(position);

        holder.valueamt.setText(history.getCurrentBalance()+"");
        holder.nameofcompany.setText(history.getCompany());
        holder.lastprice.setText(history.getPricePerShare() + "");
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class HistoryListViewHolder extends RecyclerView.ViewHolder {
        TextView valueamt, nameofcompany, lastprice;

        public HistoryListViewHolder(@NonNull View itemView) {
            super(itemView);
            valueamt = itemView.findViewById(R.id.valueamt);
            nameofcompany = itemView.findViewById(R.id.nameofcompany);
            lastprice = itemView.findViewById(R.id.tv_symbol);

        }
    }


    }
