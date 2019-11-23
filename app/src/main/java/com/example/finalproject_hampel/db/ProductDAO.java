package com.example.finalproject_hampel.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Dao

public interface ProductDAO {

    @Insert
    void insert(Product product);

    @Query("select * from Product")
    LiveData<List<Product>> getAll();


    @Query("Select * from Product where p_id =:p_id ")
    Product getByID(int p_id);

    @Delete
    void delete(Product course);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertProducts(ArrayList<Product> products);



}
