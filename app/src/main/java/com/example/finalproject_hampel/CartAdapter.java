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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_hampel.db.Product;

import java.util.ArrayList;
import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Product> products;
    private ArrayList<String> myCartArray = new ArrayList<>(); //keys
    private float total = 0;

    public CartAdapter() {
      //TODO: fix this
    }
    public Context context;

    public void setProducts(ArrayList<Product>products){
        this.products = products;
    }

    public void addItems( List<Product> products) {
        this.myCartArray.clear();
        loadArray(context);
        notifyDataSetChanged();
    }

    public void clear() {

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtName, txtDescription, txtPrice, txtTotal;
        public View root;
        public Product product;
        public Button btnDelete;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;

            txtName = (TextView)root.findViewById(R.id.txtName);
            txtDescription = (TextView)root.findViewById(R.id.txtDescription);
            txtPrice = (TextView)root.findViewById(R.id.txtPrice);
            btnDelete = (Button)root.findViewById(R.id.btnDelete);


        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //cellforrowat
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        total = 0;
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //CellForRowAt //DidSelectRowAt

        final Product product = products.get(position);
        if (product != null) {
            holder.product = product;
            holder.txtName.setText("Rover: " + product.getRover_name());
            holder.txtDescription.setText("Lot id: "+product.getId());
            double p = Double.parseDouble(product.getPrice());
            holder.txtPrice.setText("$"+ String.format("%,.2f", p));



            holder.btnDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    /// button click event
                    //loadArray(context);
                    addItems(products);
                    for(int i = 0; i < myCartArray.size(); i++){
                        String id = myCartArray.get(i);
                        int idINT = Integer.parseInt(id);
                        int positionINT = holder.getAdapterPosition();
                        int holderID = product.getP_id();
                        if(idINT == holderID ){
                            myCartArray.remove(i);
                            saveArray();

                            break;
                        }
                        int price = Integer.parseInt(holder.product.getPrice());

                        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(context);
                        float fTotal = mSharedPreference1.getFloat("CartTotal", 0);
                        fTotal -= (float)Integer.parseInt(product.getPrice());

                        //throwback in
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor mEdit1 = sp.edit();
                        mEdit1.putFloat("CartTotal", fTotal);
                        mEdit1.commit();
                        notifyDataSetChanged();
                    }
                }
            });

            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    //DID SELECT ROW AT

                    Bundle bundle = new Bundle();
                   // bundle.putInt("product_PK", product.getP_id());


                    ProductDetails prodDeets = new ProductDetails();
                    prodDeets.setArguments(bundle);
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .add(android.R.id.content, prodDeets)
                            .addToBackStack(null)
                            .commit();



                }

            });

        }
    }


    public boolean saveArray()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
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

    @Override
    public int getItemCount() {
        return products.size();
    }
}

