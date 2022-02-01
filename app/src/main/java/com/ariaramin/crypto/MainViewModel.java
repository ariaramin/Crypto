package com.ariaramin.crypto;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ariaramin.crypto.Models.AllMarket;
import com.ariaramin.crypto.Room.Entities.AllMarketEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

@HiltViewModel
public class MainViewModel extends AndroidViewModel {

    MutableLiveData<ArrayList<Integer>> sliderData = new MutableLiveData<>();

    @Inject
    MainRepository mainRepository;

    @Inject
    public MainViewModel(@NonNull Application application) {
        super(application);
        getViewPagerData();
    }

    void getViewPagerData() {
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.banner);
        images.add(R.drawable.banner2);
        images.add(R.drawable.banner3);
        images.add(R.drawable.banner4);
        images.add(R.drawable.banner5);
        sliderData.postValue(images);
    }

    public MutableLiveData<ArrayList<Integer>> getSliderData() {
        return sliderData;
    }

    public Observable<AllMarket> getAllMarket(){
        return mainRepository.getAllMarketList();
    }

    public void insertAllMarket(AllMarket allMarket){
        mainRepository.insertAllMarket(allMarket);
    }

    public Flowable<AllMarketEntity> getAllMarketEntity() {
        return mainRepository.getAllMarketEntity();
    }

}
