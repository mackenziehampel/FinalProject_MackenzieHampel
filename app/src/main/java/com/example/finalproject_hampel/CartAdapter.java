package com.example.finalproject_hampel;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_hampel.db.Product;

import java.util.ArrayList;
import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Product> products;



    public CartAdapter() {
      //TODO: fix this
    }
    public Context context;

    public void setProducts(ArrayList<Product>products){
        this.products = products;
    }

    public void addItems( List<Product> products) {

        notifyDataSetChanged();
    }

    public void clear() {

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtName, txtDescription, txtPrice;
        public View root;
        public Product product;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;

            txtName = (TextView)root.findViewById(R.id.txtName);
            txtDescription = (TextView)root.findViewById(R.id.txtDescription);
            txtPrice = (TextView)root.findViewById(R.id.txtPrice);


        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //cellforrowat
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //CellForRowAt //DidSelectRowAt
        final Product product = products.get(position);
        if (product != null) {
            holder.product = product;
            holder.txtName.setText(product.getEarth_date());
            holder.txtDescription.setText(product.getId());

            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    //FOR NEXT WEEK DID SELECT ROW AT

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




    @Override
    public int getItemCount() {
        return products.size();
    }
}

