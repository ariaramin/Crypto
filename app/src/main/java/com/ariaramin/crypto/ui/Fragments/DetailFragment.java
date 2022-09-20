package com.ariaramin.crypto.ui.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.ariaramin.crypto.Adapters.DetailAdapter;
import com.ariaramin.crypto.MainActivity;
import com.ariaramin.crypto.Models.DataItem;
import com.ariaramin.crypto.R;
import com.ariaramin.crypto.databinding.FragmentDetailBinding;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DetailFragment extends Fragment {

    FragmentDetailBinding detailBinding;
    ArrayList<String> watchlist;
    Boolean watchlistIsChecked = false;
    MainActivity mainActivity;
    ArrayList<String> detailKeysArray;
    ArrayList<String> detailValuesArray;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        detailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        Bundle args = getArguments();
        DataItem dataItem = args.getParcelable("Coin");
        mainActivity.smoothBottomBar.setVisibility(View.GONE);
        detailBinding.backStackButton.setOnClickListener(v -> requireActivity().onBackPressed());

        setupDetail(dataItem);
        readData();
        addToWatchlist(dataItem);
        loadChart(dataItem);
        setButtonsClickListener(dataItem);
        setupDetailRecyclerView(dataItem);
        return detailBinding.getRoot();
    }

    private void setupDetailRecyclerView(DataItem dataItem) {
        fillDetailKeys();
        fillDetailValues(dataItem);
        DetailAdapter detailAdapter = new DetailAdapter(detailKeysArray, detailValuesArray);
        detailBinding.detailRecyclerView.setAdapter(detailAdapter);
    }

    private void fillDetailValues(DataItem dataItem) {
        detailValuesArray = new ArrayList<>();

        String marketCap = dataItem.getListQuote().get(0).getMarketCap().toString().split("\\.")[0];
        String volume24 = dataItem.getListQuote().get(0).getVolume24h().toString().split("\\.")[0];
        String dominance = String.format("%.2f", dataItem.getListQuote().get(0).getDominance());
        String percentageChange7 = String.format("%.2f", dataItem.getListQuote().get(0).getPercentChange7d());
        String percentageChange30 = String.format("%.2f", dataItem.getListQuote().get(0).getPercentChange30d());
        String high24 = setDecimal(dataItem.getHigh24h());
        String low24 = setDecimal(dataItem.getLow24h());
        String ath = setDecimal(dataItem.getAth());
        String atl = setDecimal(dataItem.getAtl());
        String totalSupply = dataItem.getTotalSupply().toString().split("\\.")[0];

        detailValuesArray.add(dataItem.getName());
        detailValuesArray.add("$" + marketCap);
        detailValuesArray.add("$" + volume24);
        detailValuesArray.add(dominance);
        detailValuesArray.add(percentageChange7);
        detailValuesArray.add(percentageChange30);
        detailValuesArray.add(high24);
        detailValuesArray.add(low24);
        detailValuesArray.add(ath);
        detailValuesArray.add(atl);
        detailValuesArray.add(totalSupply);
    }

    private String setDecimal(double price) {
        if (price < 1) {
            return String.format("$%.8f", price);
        } else if (price < 10) {
            return String.format("$%.6f", price);
        } else {
            return String.format("$%.4f", price);
        }
    }

    private void fillDetailKeys() {
        detailKeysArray = new ArrayList<>();

        detailKeysArray.add("Name");
        detailKeysArray.add("Market Cap");
        detailKeysArray.add("Volume 24h");
        detailKeysArray.add("Dominance");
        detailKeysArray.add("PercentChange 7d");
        detailKeysArray.add("PercentChange 30d");
        detailKeysArray.add("High 24h");
        detailKeysArray.add("Low 24h");
        detailKeysArray.add("All Time High");
        detailKeysArray.add("All Time Low");
        detailKeysArray.add("Total Supply");
    }

    private void setupDetail(DataItem dataItem) {
        detailBinding.detailSymbolTextView.setText(dataItem.getSymbol());
        loadCoinLogo(dataItem);
        setPriceDecimal(dataItem);
        setChange(dataItem);
    }

    private void loadCoinLogo(DataItem dataItem) {
        Glide.with(detailBinding.getRoot().getContext())
                .load("https://s2.coinmarketcap.com/static/img/coins/64x64/" + dataItem.getId() + ".png")
                .thumbnail(
                        Glide.with(detailBinding.getRoot().getContext())
                                .load(R.drawable.spinner))
                .into(detailBinding.detailImageView);
    }

    private void setPriceDecimal(DataItem dataItem) {
        if (dataItem.getListQuote().get(0).getPrice() < 1) {
            detailBinding.detailPriceTextView.setText(String.format("$%.8f", dataItem.getListQuote().get(0).getPrice()));
        } else if (dataItem.getListQuote().get(0).getPrice() < 10) {
            detailBinding.detailPriceTextView.setText(String.format("$%.6f", dataItem.getListQuote().get(0).getPrice()));
        } else {
            detailBinding.detailPriceTextView.setText(String.format("$%.4f", dataItem.getListQuote().get(0).getPrice()));
        }
    }

    private void setChange(DataItem dataItem) {
        if (dataItem.getListQuote().get(0).getPercentChange24h() < 0) {
            int red = detailBinding.getRoot().getContext().getResources().getColor(R.color.red);
            detailBinding.detailChangeTextView.setTextColor(red);
            detailBinding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down);
            detailBinding.detailChangeTextView.setText(String.format("%.2f", dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
        } else {
            int green = detailBinding.getRoot().getContext().getResources().getColor(R.color.green);
            detailBinding.detailChangeTextView.setTextColor(green);
            detailBinding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up);
            detailBinding.detailChangeTextView.setText(String.format("+%.2f", dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
        }
    }

    private void addToWatchlist(DataItem dataItem) {

        readData();

        if (watchlist.contains(dataItem.getSymbol())) {
            detailBinding.addWatchlistButton.setImageResource(R.drawable.ic_star);
            watchlistIsChecked = true;
        } else {
            detailBinding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline);
            watchlistIsChecked = false;
        }

        detailBinding.addWatchlistButton.setOnClickListener(v -> {
            if (!watchlistIsChecked) {
                if (!watchlist.contains(dataItem.getSymbol())) {
                    watchlist.add(dataItem.getSymbol());
                }
                storeData();
                detailBinding.addWatchlistButton.setImageResource(R.drawable.ic_star);
                watchlistIsChecked = true;
            } else {
                detailBinding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline);
                watchlist.remove(dataItem.getSymbol());
                storeData();
                watchlistIsChecked = false;
            }
        });
    }

    private void readData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("watchlist", String.valueOf(new ArrayList<String>()));
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        watchlist = gson.fromJson(json, type);
    }

    private void storeData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(watchlist);
        editor.putString("watchlist", json);
        editor.apply();
    }

    private void setButtonsClickListener(DataItem dataItem) {
        Button oneMonth = detailBinding.button;
        Button oneWeek = detailBinding.button1;
        Button oneDay = detailBinding.button2;
        Button fourHour = detailBinding.button3;
        Button oneHour = detailBinding.button4;
        Button fifteenMin = detailBinding.button5;
        View.OnClickListener clickListener = v -> {
            if (v.getId() == fifteenMin.getId()) {
                loadChart(v, "15", dataItem, oneMonth, oneWeek, oneDay, fourHour, oneHour);
            } else if (v.getId() == oneHour.getId()) {
                loadChart(v, "1H", dataItem, oneMonth, oneWeek, oneDay, fourHour, fifteenMin);
            } else if (v.getId() == fourHour.getId()) {
                loadChart(v, "4H", dataItem, oneMonth, oneWeek, oneDay, oneHour, fifteenMin);
            } else if (v.getId() == oneDay.getId()) {
                loadChart(v, "D", dataItem, oneMonth, oneWeek, fourHour, oneHour, fifteenMin);
            } else if (v.getId() == oneWeek.getId()) {
                loadChart(v, "W", dataItem, oneMonth, oneDay, fourHour, oneHour, fifteenMin);
            } else if (v.getId() == oneMonth.getId()) {
                loadChart(v, "M", dataItem, oneWeek, oneDay, fourHour, oneHour, fifteenMin);
            }
        };
        oneMonth.setOnClickListener(clickListener);
        oneWeek.setOnClickListener(clickListener);
        oneDay.setOnClickListener(clickListener);
        fourHour.setOnClickListener(clickListener);
        oneHour.setOnClickListener(clickListener);
        fifteenMin.setOnClickListener(clickListener);
    }

    public void loadChart(View view, String interval, DataItem dataItem, Button btn, Button btn2, Button btn3, Button btn4, Button btn5) {
        disableAllButton(btn, btn2, btn3, btn4, btn5);
        WebView webView = detailBinding.detaillChartWebView;
        view.setBackgroundResource(R.drawable.active_button);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + dataItem.getSymbol() + "USD&interval=" + interval + "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT");
    }

    private void loadChart(DataItem dataItem) {
        detailBinding.detaillChartWebView.getSettings().setJavaScriptEnabled(true);
        detailBinding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        detailBinding.detaillChartWebView.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + dataItem.getSymbol() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT");
    }

    public void disableAllButton(Button btn, Button btn2, Button btn3, Button btn4, Button btn5) {
        btn.setBackground(null);
        btn2.setBackground(null);
        btn3.setBackground(null);
        btn4.setBackground(null);
        btn5.setBackground(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.smoothBottomBar.setVisibility(View.VISIBLE);
    }
}