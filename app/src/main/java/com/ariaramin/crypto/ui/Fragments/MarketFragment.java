package com.ariaramin.crypto.ui.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.crypto.Adapters.CurrencyAdapter;
import com.ariaramin.crypto.MainActivity;
import com.ariaramin.crypto.MainViewModel;
import com.ariaramin.crypto.Models.AllMarket;
import com.ariaramin.crypto.Models.DataItem;
import com.ariaramin.crypto.R;
import com.ariaramin.crypto.Room.Entities.AllMarketEntity;
import com.ariaramin.crypto.databinding.FragmentMarketBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MarketFragment extends Fragment {

    FragmentMarketBinding marketBinding;
    MainActivity mainActivity;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    String searchedText = "";
    private static final String TAG = "market";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        marketBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();

        searchCoin();
        setupRecyclerView();
        return marketBinding.getRoot();
    }

    private void searchCoin() {
        marketBinding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchedText = s.toString().toLowerCase(Locale.ROOT);
            }
        });
    }

    private void setupRecyclerView() {
        Disposable disposable = mainViewModel.getAllMarketEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AllMarketEntity>() {
                    @Override
                    public void accept(AllMarketEntity allMarketEntity) throws Throwable {
                        AllMarket allMarket = allMarketEntity.getAllMarket();
                        List<DataItem> dataItems = allMarket.getData().getCryptoCurrencyList();
                        List<DataItem> data = new ArrayList<>();

                        if (!searchedText.equals("")) {
                            for (DataItem item:
                                 dataItems) {
                                String coinName = item.getName().toLowerCase(Locale.ROOT);
                                String coinSymbol = item.getSymbol().toLowerCase(Locale.ROOT);
                                if (coinName.contains(searchedText) || coinSymbol.contains(searchedText)) {
                                    data.add(item);
                                }
                            }
                        } else {
                            data = dataItems;
                        }

                        if (data.isEmpty()) {
                            marketBinding.notFoundTextView.setVisibility(View.VISIBLE);
                        } else {
                            marketBinding.notFoundTextView.setVisibility(View.GONE);
                        }

                        if (marketBinding.currencyRecyclerView.getAdapter() == null) {
                            CurrencyAdapter currencyAdapter = new CurrencyAdapter(TAG, dataItems);
                            marketBinding.currencyRecyclerView.setAdapter(currencyAdapter);
                        } else {
                            CurrencyAdapter currencyAdapter = (CurrencyAdapter) marketBinding.currencyRecyclerView.getAdapter();
                            currencyAdapter.updateList(data);
                        }
                    }
                });

        compositeDisposable.add(disposable);
    }
}