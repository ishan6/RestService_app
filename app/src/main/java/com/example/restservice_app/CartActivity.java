package com.example.restservice_app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

   //User Id
    int id = 1;

    private List<Cart> Cartdetails = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //URL and Request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.42.211:8080/demo/findByCartId?user_id="+id;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null, new HTTPResponseListner(), new HTTPErrorListner());
        queue.add(request);
    }

    //Response Listener
    class HTTPResponseListner implements Response.Listener<JSONArray>{
        @Override
        public void onResponse(JSONArray jsonArray) {
            for(int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject object= jsonArray.getJSONObject(i);
                    Cart cart = new Cart();
                    cart.setPizzaname(object.get("pizzaname").toString());
                    cart.setSize(object.get("size").toString());
                    cart.setDescription(object.get("description").toString());
                    cart.setItem(Integer.parseInt(object.get("item").toString()));
                    cart.setTotal(Double.parseDouble(object.get("total").toString()));
                    cart.setImg_url(object.get("img_url").toString());
                    Cartdetails.add(cart);

                    ListView cartlist1 = findViewById(R.id.listView);
                    CustomAdapter listAdapter = new CustomAdapter(getApplicationContext(), R.layout.cart_item, Cartdetails);
                    cartlist1.setAdapter(listAdapter);


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

    private class CustomAdapter extends ArrayAdapter<Cart> {
        private List<Cart> cartList;

        CustomAdapter(Context context, int resource, List<Cart> items) {
            super(context, resource, items);
            cartList = items;
        }

        //Load data to xml file
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().from(getContext()).inflate(R.layout.cart_item, parent, false);
            }
            Cart item = cartList.get(position);

            TextView name =  convertView.findViewById(R.id.name);
            TextView total =  convertView.findViewById(R.id.price);
            TextView item1 =  convertView.findViewById(R.id.item);
            TextView size1 =  convertView.findViewById(R.id.size);
            TextView description =  convertView.findViewById(R.id.description);

            ImageView image = convertView.findViewById(R.id.image);

         // Picasso.get().load(item.getImageURL()).into(iv);
            name.setText(item.getPizzaname());
            total.setText("Rs."+Double.toString(item.getTotal()));
            item1.setText(Integer.toString(item.getItem())+" Item");
            size1.setText(item.getSize());
            description.setText(item.getDescription());
            Glide.with(CartActivity.this).load(item.getImg_url()).into(image);

            return convertView;
        }
    }
}
