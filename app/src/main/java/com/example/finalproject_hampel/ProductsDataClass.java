package com.example.finalproject_hampel;

import android.os.AsyncTask;
import android.util.Log;

import com.example.finalproject_hampel.db.Product;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

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

        try{//https://weber.instructure.com/api/v1/courses

            URL url = new URL("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=0&api_key=pU2sXjt6TjfNMRpwLFBBDwJxLMZP0SxWIPf301LN");  //pU2sXjt6TjfNMRpwLFBBDwJxLMZP0SxWIPf301LN//https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=DEMO_KEY
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
         //   connection.setRequestProperty("Authorization", "Bearer " + "14~sHRHy5YEDftmRL8elIdXNUMoSEB0SZNMP1c2e23TOo7Bxgfqvt28a18k73V5mUJd");

            connection.connect();

            int status = connection.getResponseCode();
            Log.d(TAG, "doInBackground: STATUS" + status);
            switch (status){
                case 200:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    rawJSON = br.readLine();
                    if(rawJSON == null) {
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
            JsonObject slide = (JsonObject)i.next();

            JsonPrimitive a = slide.getAsJsonPrimitive("img_src");
            JsonElement imageURL = slide.get("img_src");
            JsonElement earthDate = slide.get("earth_date");
            JsonElement id = slide.get("id");
            JsonElement rover = slide.get("rover");

            //TODO: get name of rover?  Rover Returns {"id":5,"name":"Curiosity","landing_date":"2012-08-06","launch_date":"2011-11-26","status":"active","max_sol":2540,"max_date":"2019-09-28","total_photos":366206,"cameras":[{"name":"FHAZ","full_name":"Front Hazard Avoidance Camera"},{"name":"NAVCAM","full_name":"Navigation Camera"},{"name":"MAST","full_name":"Mast Camera"},{"name":"CHEMCAM","full_name":"Chemistry and Camera Complex"},{"name":"MAHLI","full_name":"Mars Hand Lens Imager"},{"name":"MARDI","full_name":"Mars Descent Imager"},{"name":"RHAZ","full_name":"Rear Hazard Avoidance Camera"}]}
           //Probably will have to pop that open and look around

            Log.d(TAG, "parseJson: PAUSE");
            JsonObject rov = (JsonObject)rover;
            JsonElement name2 = rov.get("name");
            int priceInt = (getRandomPriceInRange(20, 70000000));
            int acresInt = getRandomPriceInRange(1, 50);
            String pricePerSqFt = Double.toString(priceInt / (acresInt * 43560));
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

                if(count == 17){

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
