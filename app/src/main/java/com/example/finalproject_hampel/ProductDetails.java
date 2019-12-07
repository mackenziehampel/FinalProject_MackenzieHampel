package com.example.finalproject_hampel;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.finalproject_hampel.db.AppDatabase;
import com.example.finalproject_hampel.db.Product;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetails extends Fragment {

    private View root;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button addToMyCart;
    private ArrayList<String> myCartArray = new ArrayList<>(); //keys
    private Product product;
    private int product_pk;
    private TextView txt1, txt2, txt3, txt4, txt5, txt6;
    private ImageView imgView;


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
        txt6 = (TextView)root.findViewById(R.id.txt6);
        imgView = (ImageView) root.findViewById(R.id.img_main);

        //get my cart array
        loadArray(getActivity().getApplicationContext());

        Log.d(TAG, "onCreateView: " + myCartArray);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            product_pk = bundle.getInt("product_PK");
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
                            txt3.setText("Listed Price: $" + product.getPrice() + " USD");
                            txt4.setText("Acres: "+ product.getAcres());
                            txt5.setText("Price/Square Foot: $" + product.getPricePerSqFt());
                            txt6.setText("Photographed by: " + product.getRover_name() + " rover");
                            String imgUrl = product.getImg_src();
                            String url = imgUrl.replaceAll("^\"|\"$", "");
                            new DownloadImageTask(imgView).execute(url);

                        }
                    });

                }
            }).start();

        }


        addToMyCart = (Button)root.findViewById(R.id.btnAddToCart);
        addToMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //pull out and manipulate
                Context context = getContext();
                SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(context);
                float fTotal = mSharedPreference1.getFloat("CartTotal", 0);
                fTotal += (float)Integer.parseInt(product.getPrice());

                //throwback in
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor mEdit1 = sp.edit();
                mEdit1.putFloat("CartTotal", fTotal);
                mEdit1.commit();

                //add to my cart array if item doesn't exist.
                String prodID =  Integer.toString(product.getP_id());
                if(!Arrays.asList(myCartArray).contains(prodID)){
                    myCartArray.add(prodID);
                    saveArray();
                    //display dialog saying added to cart successful

                    Toast.makeText(getActivity(), "Succesfully added to your cart!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "This lot was already in your cart!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        Toolbar tb = (Toolbar)root.findViewById(R.id.toolbarDetails);
        tb.setTitle("Mars Lot Details");
        tb.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                getActivity().onBackPressed();

            }

        });


        return root;
    }

    //TODO:check if has image if not set default img
    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return hasImage;
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
        if( myCartArray.size() > 0) {

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


///eURI(Uri.parse("https://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/00000/opgs/edr/fcam/FLA_397504830EDR_F0010000AUT_04096M_.JPG"));
