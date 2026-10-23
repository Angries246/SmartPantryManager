package com.example.smartpantrymanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.smartpantrymanager.model.PantryItem;

import java.util.List;

@Dao
public interface PantryDao {

    @Insert
    void insert(PantryItem item);

    @Update
    void update(PantryItem item);

    @Delete
    void delete(PantryItem item);

    @Query("SELECT * FROM pantry_items ORDER BY expiryDate ASC")
    List<PantryItem> getAllItems();

    @Query("SELECT * FROM pantry_items WHERE expiryDate < :currentTime ORDER BY expiryDate ASC")
    List<PantryItem> getExpiredItems(long currentTime);

    @Query("SELECT * FROM pantry_items WHERE expiryDate BETWEEN :currentTime AND :threeDaysLater ORDER BY expiryDate ASC")
    List<PantryItem> getExpiringSoonItems(long currentTime, long threeDaysLater);

    @Query("SELECT * FROM pantry_items WHERE name LIKE '%' || :search || '%' ORDER BY expiryDate ASC")
    List<PantryItem> searchItems(String search);

    @Query("DELETE FROM pantry_items")
    void deleteAll();
}
