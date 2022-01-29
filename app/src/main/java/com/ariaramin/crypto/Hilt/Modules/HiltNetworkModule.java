package com.ariaramin.crypto.Hilt.Modules;

import com.ariaramin.crypto.MainRepository;
import com.ariaramin.crypto.Retrofit.RequestApi;
import com.ariaramin.crypto.Room.DatabaseDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class HiltNetworkModule {

    @Provides
    @Singleton
    Retrofit ProvideRetrofit() {
        String BASE_URL = "https://api.coinmarketcap.com/data-api/v3/";
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    RequestApi ProvideRequestApi(Retrofit retrofit) {
        return retrofit.create(RequestApi.class);
    }

    @Provides
    @Singleton
    MainRepository ProvideMainRepository(RequestApi requestApi, DatabaseDao databaseDao) {
        return new MainRepository(requestApi, databaseDao);
    }
}
