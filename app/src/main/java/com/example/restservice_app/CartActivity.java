package com.example.restservice_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

    TextView sub_tot, grand_tot, remove;

    CardView explore, discount1;

    int id = 1;
    static int clicked_item;

    Double delivery = 100.0;
    Double tot_price = 0.00;
    Double grand_price;
    Double discount;

    String URL_DATA = "http://192.168.42.191:8080/demo/findByCartId?user_id="+id;

    RecyclerView recyclerView;
    CartAdapter adapter;

    List<Cart> cartlist;

   static RequestQueue queue1;
   static String url1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sub_tot = findViewById(R.id.txt_tot);
        grand_tot = findViewById(R.id.txt_grandtot);

        explore = findViewById(R.id.card2);
        discount1 = findViewById(R.id.card3);

        cartlist = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadRecyclerviewData();

       // CheckCartEmptiness();

          //  Intent intent = getIntent();
          //  User_id = intent.getIntExtra("USER_ID",1);
          //  System.out.println(User_id +"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.cart_item, null);

        remove = view.findViewById(R.id.remove);

        queue1 = Volley.newRequestQueue(CartActivity.this);

        url1 ="http://192.168.42.191:8080/demo/deleteByCartId?id="+clicked_item;

        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        discount1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CartActivity.this, "No Discount or Coupon Available for now!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void loadRecyclerviewData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray carts  = new JSONArray(response);

                    for (int i =0; i<carts.length(); i++){

                        JSONObject cartobject  = carts.getJSONObject(i);

                        int id = cartobject.getInt("id");
                   //   int user_id = cartobject.getInt("user_id");
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




    @Override
    public void onItemClick( int position) {

        final Cart clickItem = cartlist.get(position);

        clicked_item = clickItem.getId();

        JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.GET, url1,
                null, new HTTPResponseListner(), new HTTPErrorListner());
        queue1.add(request1);

    }


    static class HTTPResponseListner implements Response.Listener<JSONArray> {
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

    static class HTTPErrorListner implements Response.ErrorListener {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
    }



}
