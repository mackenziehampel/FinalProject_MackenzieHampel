package com.example.finalproject_hampel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accounts.Account;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.finalproject_hampel.db.AppDatabase;
import com.example.finalproject_hampel.db.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//TAB VIEW
    ArrayList<Product> products;
    private ProductsDataClass task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.menu_home:
                    Log.d("", "onNavigationItemSelected: 1 ");
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.menu_products:
                    selectedFragment = new ProductsFragment();
                    task = new ProductsDataClass();
                    task.setOnProductListComplete(new ProductsDataClass.OnProductListComplete() {
                        @Override
                        public void processProductList(Product[] products) {
                            Log.d("HERE", "processProductList: " + products.length);
                            final ArrayList<Product> productList = new ArrayList<>();

                            for(Product product: products){

                                productList.add(product);

                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    AppDatabase.getInstance(getApplicationContext())
                                            .productDAO()
                                            .insertProducts(productList);
                                }
                            }).start();

                        }
                    });

                    task.execute("");


                   // /---------------------

//                RecyclerView rvProducts = (RecyclerView)findViewById(R.id.rvProducts);
//                //create list  products = getLIST()
//                  ProductsAdapter adapter = new ProductsAdapter(products); //put products in adapter
//               rvProducts.setAdapter(adapter);
//               rvProducts.setLayoutManager(new LinearLayoutManager(this));
//                //pass this list to the products view

                    //--------------------

                    break;
                case R.id.menu_myCart:
                    selectedFragment = new MyCartFragment();
                    break;
                case R.id.menu_account:
                    selectedFragment = new AccountFragment();
                    break;


            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment)
                    .commit();

            return true;
        }
    };

}
