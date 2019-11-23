package com.example.finalproject_hampel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_hampel.db.Product;

import java.io.InputStream;
import java.util.List;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private final List<Product> products;


    public ProductsAdapter(List<Product> products) {
        this.products = products;
    }


    public void addItems( List<Product> products) {

        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();

    }

    public void clear() {
        this.products.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtName, txtDescription, txtPrice;
        public ImageView img;
        public View root;
        public Product product;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;

            txtName = (TextView)root.findViewById(R.id.txtName);
            txtDescription = (TextView)root.findViewById(R.id.txtDescription);
            txtPrice = (TextView)root.findViewById(R.id.txtPrice);
            img = (ImageView)root.findViewById(R.id.imgLeft);


        }

    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //cellforrowat
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
//        Context context = parent.getContext();
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View productView = inflater.inflate(R.layout.item_product, parent, false);
//        ViewHolder viewHolder = new ViewHolder(productView);
//        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {
        //CellForRowAt //DidSelectRowAt
        final Product product = products.get(position);
        if (product != null) {
            holder.product = product;
            holder.txtName.setText(product.getEarth_date());
            holder.txtDescription.setText(product.getId());
            String imgUrl = product.getImg_src();
            String url = imgUrl.replaceAll("^\"|\"$", "");
            new DownloadImageTask(holder.img).execute(url);

//            String imageUrl = "https://via.placeholder.com/500";  //getImg_src
//
//            //Loading image using Picasso
//            Picasso.get().load(imageUrl).into(imageView);


            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    //FOR NEXT WEEK DID SELECT ROW AT

                    Bundle bundle = new Bundle();
                    bundle.putInt("product_PK", product.getP_id());


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


class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}