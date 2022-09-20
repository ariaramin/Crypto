package com.ariaramin.crypto.ui.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ariaramin.crypto.Adapters.CurrencyAdapter;
import com.ariaramin.crypto.MainActivity;
import com.ariaramin.crypto.MainViewModel;
import com.ariaramin.crypto.Models.AllMarket;
import com.ariaramin.crypto.Models.DataItem;
import com.ariaramin.crypto.R;
import com.ariaramin.crypto.Room.Entities.AllMarketEntity;
import com.ariaramin.crypto.databinding.FragmentWatchlistBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WatchlistFragment extends Fragment {

    FragmentWatchlistBinding watchListBinding;
    MainActivity mainActivity;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    ArrayList<String> watchlist;
    ArrayList<DataItem> watchlistItems;
    private static final String TAG = "watchlist";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        watchListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_watchlist, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        watchlistItems = new ArrayList<>();

        readData();
        if (watchlist.isEmpty()) {
            watchListBinding.emptyTextView.setVisibility(View.VISIBLE);
        }
        getAllMarket();
        return watchListBinding.getRoot();
    }

    private void getAllMarket() {
        Disposable disposable = mainViewModel.getAllMarketEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(allMarketEntity -> {
                    AllMarket allMarket = allMarketEntity.getAllMarket();
                    List<DataItem> dataItems = allMarket.getData().getCryptoCurrencyList();

                    watchlistItems.clear();
                    for (int i = 0; i < watchlist.size(); i++) {
                        for (int j = 0; j < dataItems.size(); j++) {
                            if (watchlist.get(i).equals(dataItems.get(j).getSymbol())) {
                                watchlistItems.add(dataItems.get(j));
                            }
                        }
                    }

                    if (watchListBinding.watchlistRecyclerView.getAdapter() == null) {
                        CurrencyAdapter currencyAdapter = new CurrencyAdapter(TAG, watchlistItems);
                        watchListBinding.watchlistRecyclerView.setAdapter(currencyAdapter);
                    } else {
                        CurrencyAdapter currencyAdapter = (CurrencyAdapter) watchListBinding.watchlistRecyclerView.getAdapter();
                        currencyAdapter.updateList(watchlistItems);
                    }

                    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                            int position = viewHolder.getAdapterPosition();
                            String coinName = watchlistItems.get(position).getName();
                            watchlistItems.remove(position);
                            watchlist.remove(position);
                            storeData(watchlist);
                            watchListBinding.watchlistRecyclerView.getAdapter().notifyItemRemoved(position);
                            Toast.makeText(mainActivity, coinName+" removed from your watchlist", Toast.LENGTH_SHORT).show();
                            if (watchlist.isEmpty()) {
                                watchListBinding.emptyTextView.setVisibility(View.VISIBLE);
                            }
                        }
                    }).attachToRecyclerView(watchListBinding.watchlistRecyclerView);
                });

        compositeDisposable.add(disposable);
    }

    private void readData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("watchlist", String.valueOf(new ArrayList<String>()));
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        watchlist = gson.fromJson(json, type);
    }

    private void storeData(ArrayList<String> newList) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(newList);
        editor.putString("watchlist", json);
        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}