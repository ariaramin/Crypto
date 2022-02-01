package com.ariaramin.crypto.Adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ariaramin.crypto.Models.DataItem;
import com.ariaramin.crypto.R;
import com.ariaramin.crypto.databinding.CurrencyItemLayoutBinding;
import com.ariaramin.crypto.ui.Fragments.HomeFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    List<DataItem> dataItems;
    String TAG;

    public CurrencyAdapter(String tag, List<DataItem> dataItems) {
        TAG = tag;
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CurrencyItemLayoutBinding currencyItemLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.currency_item_layout, parent, false);
        return new CurrencyViewHolder(currencyItemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        holder.bindData(TAG, dataItems.get(position));
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public void updateList(List<DataItem> dataItemList) {
        dataItems = dataItemList;
        notifyDataSetChanged();
    }


    static class CurrencyViewHolder extends RecyclerView.ViewHolder {

        CurrencyItemLayoutBinding currencyItemLayoutBinding;

        public CurrencyViewHolder(@NonNull CurrencyItemLayoutBinding currencyItemLayoutBinding) {
            super(currencyItemLayoutBinding.getRoot());
            this.currencyItemLayoutBinding = currencyItemLayoutBinding;
        }

        public void bindData(String tag, DataItem dataItem) {
            loadCoinLogo(dataItem);
            setPriceDecimal(dataItem);
            setChange(dataItem);
            loadChart(dataItem);
            currencyItemLayoutBinding.currencyNameTextView.setText(dataItem.getName());
            currencyItemLayoutBinding.currencySymbolTextView.setText(dataItem.getSymbol());
            currencyItemLayoutBinding.currencyCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Coin", dataItem);

                    switch (tag) {
                        case "topGainLose":
                            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_detailFragment, bundle);
                            break;
                        case "market":
                            Navigation.findNavController(v).navigate(R.id.action_marketFragment_to_detailFragment, bundle);
                            break;
                        case "watchlist":
                            Navigation.findNavController(v).navigate(R.id.action_watchListFragment_to_detailFragment, bundle);
                            break;
                    }
                }
            });
            currencyItemLayoutBinding.executePendingBindings();
        }

        private void loadCoinLogo(DataItem dataItem) {
            Glide.with(currencyItemLayoutBinding.getRoot().getContext())
                    .load("https://s2.coinmarketcap.com/static/img/coins/64x64/" + dataItem.getId() + ".png")
                    .thumbnail(
                            Glide.with(currencyItemLayoutBinding.getRoot().getContext())
                                    .load(R.drawable.spinner))
                    .into(currencyItemLayoutBinding.currencyImageView);
        }

        private void setPriceDecimal(DataItem dataItem) {
            if (dataItem.getListQuote().get(0).getPrice() < 1) {
                currencyItemLayoutBinding.currencyPriceTextView.setText(String.format("$%.6f", dataItem.getListQuote().get(0).getPrice()));
            } else if (dataItem.getListQuote().get(0).getPrice() < 10) {
                currencyItemLayoutBinding.currencyPriceTextView.setText(String.format("$%.4f", dataItem.getListQuote().get(0).getPrice()));
            } else {
                currencyItemLayoutBinding.currencyPriceTextView.setText(String.format("$%.2f", dataItem.getListQuote().get(0).getPrice()));
            }
        }

        private void setChange(DataItem dataItem) {
            if (dataItem.getListQuote().get(0).getPercentChange24h() < 0) {
                int red = currencyItemLayoutBinding.getRoot().getContext().getResources().getColor(R.color.red);
                currencyItemLayoutBinding.currencyChangeTextView.setTextColor(red);
                currencyItemLayoutBinding.currencyChangeImageView.setImageResource(R.drawable.ic_caret_down);
                currencyItemLayoutBinding.currencyChangeTextView.setText(String.format("%.2f", dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
            } else {
                int green = currencyItemLayoutBinding.getRoot().getContext().getResources().getColor(R.color.green);
                currencyItemLayoutBinding.currencyChangeTextView.setTextColor(green);
                currencyItemLayoutBinding.currencyChangeImageView.setImageResource(R.drawable.ic_caret_up);
                currencyItemLayoutBinding.currencyChangeTextView.setText(String.format("+%.2f", dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
            }
        }

        private void loadChart(DataItem dataItem) {
            if (dataItem.getListQuote().get(0).getPercentChange24h() < 0) {
                int red = currencyItemLayoutBinding.getRoot().getContext().getResources().getColor(R.color.red);
                currencyItemLayoutBinding.currencyChartImageView.setColorFilter(red);
            } else {
                int green = currencyItemLayoutBinding.getRoot().getContext().getResources().getColor(R.color.green);
                currencyItemLayoutBinding.currencyChartImageView.setColorFilter(green);
            }
            Glide.with(currencyItemLayoutBinding.getRoot().getContext())
                    .load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/" + dataItem.getId() + ".png")
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(currencyItemLayoutBinding.currencyChartImageView);
        }
    }

}
