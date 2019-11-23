package com.example.finalproject_hampel;


import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_hampel.db.Product;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {

    private View root;
    private RecyclerView recyclerView;
    private ProductsAdapter productsAdapter;
    private int columnCount = 1;



    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_products, container, false);

        getActivity().setTitle("Product");
        recyclerView = (RecyclerView)root.findViewById(R.id.rvProducts);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = getContext();

        productsAdapter = new ProductsAdapter(new ArrayList<Product>());

        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(getContext(), 500);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(productsAdapter);
        recyclerView.setHasFixedSize(false);
        ViewModelProviders.of(this)
                .get(AllProductsViewModel.class)
                .getProductList(context)
                .observe(this, new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        if(products != null && products.size() > 0){
                            productsAdapter.addItems(products);
                        }
                    }
                });
    }
}
