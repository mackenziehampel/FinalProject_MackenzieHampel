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


public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private List<Product> products;
    private ArrayList<String> myAccountArray = new ArrayList<>(); //keys
    private float total = 0;

    public AccountAdapter() {
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

            txtName = (TextView)root.findViewById(R.id.txtNameAccount);
            txtDescription = (TextView)root.findViewById(R.id.txtDescriptionAccount);
            txtPrice = (TextView)root.findViewById(R.id.txtPriceAccount);


        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //cellforrowat
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_myaccount, parent, false);
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


            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    //DID SELECT ROW AT

                    Bundle bundle = new Bundle();
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

