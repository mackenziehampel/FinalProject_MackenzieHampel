package com.example.finalproject_hampel;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
//see what you have previously purchased
    //TAB
    private View root;
    private Button btn;
    private TextView txtTotal;
    private RecyclerView recyclerView;
    private AccountAdapter accountAdapter;
    private ArrayList<String> myAccountArray = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private Product product;
    private TextView txt1,txt2,txt3;
    private int columnCount = 1;


    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_account, container, false);

        getActivity().setTitle("My Account");
        recyclerView = (RecyclerView)root.findViewById(R.id.rvAccount);

        loadArray(getActivity().getApplicationContext());

        for (int i = 0; i < myAccountArray.size(); i++) {
            //go to database, GRAB product for id
            final int j = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String s = myAccountArray.get(j);
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

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = getContext();

        accountAdapter = new AccountAdapter();
        accountAdapter.setProducts(products);
        accountAdapter.context = context;

        if(columnCount <= 1){
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        }

        recyclerView.setAdapter(accountAdapter);
        recyclerView.setHasFixedSize(false);
        ViewModelProviders.of(this)
                .get(AllProductsViewModel.class)
                .getProductList(context)
                .observe(this, new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        if(products != null && products.size() > 0){
                            accountAdapter.addItems(products);
                        }
                    }
                });
    }

    public void loadArray(Context mContext)
    {
        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(mContext);
        if(myAccountArray.size() > 0) {
            myAccountArray.clear();
        }
        int size = mSharedPreference1.getInt("MyAccountProducts_size", 0);
        if (size != 0) {
            for(int i=0;i<size;i++)
            {
                myAccountArray.add(mSharedPreference1.getString("MyAccountProducts_" + i, null));
            }
        }

    }

}
