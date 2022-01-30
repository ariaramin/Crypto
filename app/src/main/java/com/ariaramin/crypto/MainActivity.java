package com.ariaramin.crypto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.ariaramin.crypto.Models.AllMarket;
import com.ariaramin.crypto.databinding.ActivityMainBinding;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    NavController navController;
    NavHostFragment navHostFragment;
    public DrawerLayout drawerLayout;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    @Inject
    ConnectivityManager connectivityManager;
    @Inject
    NetworkRequest networkRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        drawerLayout = activityMainBinding.drawerLayout;
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();

        setupNavigationComponent();
        checkNetworkConnection();
    }

    private void checkNetworkConnection() {
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@androidx.annotation.NonNull Network network) {
                super.onAvailable(network);
                callApiRequest();
            }

            @Override
            public void onLost(@androidx.annotation.NonNull Network network) {
                super.onLost(network);
                Toast.makeText(getApplicationContext(), "Check your network connection.", Toast.LENGTH_SHORT).show();
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
        }
    }

    private void callApiRequest() {
        Observable.interval(5, TimeUnit.SECONDS)
                .flatMap(n -> mainViewModel.getAllMarket())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AllMarket>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull AllMarket allMarket) {
                        // Get data from API and insert into database
                        mainViewModel.insertAllMarket(allMarket);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setupNavigationComponent() {
        // Get nav host and nav controller
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(activityMainBinding.navigationView, navController);

        setupSmoothBottomMenu();
    }

    private void setupSmoothBottomMenu() {
        PopupMenu popupMenu = new PopupMenu(this, null);
        popupMenu.inflate(R.menu.bottom_nav_menu);
        Menu menu = popupMenu.getMenu();
        activityMainBinding.bottomBar.setupWithNavController(menu, navController);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}