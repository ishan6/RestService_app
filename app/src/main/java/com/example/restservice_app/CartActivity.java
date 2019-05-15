package com.example.restservice_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemClickListner {

    TextView sub_tot, grand_tot;

    int id = 1;
    int clicked_item;

    Double delivery = 100.0;
    Double tot_price = 0.00;
    Double grand_price;
    Double discount;

    String URL_DATA = "http://192.168.42.59:8080/demo/findByCartId?user_id="+id;

    RecyclerView recyclerView;
    CartAdapter adapter;

    List<Cart> cartlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sub_tot = findViewById(R.id.txt_tot);
        grand_tot = findViewById(R.id.txt_grandtot);

        cartlist = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadRecyclerviewData();

      //  Intent intent = getIntent();
      //  User_id = intent.getIntExtra("USER_ID",1);
        //  System.out.println(User_id +"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");

    }


    private void loadRecyclerviewData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray carts  = new JSONArray(response);

                    for (int i =0; i<carts.length(); i++){

                        JSONObject cartobject  = carts.getJSONObject(i);

                        int id = cartobject.getInt("id");
                   //     int user_id = cartobject.getInt("user_id");
                        int pizza_id = cartobject.getInt("pizza_id");
                        String name = cartobject.getString("pizzaname");
                        String size = cartobject.getString("size");
                        String description = cartobject.getString("description");
                        int item = cartobject.getInt("item");
                        Double total = cartobject.getDouble("total");
                        String img_url = cartobject.getString("img_url");
                        int cart_status = cartobject.getInt("cart_status");

                        tot_price = tot_price + total;
                    //    System.out.println(tot_price+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");


                        Cart cart = new Cart(id, 1, pizza_id, name, size, description, item, total, img_url, cart_status);
                        cartlist.add(cart);

                    }

                    sub_tot.setText("Rs." + tot_price);
                    tot_price = tot_price + delivery;
                    grand_tot.setText("Rs." + tot_price);


                    adapter = new CartAdapter(CartActivity.this, cartlist);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemCliclListener(CartActivity.this);

                    //   progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CartActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    //send data to detail activity
    @Override
    public void onItemClick(int position) {

        Cart clickItem = cartlist.get(position);

    //    System.out.println(clickItem.getId()+"sssssssssssssssssssssssssssssssssssssssssssssss ");

       clicked_item = clickItem.getId();
    //    System.out.println(clicked_item+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        RequestQueue queue1 = Volley.newRequestQueue(this);
        String url1 ="http://192.168.42.59:8080/demo/deleteByCartId?id="+clicked_item;

        JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.GET, url1,
                null, new HTTPResponseListner(), new HTTPErrorListner());
        queue1.add(request1);

        Intent intent = new Intent(CartActivity.this, CartActivity.class);
        startActivity(intent);


    }


    class HTTPResponseListner implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray jsonArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

            class HTTPErrorListner implements Response.ErrorListener {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }
}
