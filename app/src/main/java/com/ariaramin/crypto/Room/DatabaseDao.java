package com.ariaramin.crypto.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ariaramin.crypto.Room.Entities.AllMarketEntity;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AllMarketEntity allMarketEntity);

    @Query("SELECT * FROM allMarket_tbl")
    Flowable<AllMarketEntity> getAllMarket();
}
