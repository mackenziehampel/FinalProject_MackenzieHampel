package com.example.finalproject_hampel;

import android.os.AsyncTask;
import android.util.Log;

import com.example.finalproject_hampel.db.Product;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class ProductsDataClass extends AsyncTask<String, Double, String>{
    private String rawJSON;
    private OnProductListComplete mCallBack;

    public interface OnProductListComplete {
        void processProductList(Product[] products);
    }

    public void setOnProductListComplete(OnProductListComplete listener){
        mCallBack = listener;
    }


    @Override
    protected String doInBackground(String... strings) {

        try{
                URL url = new URL("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=55&api_key=pU2sXjt6TjfNMRpwLFBBDwJxLMZP0SxWIPf301LN");  //_key=DEMO_KEY
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                connection.connect();

                int status = connection.getResponseCode();
                Log.d(TAG, "doInBackground: STATUS" + status);
                switch (status) {
                    case 200:
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        rawJSON = br.readLine();
                        if (rawJSON == null) {
                            Log.d(TAG, "doInBackground: " + rawJSON.toString());
                        }
                        break;
                }

        } catch (Exception e){
            Log.d(TAG, "doInBackground: " + e.toString());


        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(rawJSON);
        Product[] products;
        try{

            products = parseJson(rawJSON);
            mCallBack.processProductList(products);

        }catch (Exception e){
            Log.d(TAG, "onPostExecute: " + e.toString());
        }
    }

    private Product[] parseJson(String result) {
        result = cutResult(result);

        Product[] products = null;
        ArrayList<Product> arrayList = new ArrayList<Product>();
        JsonObject jsonObject = (JsonObject)new JsonParser().parse(result);
        JsonArray jsonArray = jsonObject.getAsJsonArray("photos");
        Iterator i =  jsonArray.iterator();

        while(i.hasNext()){
            JsonObject obj = (JsonObject)i.next();

            JsonElement imageURL = obj.get("img_src");
            JsonElement earthDate = obj.get("earth_date");
            JsonElement id = obj.get("id");
            JsonElement rover = obj.get("rover");

            //nested Json
            JsonObject rov = (JsonObject)rover;
            JsonElement name2 = rov.get("name");
            int priceInt = (getRandomPriceInRange(20, 70000));
            int acresInt = getRandomPriceInRange(1, 20);
            int l = acresInt * 43560;
            double rr = (double)priceInt / l;
            String pricePerSqFt = rr + "";
            String price = Integer.toString(priceInt);
            String acres = Integer.toString(acresInt);

            Product tempProd = new Product(id.toString(),imageURL.toString(),earthDate.toString(),
                    name2.toString(),price,acres,pricePerSqFt);

            arrayList.add(tempProd);
        }
        Log.d(TAG, "parseJson: "+ arrayList);
        products = arrayList.toArray(new Product[arrayList.size()]);
        return products;

    }

    private static int getRandomPriceInRange(int min, int max){

        Random r = new Random();
        return r.nextInt((max-min) + 1)+min;

    }

    private String cutResult(String result) {

        int count = 0;
        String newRes = new String();
        for (int i=0; i < result.length(); i++)
        {
            newRes += result.charAt(i);
            if (result.charAt(i) == '}' && result.charAt(i + 1) == ']' &&
            result.charAt(i+2) == '}' && result.charAt(i+3)=='}' && result.charAt(i+4)==',')
            {
                count++;

                if(count == 12){

                    if(newRes.endsWith(","))
                    {
                        newRes = newRes.substring(0,newRes.length() - 1);
                    }
                    newRes += "]}}]}";
                    return newRes;
                }
            }
        }


        return "";
    }

}
