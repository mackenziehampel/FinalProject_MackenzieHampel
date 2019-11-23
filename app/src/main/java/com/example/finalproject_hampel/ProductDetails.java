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
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.finalproject_hampel.db.AppDatabase;
import com.example.finalproject_hampel.db.Product;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetails extends Fragment {

    private View root;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button addToMyCart;
    private ArrayList<String> myCartArray; //keys
    private Product product;
    private int product_pk;
    private TextView txt1, txt2, txt3, txt4, txt5;

    public ProductDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_product_details, container, false);
        txt1 = (TextView)root.findViewById(R.id.txt1);
        txt2 = (TextView)root.findViewById(R.id.txt2);
        txt3 = (TextView)root.findViewById(R.id.txt3);
        txt4 = (TextView)root.findViewById(R.id.txt4);
        txt5 = (TextView)root.findViewById(R.id.txt5);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            product_pk = bundle.getInt("product_PK");
//            Log.d(TAG, "onCreateView: ");
            getActivity().setTitle("Lot Details");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    product = AppDatabase.getInstance(getContext())
                            .productDAO()
                            .getByID(product_pk);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt1.setText("Lot Id#: " + product.getId());
                            txt2.setText("Date Listed: "+product.getEarth_date());
                            int price = getRnadomPriceInRange(20, 70000000);

                            txt3.setText("Listed Price: $" + price + "USD");
                            int acres = getRnadomPriceInRange(1, 50);
                            txt4.setText("Acres: "+ acres);
                            double pricePerSqFt = (price / (acres * 43560));
                            txt5.setText("Price/Square Foot: $" + pricePerSqFt);
                            //price per squareft
                        }
                    });

                }
            }).start();



        }


        addToMyCart = (Button)root.findViewById(R.id.btnAddToCart);
        addToMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //add to my cart array if item doesn't exist.
            }
        });

        Toolbar tb = (Toolbar)root.findViewById(R.id.toolbarDetails);
        tb.setTitle("Mars Lot Details");
        
        ((AppCompatActivity)getActivity()).setSupportActionBar(tb);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if(actionBar != null) {

            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }


        return root;
    }

    private static int getRnadomPriceInRange(int min, int max){

        Random r = new Random();
        return r.nextInt((max-min) + 1)+min;

    }

    public boolean saveArray()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor mEdit1 = sp.edit();
        /* sKey is an array */
        mEdit1.putInt("Product_size", myCartArray.size());

        for(int i=0;i<myCartArray.size();i++)
        {
            mEdit1.remove("Product_" + i);
            mEdit1.putString("Product_" + i, myCartArray.get(i));
        }

        return mEdit1.commit();
    }

    public  void loadArray(Context mContext)
    {
        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(mContext);
        myCartArray.clear();
        int size = mSharedPreference1.getInt("Product_size", 0);

        for(int i=0;i<size;i++)
        {
            myCartArray.add(mSharedPreference1.getString("Product_" + i, null));
        }

    }

}


///eURI(Uri.parse("https://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/00000/opgs/edr/fcam/FLA_397504830EDR_F0010000AUT_04096M_.JPG"));
