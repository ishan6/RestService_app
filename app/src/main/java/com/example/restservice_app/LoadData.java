package com.example.restservice_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoadData extends AppCompatActivity {

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);

        requestQueue = Volley.newRequestQueue(this);

        LoadPizzaPrices();
    }


    private void LoadPizzaPrices() {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAaaa");
        String URL1 = "http://192.168.42.137:8080/demo/findByPizzaId?id=" + 3;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
                try {
                    Double price1 = response.getDouble("price");
                 //   x = price1;
                    System.out.println(price1 + "11111111111111111111111111111111111111111111111111111");

                    Double price2 = response.getDouble("medium_price");
                 //   pizzaprice2 = price2;
                    System.out.println(price2 + "11111111111111111111111111111111111111111111111111111");

                    Double price3 = response.getDouble("liarge_price");
                 //   pizzaprice3 = price3;
                    System.out.println(price3 + "11111111111111111111111111111111111111111111111111111");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
