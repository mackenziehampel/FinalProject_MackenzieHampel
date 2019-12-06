package com.example.finalproject_hampel;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
                    //pull check
//                    Context c = getApplicationContext();
//                    SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(c);
//                    int once = mSharedPreference1.getInt("LoadJSONOnce", 0);
//                    if (once == 0) {
                        selectedFragment = new ProductsFragment();
                        task = new ProductsDataClass();
                        task.setOnProductListComplete(new ProductsDataClass.OnProductListComplete() {
                            @Override
                            public void processProductList(Product[] products) {
                                Log.d("HERE", "processProductList: " + products.length);
                                final ArrayList<Product> productList = new ArrayList<>();

                                for (Product product : products) {

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
//                    }
//                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
//                    SharedPreferences.Editor mEdit1 = sp.edit();
//                    once = 1;
//                    mEdit1.putInt("LoadJSONOnce", once);
//                    mEdit1.commit();

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
