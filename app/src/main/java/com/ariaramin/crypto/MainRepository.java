package com.ariaramin.crypto;

import android.util.Log;

import com.ariaramin.crypto.Models.AllMarket;
import com.ariaramin.crypto.Retrofit.RequestApi;
import com.ariaramin.crypto.Room.DatabaseDao;
import com.ariaramin.crypto.Room.Entities.AllMarketEntity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainRepository {

    RequestApi requestApi;
    DatabaseDao databaseDao;
    CompositeDisposable compositeDisposable;

    public MainRepository(RequestApi requestApi, DatabaseDao databaseDao) {
        this.requestApi = requestApi;
        this.databaseDao = databaseDao;
        compositeDisposable = new CompositeDisposable();
    }

    public Observable<AllMarket> getAllMarketList() {
        return requestApi.getMarketList();
    }

    public void insertAllMarket(AllMarket allMarket) {
        Completable.fromAction(() -> databaseDao.insert(new AllMarketEntity(allMarket)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("insertAllMarket", "Completed");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public Flowable<AllMarketEntity> getAllMarketEntity() {
        return databaseDao.getAllMarket();
    }
}
