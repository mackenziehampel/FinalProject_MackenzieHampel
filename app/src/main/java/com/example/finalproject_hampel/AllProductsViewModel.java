package com.example.finalproject_hampel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalproject_hampel.db.AppDatabase;
import com.example.finalproject_hampel.db.Product;

import java.util.List;

public class AllProductsViewModel extends ViewModel {

    private LiveData<List<Product>> productList;

    public LiveData<List<Product>> getProductList(Context c){

        if (productList != null){
            return productList;
        } else {
            return productList = AppDatabase.getInstance(c).productDAO().getAll();
        }

    }
}
