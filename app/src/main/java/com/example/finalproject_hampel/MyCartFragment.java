package com.example.finalproject_hampel;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_hampel.db.AppDatabase;
import com.example.finalproject_hampel.db.Product;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyCartFragment extends DialogFragment {
    private View root;
    private Button btn;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private ArrayList<String> myCartArray = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private Product product;
    private TextView txt1,txt2,txt3;
    private int columnCount = 1;

    public MyCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_my_cart, container, false);

//        txt1 = (TextView)root.findViewById(R.id.)

        btn = (Button)root.findViewById(R.id.btnPay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PayPalActivity.class);
                startActivity(intent);
            }
        });

        getActivity().setTitle("My Cart");
        recyclerView = (RecyclerView)root.findViewById(R.id.rvCart);
      //  cartAdapter.context  = getActivity().getApplicationContext();

        loadArray(getActivity().getApplicationContext());

        for (int i = 0; i < myCartArray.size(); i++) {
            //go to database, GRAB product for id
            final int j = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s = myCartArray.get(j);
                        int pk = Integer.parseInt(s);
                        product = AppDatabase.getInstance(getContext())
                                .productDAO()
                                .getByID(pk);

                        products.add(product);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onResume();
                            }
                        });
                    }
                }).start();

            }

        //     cartAdapter.setProducts(products);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = getContext();

        cartAdapter = new CartAdapter();
        cartAdapter.setProducts(products);

        if(columnCount <= 1){
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        }


        //AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(getContext(), 500);
       // recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(cartAdapter);
        recyclerView.setHasFixedSize(false);
        ViewModelProviders.of(this)
                .get(AllProductsViewModel.class)
                .getProductList(context)
                .observe(this, new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        if(products != null && products.size() > 0){
                            cartAdapter.addItems(products);
                        }
                    }
                });
    }

    public  void loadArray(Context mContext)
    {
        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(mContext);
        if(myCartArray.size() > 0) {
            myCartArray.clear();
        }
        int size = mSharedPreference1.getInt("Product_size", 0);
        if (size != 0) {
            for(int i=0;i<size;i++)
            {
                myCartArray.add(mSharedPreference1.getString("Product_" + i, null));
            }
        }

    }
}
