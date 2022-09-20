package com.ariaramin.crypto.ui.Fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.crypto.Adapters.CurrencyAdapter;
import com.ariaramin.crypto.MainViewModel;
import com.ariaramin.crypto.Models.AllMarket;
import com.ariaramin.crypto.Models.DataItem;
import com.ariaramin.crypto.R;
import com.ariaramin.crypto.Room.Entities.AllMarketEntity;
import com.ariaramin.crypto.databinding.FragmentTopGainLoseBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class TopGainLoseFragment extends Fragment {

    FragmentTopGainLoseBinding topGainLoseBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    private static final String TAG = "topGainLose";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        topGainLoseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_gain_lose, container, false);
        Bundle args = getArguments();
        int position = args.getInt("position");
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();

        setupRecyclerView(position);
        return topGainLoseBinding.getRoot();
    }

    private void setupRecyclerView(int pos) {
        Disposable disposable = mainViewModel.getAllMarketEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(allMarketEntity -> {
                    AllMarket allMarket = allMarketEntity.getAllMarket();
                    List<DataItem> dataItems = allMarket.getData().getCryptoCurrencyList();

                    // Sort data by change
                    Collections.sort(dataItems, (o1, o2) ->
                            Integer.compare((int) o2.getListQuote().get(0).getPercentChange24h(), (int) o1.getListQuote().get(0).getPercentChange24h())
                    );

                    ArrayList<DataItem> data = new ArrayList<>();
                    if (pos == 0) {
                        // Get ten first items
                        for (int i = 0; i < 10; i++) {
                            data.add(dataItems.get(i));
                        }
                    } else {
                        // Get ten last items
                        for (int i = 0; i < 10; i++) {
                            data.add(dataItems.get(dataItems.size() - 1 - i));
                        }
                    }

                    if (topGainLoseBinding.topGainLoseRecyclerView.getAdapter() == null) {
                        CurrencyAdapter currencyAdapter = new CurrencyAdapter(TAG, data);
                        topGainLoseBinding.topGainLoseRecyclerView.setAdapter(currencyAdapter);
                    } else {
                        CurrencyAdapter currencyAdapter = (CurrencyAdapter) topGainLoseBinding.topGainLoseRecyclerView.getAdapter();
                        currencyAdapter.updateList(data);
                    }

                    if (dataItems.isEmpty()) {
                        topGainLoseBinding.spinKitView.setVisibility(View.VISIBLE);
                    } else {
                        topGainLoseBinding.spinKitView.setVisibility(View.GONE);
                    }
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}