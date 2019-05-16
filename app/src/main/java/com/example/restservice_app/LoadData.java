package com.example.restservice_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoadData extends AppCompatActivity {

    Button button;

    RequestQueue requestQueue;

    String URL_DATA = "http://192.168.42.137:8080/demo/findByPizzaId?id="+2;

    Double price11=0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);

        button = findViewById(R.id.button);

        requestQueue = Volley.newRequestQueue(this);

        LoadPizzaPrices();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(price11+"zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
            }
        });


    }


    private void LoadPizzaPrices() {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAaaa");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray carts = new JSONArray(response);

                    for (int i = 0; i < carts.length(); i++) {

                        JSONObject cartobject = carts.getJSONObject(i);

                        Double price1 = cartobject.getDouble("price");
                        System.out.println(price1+"jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
                        price11 = price11+price1;

                        System.out.println(price11+"ppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp");

                    }


                    //   progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoadData.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoadData.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
