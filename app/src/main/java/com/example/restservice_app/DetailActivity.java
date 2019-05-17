package com.example.restservice_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    //web url
    final String URL = "http://192.168.42.174:8080/demo/add_to_cart";

    CardView small, medium, large;

    TextView pizzaname, pizzaprice, pizzadescription, smallpizza, largepizza, mediumpizza, allprice1, itemcount, minplus;

    ImageView pizzaimage;

    Button plus, minus, addtoCart;

    //adding cheese
    CheckBox addcheese;

    //check additional cheese
    Boolean is_checkbox_Checked_or_not = false;

    //Default is set to small size
    int selected_pizza_type = 1;

    //Default quantity size set to one
    int qty = 1;

    String Pizzaname1;
    String PizzaDescription1;
    String Imageurl;
    Double Allprice;

    //set User Id to 1;
    int id = 1;

    //To add Dataabse
    String PizzaSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        pizzaname = findViewById(R.id.name);
        smallpizza = findViewById(R.id.smallpizzza);
        mediumpizza = findViewById(R.id.mediumpizzza);
        largepizza = findViewById(R.id.largepizzza);
        pizzadescription = findViewById(R.id.description);
        pizzaprice = findViewById(R.id.price);
        allprice1 = findViewById(R.id.allprice);
        itemcount = findViewById(R.id.qty);
        minplus = findViewById(R.id.minusplus);

        pizzaimage = findViewById(R.id.image);

        small = findViewById(R.id.small);
        medium = findViewById(R.id.medium);
        large = findViewById(R.id.large);

        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        addtoCart = findViewById(R.id.addToCart);


        addcheese = findViewById(R.id.addcheese);

        //Default selection to small pizza
        small.setCardBackgroundColor(Color.GRAY);

        //getting data from homeActivity
        Intent intent = getIntent();
        final String imgurl = intent.getStringExtra("IMG");
        final Double pizzaprice1 = intent.getDoubleExtra("PRICE",0.00);
        final Double pizzaprice3 = intent.getDoubleExtra("PIZZA_LARGE",0.00);
        final Double pizzaprice2 = intent.getDoubleExtra("PIZZA_MEDIUM",0.00);
        final String pizzaname1 = intent.getStringExtra("NAME");
        final String pizzadescription1 = intent.getStringExtra("DETAILS");
        final int Pizza_id = intent.getIntExtra("PIZZA_ID",1);
        final int User_id = intent.getIntExtra("USER_ID",1);

     // System.out.println(User_id+"dddddddddddddddddddddddddddddddddddddddddddddd");
        final Double pizzawithCheese1 = pizzaprice1+160;
        final Double pizzawithCheese2 = pizzaprice2+160;
        final Double pizzawithCheese3 = pizzaprice3+160;

        //assign homeActivity data to DetailsActivity's variables
        Imageurl = imgurl;
        PizzaDescription1 = pizzadescription1;
        Pizzaname1 = pizzaname1;


        //setting data to xml file
        Glide.with(DetailActivity.this).load(imgurl).into(pizzaimage);
        pizzaname.setText(pizzaname1);
        pizzadescription.setText(pizzadescription1);
        pizzaprice.setText("Rs." + pizzaprice1);
        allprice1.setText("Rs. " + pizzaprice1);

        Allprice = pizzaprice1;



        // disable when starting add additional cheese to pizza
        if(addcheese.isChecked() == true){
            addcheese.setChecked(false);
        }

        // adding or not additional cheese to pizza
        addcheese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adding cheese
                if(is_checkbox_Checked_or_not == false) {
                    //is pizza type small
                    if (selected_pizza_type == 1) {
                        Double pizzawithCheese11 = pizzawithCheese1 * qty;
                        allprice1.setText("Rs." + pizzawithCheese11);
                        pizzaprice.setText("Rs." + pizzawithCheese11);
                        Allprice = pizzawithCheese11;
                        is_checkbox_Checked_or_not = true;

                    }else if(selected_pizza_type == 2){
                        Double pizzawithCheese22 = pizzawithCheese2 * qty;
                        allprice1.setText("Rs." + pizzawithCheese22);
                        pizzaprice.setText("Rs." + pizzawithCheese22);
                        Allprice = pizzawithCheese22;
                        is_checkbox_Checked_or_not = true;
                    }else if(selected_pizza_type == 3){
                        Double pizzawithCheese33 = pizzawithCheese3 * qty;
                        allprice1.setText("Rs." + pizzawithCheese33);
                        pizzaprice.setText("Rs." + pizzawithCheese33);
                        Allprice = pizzawithCheese33;
                        is_checkbox_Checked_or_not = true;
                    }
                }else{
                    if(selected_pizza_type == 1) {
                        Double pizzaprice11 = pizzaprice1 * qty;
                        allprice1.setText("Rs." + pizzaprice11);
                        pizzaprice.setText("Rs." + pizzaprice11);
                        Allprice = pizzaprice11;
                        System.out.println("ggg");
                        is_checkbox_Checked_or_not = false;

                    }else if(selected_pizza_type == 2){
                        Double pizzaprice22 = pizzaprice2 * qty;
                        allprice1.setText("Rs." + pizzaprice22);
                        pizzaprice.setText("Rs." + pizzaprice22);
                        Allprice = pizzaprice22;
                        is_checkbox_Checked_or_not = false;

                    }else if(selected_pizza_type == 3){
                        Double pizzaprice33 = pizzaprice3 * qty;
                        allprice1.setText("Rs." + pizzaprice33);
                        pizzaprice.setText("Rs." + pizzaprice33);
                        Allprice = pizzaprice33;
                        is_checkbox_Checked_or_not = false;
                    }
                }
            }
        });

        //select pizza type
        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //color
                small.setCardBackgroundColor(Color.GRAY);
                medium.setCardBackgroundColor(Color.green(1));
                large.setCardBackgroundColor(Color.green(1));

                //set_pizza_type_to_small
                selected_pizza_type = 1;

                //change price according to selection
                allprice1.setText("Rs." + pizzaprice1);
                pizzaprice.setText("Rs." + pizzaprice1);
                Allprice = pizzaprice1;

                //Reset number of items
                qty = 1;
                itemcount.setText(1 + " Items");
                minplus.setText(1 + "");

                // disable when starting add additional cheese to pizza
                if(addcheese.isChecked() == true){
                    addcheese.setChecked(false);
                }
                is_checkbox_Checked_or_not = false;
            }
        });

        //select pizza type
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //color
                medium.setCardBackgroundColor(Color.GRAY);
                small.setCardBackgroundColor(Color.green(1));
                large.setCardBackgroundColor(Color.green(1));

                //set_pizza_type_to_medium
                selected_pizza_type = 2;

                //change price according to selection
                allprice1.setText("Rs." + pizzaprice2);
                pizzaprice.setText("Rs." + pizzaprice2);
                Allprice = pizzaprice2;

                //Reset number of items
                qty = 1;
                itemcount.setText(1 + " Items");
                minplus.setText(1 + "");

                // disable when starting add additional cheese to pizza
                if(addcheese.isChecked() == true){
                    addcheese.setChecked(false);
                }
                is_checkbox_Checked_or_not = false;
            }
        });

        //select pizza type
        large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //color
                medium.setCardBackgroundColor(Color.green(1));
                small.setCardBackgroundColor(Color.green(1));
                large.setCardBackgroundColor(Color.GRAY);

                //set_pizza_type_to_large
                selected_pizza_type = 3;

                //change price according to selection
                allprice1.setText("Rs." + pizzaprice3);
                pizzaprice.setText("Rs." + pizzaprice3);
                Allprice  = pizzaprice3;

                //Reset number of items
                qty = 1;
                itemcount.setText(1 + " Items");
                minplus.setText(1 + "");

                // disable when starting add additional cheese to pizza
                if(addcheese.isChecked() == true){
                    addcheese.setChecked(false);
                }
                is_checkbox_Checked_or_not = false;
            }
        });

        //number of pizza need +
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Maximum five pizza can order
                if(qty < 5) {
                    qty++;
                }else{
                    Toast.makeText(DetailActivity.this, "FIVE Pizzas can be ordered maximally!", Toast.LENGTH_SHORT).show();
                }

                //calculate small pizza price
                if(selected_pizza_type == 1){

                    //additional cheese check
                    if(is_checkbox_Checked_or_not == true){
                        Double pizzawithCheese11 = pizzawithCheese1 * qty;
                        allprice1.setText("Rs." + pizzawithCheese11);
                        pizzaprice.setText("Rs." + pizzawithCheese11);
                        Allprice = pizzawithCheese11;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }else{
                        Double pizzaprice11 = pizzaprice1 * qty;
                        allprice1.setText("Rs." + pizzaprice11);
                        pizzaprice.setText("Rs." + pizzaprice11);
                        Allprice = pizzaprice11;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }
                }else if(selected_pizza_type == 2){

                    //additional cheese check
                    if(is_checkbox_Checked_or_not == true){
                        Double pizzawithCheese22 = pizzawithCheese2 * qty;
                        allprice1.setText("Rs." + pizzawithCheese22);
                        pizzaprice.setText("Rs." + pizzawithCheese22);
                        Allprice = pizzawithCheese22;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }else{
                        Double pizzaprice22 = pizzaprice2 * qty;
                        allprice1.setText("Rs." + pizzaprice22);
                        pizzaprice.setText("Rs." + pizzaprice22);
                        Allprice = pizzaprice22;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }
                }else if(selected_pizza_type == 3){

                    //additional cheese check
                    if(is_checkbox_Checked_or_not == true){
                        Double pizzawithCheese33 = pizzawithCheese3 * qty;
                        allprice1.setText("Rs." + pizzawithCheese33);
                        pizzaprice.setText("Rs." + pizzawithCheese33);
                        Allprice = pizzawithCheese33;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }else{
                        Double pizzaprice33 = pizzaprice3 * qty;
                        allprice1.setText("Rs." + pizzaprice33);
                        pizzaprice.setText("Rs." + pizzaprice33);
                        Allprice = pizzaprice33;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }
                }

            }
        });

        // number of pizza need -
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //should select minimum one pizza
                if(qty > 1){
                    qty--;
                }

                //calculate small pizza price
                if(selected_pizza_type == 1){

                    //additional cheese check
                    if(is_checkbox_Checked_or_not == true){
                        Double pizzawithCheese11 = pizzawithCheese1 * qty;
                        allprice1.setText("Rs." + pizzawithCheese11);
                        pizzaprice.setText("Rs." + pizzawithCheese11);
                        Allprice = pizzawithCheese11;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }else{
                        Double pizzaprice11 = pizzaprice1 * qty;
                        allprice1.setText("Rs." + pizzaprice11);
                        pizzaprice.setText("Rs." + pizzaprice11);
                        Allprice = pizzaprice11;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }
                }else if(selected_pizza_type == 2){

                    //additional cheese check
                    if(is_checkbox_Checked_or_not == true){
                        Double pizzawithCheese22 = pizzawithCheese2 * qty;
                        allprice1.setText("Rs." + pizzawithCheese22);
                        pizzaprice.setText("Rs." + pizzawithCheese22);
                        Allprice = pizzawithCheese22;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }else{
                        Double pizzaprice22 = pizzaprice2 * qty;
                        allprice1.setText("Rs." + pizzaprice22);
                        pizzaprice.setText("Rs." + pizzaprice22);
                        Allprice = pizzaprice22;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }
                }else if(selected_pizza_type == 3){

                    //additional cheese check
                    if(is_checkbox_Checked_or_not == true){
                        Double pizzawithCheese33 = pizzawithCheese3 * qty;
                        allprice1.setText("Rs." + pizzawithCheese33);
                        pizzaprice.setText("Rs." + pizzawithCheese33);
                        Allprice = pizzawithCheese33;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }else{
                        Double pizzaprice33 = pizzaprice3 * qty;
                        allprice1.setText("Rs." + pizzaprice33);
                        pizzaprice.setText("Rs." + pizzaprice33);
                        Allprice = pizzaprice33;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }
                }
            }
        });

        addtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject js1 = new JSONObject();

                //Setting up Pizza Sizes before add to database;
                if( selected_pizza_type == 1 ){
                    PizzaSize = "Small";
                }else if( selected_pizza_type == 2){
                    PizzaSize = "Medium";
                }else if( selected_pizza_type == 3){
                    PizzaSize = "Large";
                }
                try {
                 //   System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa+"+User_id);
                    js1.put("userid", User_id);
                    js1.put("pizza_id", Pizza_id);
                    js1.put("pizzaname", Pizzaname1);
                    js1.put("size", PizzaSize);
                    js1.put("description", pizzadescription1);
                    js1.put("item", qty);
                    js1.put("total", Allprice);
                    js1.put("img_url", Imageurl);
                    // Cart status one still buy the item
                    js1.put("cartstatus",0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Make request for JSONObject
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST, URL, js1,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //   Log.d(TAG, response.toString() + " i am queen");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  VolleyLog.d(TAG, "Error: " + error.getMessage());
                        error.printStackTrace();
                    }
                }) {

                    /**
                     * Passing some request headers
                     */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }

                };

                // Adding request to request queue
                Volley.newRequestQueue(DetailActivity.this).add(jsonObjReq);

                Intent cartdetails = new Intent(DetailActivity.this, CartActivity.class);
                startActivity(cartdetails);
            }
        });
    }
}

