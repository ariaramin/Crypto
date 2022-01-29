package com.ariaramin.crypto.Room.Converters;


import androidx.room.TypeConverter;

import com.ariaramin.crypto.Models.AllMarket;
import com.google.gson.Gson;

public class AllMarketConverter {

    @TypeConverter
    public String toJson(AllMarket allMarket) {
        Gson gson = new Gson();
        return gson.toJson(allMarket);
    }

    @TypeConverter
    public AllMarket toObject(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, AllMarket.class);
    }
}
