package com.ariaramin.crypto.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ariaramin.crypto.Models.DataItem;
import com.ariaramin.crypto.R;
import com.ariaramin.crypto.Room.Entities.AllMarketEntity;
import com.ariaramin.crypto.databinding.TopCurrencyLayoutBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TopCurrencyAdapter extends RecyclerView.Adapter<TopCurrencyAdapter.TopCurrencyViewHolder> {

    List<DataItem> dataItems;

    public TopCurrencyAdapter(List<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public TopCurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TopCurrencyLayoutBinding topCurrencyLayoutBinding = DataBindingUtil.inflate(layoutInflater, R.layout.top_currency_layout, parent, false);
        return new TopCurrencyViewHolder(topCurrencyLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TopCurrencyViewHolder holder, int position) {
        holder.bindData(dataItems.get(position));
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public void updateList(ArrayList<DataItem> dataItemList) {
        dataItems = dataItemList;
        notifyDataSetChanged();
    }

    static class TopCurrencyViewHolder extends RecyclerView.ViewHolder {

        TopCurrencyLayoutBinding topCurrencyLayoutBinding;

        public TopCurrencyViewHolder(@NonNull TopCurrencyLayoutBinding topCurrencyLayoutBinding) {
            super(topCurrencyLayoutBinding.getRoot());
            this.topCurrencyLayoutBinding = topCurrencyLayoutBinding;
        }

        public void bindData(DataItem dataItem) {
            loadCoinLogo(dataItem);
            setChangeText(dataItem);
            topCurrencyLayoutBinding.topCurrencyNameTextView.setText(dataItem.getName());
            topCurrencyLayoutBinding.executePendingBindings();
        }

        private void loadCoinLogo(DataItem dataItem) {
            Glide.with(topCurrencyLayoutBinding.getRoot().getContext())
                    .load("https://s2.coinmarketcap.com/static/img/coins/64x64/" + dataItem.getId() + ".png")
                    .thumbnail(
                            Glide.with(topCurrencyLayoutBinding.getRoot().getContext())
                                    .load(R.drawable.spinner))
                    .into(topCurrencyLayoutBinding.topCurrencyImageView);
        }

        private void setChangeText(DataItem dataItem) {
            if (dataItem.getListQuote().get(0).getPercentChange24h() > 0){
                int green = topCurrencyLayoutBinding.getRoot().getContext().getResources().getColor(R.color.green);
                topCurrencyLayoutBinding.topCurrencyChangeTextView.setTextColor(green);
                topCurrencyLayoutBinding.topCurrencyChangeTextView.setText(String.format("+%.2f",dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
            }else {
                int red = topCurrencyLayoutBinding.getRoot().getContext().getResources().getColor(R.color.red);
                topCurrencyLayoutBinding.topCurrencyChangeTextView.setTextColor(red);
                topCurrencyLayoutBinding.topCurrencyChangeTextView.setText(String.format("%.2f",dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
            }
        }

//        private void setPriceDecimal(DataItem dataItem) {
//            if (dataItem.getListQuote().get(0).getPrice() < 1) {
//                topCurrencyLayoutBinding.topCurrencyChangeTextView.setText(String.format("%.6f", dataItem.getListQuote().get(0).getPrice()));
//            } else if (dataItem.getListQuote().get(0).getPrice() < 10) {
//                topCurrencyLayoutBinding.topCurrencyChangeTextView.setText(String.format("%.4f", dataItem.getListQuote().get(0).getPrice()));
//            } else {
//                topCurrencyLayoutBinding.topCurrencyChangeTextView.setText(String.format("%.2f", dataItem.getListQuote().get(0).getPrice()));
//            }
//        }

//        private void setTextColor(DataItem dataItem) {
//            if (dataItem.getListQuote().get(0).getPercentChange24h() < 0) {
//                int red = topCurrencyLayoutBinding.getRoot().getContext().getResources().getColor(R.color.red);
//                topCurrencyLayoutBinding.topCurrencyPriceTextView.setTextColor(red);
//            } else {
//                int green = topCurrencyLayoutBinding.getRoot().getContext().getResources().getColor(R.color.green);
//                topCurrencyLayoutBinding.topCurrencyPriceTextView.setTextColor(green);
//            }
//        }
    }
}