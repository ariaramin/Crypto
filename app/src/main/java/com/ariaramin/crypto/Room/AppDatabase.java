package com.ariaramin.crypto.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ariaramin.crypto.Room.Converters.AllMarketConverter;
import com.ariaramin.crypto.Room.Entities.AllMarketEntity;

@TypeConverters({AllMarketConverter.class})
@Database(entities = {AllMarketEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DatabaseDao databaseDao();
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "crypto_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
