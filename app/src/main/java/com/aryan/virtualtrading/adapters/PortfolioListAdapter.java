package com.aryan.virtualtrading.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aryan.virtualtrading.R;
import com.aryan.virtualtrading.models.PortfolioModel;

import java.util.List;

public class PortfolioListAdapter extends RecyclerView.Adapter<PortfolioListAdapter.PortfolioListViewHolder>{

    List<PortfolioModel> portfolioList;

    public PortfolioListAdapter(List<PortfolioModel> portfolioList) {
        this.portfolioList = portfolioList;
    }

    @NonNull
    @Override
    public PortfolioListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.portfolio_stock, parent, false);

        return new PortfolioListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortfolioListViewHolder holder, int position) {

        final PortfolioModel portfolio = portfolioList.get(position);

        holder.valueamt.setText(portfolio.getShareBalance()+"");
        holder.nameofcompany.setText(portfolio.getCompany());
        holder.lastprice.setText(portfolio.getPreviousPrice() + "");
    }

    @Override
    public int getItemCount() {
        return portfolioList.size();
    }

    public class PortfolioListViewHolder extends RecyclerView.ViewHolder {
        TextView valueamt, nameofcompany, lastprice;

        public PortfolioListViewHolder(@NonNull View itemView) {
            super(itemView);
            valueamt = itemView.findViewById(R.id.valueamt);
            nameofcompany = itemView.findViewById(R.id.nameofcompany);
            lastprice = itemView.findViewById(R.id.tv_symbol);

        }
    }


    }
