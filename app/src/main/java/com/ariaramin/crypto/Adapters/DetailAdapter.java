package com.ariaramin.crypto.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ariaramin.crypto.R;
import com.ariaramin.crypto.databinding.DetailItemLayoutBinding;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder>{

    ArrayList<String> detailKeysArray;
    ArrayList<String> detailValuesArray;

    public DetailAdapter(ArrayList<String> detailKeysArray, ArrayList<String> detailValuesArray) {
        this.detailKeysArray = detailKeysArray;
        this.detailValuesArray = detailValuesArray;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DetailItemLayoutBinding detailItemLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.detail_item_layout, parent, false);
        return new DetailViewHolder(detailItemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        holder.bindData(detailKeysArray.get(position), detailValuesArray.get(position));
    }

    @Override
    public int getItemCount() {
        return detailKeysArray.size();
    }

    static class DetailViewHolder extends RecyclerView.ViewHolder {

        DetailItemLayoutBinding itemLayoutBinding;

        public DetailViewHolder(DetailItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }

        private void bindData(String key, String value) {
            if (key.equals("PercentChange 7d") || key.equals("PercentChange 30d")) {
                setValueTextColor(value, itemLayoutBinding.detailValueTextView);
            }
            itemLayoutBinding.detailValueTextView.setText(value);
            itemLayoutBinding.detailKeyTextView.setText(key);
        }

        private void setValueTextColor(String value, TextView textView) {
            if (Float.parseFloat(value) < 0) {
                int red = itemLayoutBinding.getRoot().getContext().getResources().getColor(R.color.red);
                textView.setTextColor(red);
            } else {
                int green = itemLayoutBinding.getRoot().getContext().getResources().getColor(R.color.green);
                textView.setTextColor(green);
            }
        }
    }
}
