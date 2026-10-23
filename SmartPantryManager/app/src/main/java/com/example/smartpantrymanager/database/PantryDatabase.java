package com.example.smartpantrymanager.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.smartpantrymanager.model.PantryItem;

@Database(entities = {PantryItem.class}, version = 1, exportSchema = false)
public abstract class PantryDatabase extends RoomDatabase {

    public abstract PantryDao pantryDao();

    private static PantryDatabase instance;

    public static synchronized PantryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            PantryDatabase.class, "pantry_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
