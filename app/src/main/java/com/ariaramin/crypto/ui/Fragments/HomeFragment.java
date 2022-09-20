package com.ariaramin.crypto.ui.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.crypto.Adapters.SliderAdapter;
import com.ariaramin.crypto.Adapters.TopCurrencyAdapter;
import com.ariaramin.crypto.Adapters.TopGainLosePagerAdapter;
import com.ariaramin.crypto.MainActivity;
import com.ariaramin.crypto.Models.DataItem;
import com.ariaramin.crypto.R;
import com.ariaramin.crypto.MainViewModel;
import com.ariaramin.crypto.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    FragmentHomeBinding homeBinding;
    MainActivity mainActivity;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();

        getAllMarket();
        setupViewPager();
        setupTabLayout();
        return homeBinding.getRoot();
    }

    private void setupTabLayout() {
        TopGainLosePagerAdapter topGainLosePagerAdapter = new TopGainLosePagerAdapter(this);
        homeBinding.contentViewPager.setAdapter(topGainLosePagerAdapter);

        homeBinding.contentViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 0) {
                    homeBinding.topGainIndicator.setVisibility(View.VISIBLE);
                    homeBinding.topLoseIndicator.setVisibility(View.GONE);
                } else {
                    homeBinding.topGainIndicator.setVisibility(View.GONE);
                    homeBinding.topLoseIndicator.setVisibility(View.VISIBLE);
                }
            }
        });

        new TabLayoutMediator(homeBinding.tabLayout, homeBinding.contentViewPager, (tab, position) -> {
            String title;
            if (position == 0) {
                title = requireContext().getResources().getString(R.string.top_gainers);
            } else {
                title = requireContext().getResources().getString(R.string.top_losers);
            }
            tab.setText(title);
        }).attach();
    }

    private void getAllMarket() {
        Disposable disposable = mainViewModel.getAllMarketEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(allMarketEntity -> {
                    List<DataItem> dataItems = allMarketEntity.getAllMarket().getData().getCryptoCurrencyList().subList(0, 10);

                    if (homeBinding.topCurrencyRecyclerView.getAdapter() == null) {
                        TopCurrencyAdapter topCurrencyAdapter = new TopCurrencyAdapter(dataItems);
                        homeBinding.topCurrencyRecyclerView.setAdapter(topCurrencyAdapter);
                    } else {
                        TopCurrencyAdapter topCurrencyAdapter = (TopCurrencyAdapter) homeBinding.topCurrencyRecyclerView.getAdapter();
                        topCurrencyAdapter.updateList(dataItems);
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void setupViewPager() {
        mainViewModel.getSliderData().observe(requireActivity(), integers -> {
            SliderAdapter sliderAdapter = new SliderAdapter(integers);
            homeBinding.sliderView.setSliderAdapter(sliderAdapter);
            homeBinding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
            homeBinding.sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
            homeBinding.sliderView.setScrollTimeInSec(5);
            homeBinding.sliderView.startAutoCycle();
        });
    }
}