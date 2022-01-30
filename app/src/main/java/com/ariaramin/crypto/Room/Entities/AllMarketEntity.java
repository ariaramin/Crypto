package com.ariaramin.crypto.Room.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ariaramin.crypto.Models.AllMarket;

@Entity(tableName = "allMarket_tbl")
public class AllMarketEntity {

    @PrimaryKey
    public long id;

    public AllMarket allMarket;

    public AllMarketEntity(AllMarket allMarket) {
        this.allMarket = allMarket;
    }

    public long getId() {
        return id;
    }

    public AllMarket getAllMarket() {
        return allMarket;
    }
}
