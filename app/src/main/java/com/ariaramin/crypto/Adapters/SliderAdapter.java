package com.ariaramin.crypto.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.ariaramin.crypto.R;
import com.ariaramin.crypto.databinding.SliderItemLayoutBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder> {

    ArrayList<Integer> sliderImages = new ArrayList<>();

    public SliderAdapter(ArrayList<Integer> images) {
        sliderImages = images;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SliderItemLayoutBinding sliderItemLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.slider_item_layout, parent, false);
        return new SliderViewHolder(sliderItemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        viewHolder.bindData(sliderImages.get(position));
    }

    @Override
    public int getCount() {
        return sliderImages.size();
    }

    static class SliderViewHolder extends SliderViewAdapter.ViewHolder {

        SliderItemLayoutBinding sliderItemLayoutBinding;

        public SliderViewHolder(SliderItemLayoutBinding sliderItemLayoutBinding) {
            super(sliderItemLayoutBinding.getRoot());
            this.sliderItemLayoutBinding = sliderItemLayoutBinding;
        }

        public void bindData(Integer image) {
            Glide.with(sliderItemLayoutBinding.getRoot().getContext())
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(sliderItemLayoutBinding.sliderImageView);
            sliderItemLayoutBinding.executePendingBindings();
        }
    }
}