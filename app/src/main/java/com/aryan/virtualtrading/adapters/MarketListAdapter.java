package com.aryan.virtualtrading.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.aryan.virtualtrading.R;
import com.aryan.virtualtrading.models.MarketModel;

import java.util.ArrayList;
import java.util.List;


public class MarketListAdapter extends RecyclerView.Adapter<MarketListAdapter.MarketListViewHolder> {

    private NotificationManagerCompat notificationManagerCompat;
    OnCompanyListener mCompanyListener;

    List<MarketModel> marketList;
    Context mContext;

    public MarketListAdapter(List<MarketModel> marketList, Context mContext, OnCompanyListener mCompanyListener) {
        this.marketList = marketList;
        this.mContext = mContext;
        this.mCompanyListener = mCompanyListener;
    }

    @NonNull
    @Override
    public MarketListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.portfolio_stock, parent, false);
        notificationManagerCompat = NotificationManagerCompat.from(mContext);

        return new MarketListViewHolder(view, mCompanyListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketListViewHolder holder, int position) {
        final MarketModel market = marketList.get(position);

        holder.valueamt.setText(market.getSharePrice() + "");
        holder.nameofcompany.setText(market.getName());
        holder.markettype.setText(market.getSymbol());
        holder.tvPerChange.setText(market.getEps() + "%");
    }

    @Override
    public int getItemCount() {
        return marketList.size();
    }

    public void filterCompany(ArrayList<MarketModel> list){
        marketList = list;
        notifyDataSetChanged();
    }
    public class MarketListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView valueamt, nameofcompany, markettype, tvPerChange;
        CardView details_card;
        OnCompanyListener onCompanyListener;

        public MarketListViewHolder(@NonNull View itemView, OnCompanyListener onCompanyListener) {
            super(itemView);
            valueamt = itemView.findViewById(R.id.valueamt);
            nameofcompany = itemView.findViewById(R.id.nameofcompany);
            markettype = itemView.findViewById(R.id.tv_symbol);
            details_card = itemView.findViewById(R.id.details_card);
            tvPerChange = itemView.findViewById(R.id.tvPerChange);
            this.onCompanyListener = onCompanyListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onCompanyListener.onCompanyClick(getAdapterPosition(), marketList.get(getAdapterPosition()).get_id());
        }
    }

    public interface OnCompanyListener{
        void onCompanyClick(int position, String name);
    }
}
