package com.example.restservice_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListner {

    private static final String URL_DATA = "http://"+MyIpAddress.MyIpAddress+":8080/demo/all";

    RecyclerView recyclerView;
    ProductAdapter adapter;

    FloatingActionButton floatingActionButton;

    int User_id;
    int Item_count = 0;

    List<Product> productslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        floatingActionButton = findViewById(R.id.cart);

        productslist = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadRecyclerviewData();

        final Intent intent = getIntent();
        User_id = intent.getIntExtra("USER_ID",1);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CartItemCount();

            }
        });


    }


    private void loadRecyclerviewData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data From Server...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray products  = new JSONArray(response);

                    for (int i =0; i<products.length(); i++){

                        JSONObject productobject  = products.getJSONObject(i);

                        int id = productobject.getInt("pizzaId");
                        String name = productobject.getString("name");
                        String description = productobject.getString("description");
                        Double price = productobject.getDouble("price");
                        String imageurl = productobject.getString("imageUrl");
                        Double medium_price = productobject.getDouble("medium_price");
                        Double large_price = productobject.getDouble("liarge_price");

                        Product product = new Product(id, name, description, price, imageurl, medium_price, large_price);
                        productslist.add(product);

                    }

                    adapter = new ProductAdapter(HomeActivity.this, productslist);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemCliclListener(HomeActivity.this);

                    //   progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    //send data to detail activity
    @Override
    public void onItemClick(int position) {
        Intent detailintent = new Intent(this, DetailActivity.class);
        Product clickItem = productslist.get(position);

        detailintent.putExtra("NAME", clickItem.getName());
        detailintent.putExtra("DETAILS", clickItem.getDescription());
        detailintent.putExtra("PRICE", clickItem.getPrice());
        detailintent.putExtra("IMG", clickItem.getImgurl());
        detailintent.putExtra("PIZZA_LARGE", clickItem.getLarge_price());
        detailintent.putExtra("PIZZA_MEDIUM", clickItem.getMedium_price());
        detailintent.putExtra("PIZZA_ID",clickItem.getId());
        detailintent.putExtra("USER_ID", User_id);

        startActivity(detailintent);
    }

    public void CartItemCount(){
        String URL_DATA11 = "http://"+MyIpAddress.MyIpAddress+":8080/demo/findByCartIdAndUserId?user_id="+LoginActivity.id+"&cartstatus=0";
        System.out.println(LoginActivity.id+"Lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA11, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray carts  = new JSONArray(response);

                    for (int i = 0; i<carts.length(); i++){

                        JSONObject cartobject  = carts.getJSONObject(i);

                        int id = cartobject.getInt("id");

                        Item_count = i+1;
                        System.out.println(Item_count+"]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
                        System.out.println(Item_count+"pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp");

                    }

                    if(Item_count > 0 ) {
                        Intent intent1 = new Intent(HomeActivity.this, CartActivity.class);
                        finish();
                        startActivity(intent1);
                    }else{

                        Intent intent1 = new Intent(HomeActivity.this, EmptyActivity.class);
                        finish();
                        startActivity(intent1);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);

    }
}
