package com.example.restservice_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class EditCartActivity extends Activity {


    CardView small, medium, large;

    TextView pizzaname, pizzaprice, pizzadescription, smallpizza, largepizza, mediumpizza, allprice1, itemcount, minplus;

    ImageView pizzaimage;

    Button plus, minus, Update_cart;

    //adding cheese
    CheckBox addcheese;

    //check additional cheese
    Boolean is_checkbox_Checked_or_not = false;

    //Default is set to small size
    int selected_pizza_type;

    //Default quantity size set to one
    int qty = 1;

    String Pizzaname1;
    String PizzaDescription1;
    String Imageurl;
    Double Allprice;

    //To add Dataabse
    String PizzaSize;

    Double pizzaprice1 = 0.00;
    Double pizzaprice2 = 0.00;
    Double pizzaprice3 = 0.00;

    static String URL1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cart);

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
        Update_cart = findViewById(R.id.Update_cart);


        addcheese = findViewById(R.id.addcheese);


        //getting data from homeActivity
        Intent intent = getIntent();
        final String imgurl = intent.getStringExtra("IMG_URL");

        final int Pizza_id = intent.getIntExtra("PIZZA_ID", 1);
        System.out.println(Pizza_id + "**********************************************************");

        final String pizzaname1 = intent.getStringExtra("PIZZA_NAME");
        System.out.println(pizzaname1);

        final String pizzadescription1 = intent.getStringExtra("PIZZA_DESCRIPTION");
        System.out.println(pizzadescription1);

        final String Pizza_size = intent.getStringExtra("PIZZA_SIZE");
        System.out.println(Pizza_size);

        final int item_count = intent.getIntExtra("ITEM_COUNT", 0);

        final Double Total = intent.getDoubleExtra("PRICE", 0.00);
        System.out.println(Total);

        final int Item_id = intent.getIntExtra("ITEM_ID", 0);
        System.out.println(Item_id+"#####################################################333333");

        qty = qty + (item_count - 1);

        itemcount.setText(qty + " Items");
        minplus.setText(qty + "");

        pizzaprice.setText("Rs." + Total);
        allprice1.setText("Rs. " + Total);

        Allprice = pizzaprice1;

        if (Pizza_size.equals("Small")) {
           selected_pizza_type = 1;
            small.setCardBackgroundColor(Color.GRAY);

        } else if (Pizza_size.equals("Medium")) {
            selected_pizza_type = 2;
            medium.setCardBackgroundColor(Color.GRAY);

        } else if (Pizza_size.equals("Large")) {
            selected_pizza_type = 3;
            large.setCardBackgroundColor(Color.GRAY);
        }


        //web url
        URL1 = "http://"+MyIpAddress.MyIpAddress+":8080/demo/findByPizzaId?id=" + Pizza_id;
        System.out.println(URL1);

        LoadPizzaPrices();

        //assign homeActivity data to DetailsActivity's variables
        Imageurl = imgurl;
        PizzaDescription1 = pizzadescription1;
        Pizzaname1 = pizzaname1;

        //setting data to xml file
        Glide.with(EditCartActivity.this).load(imgurl).into(pizzaimage);
        pizzaname.setText(pizzaname1);
        pizzadescription.setText(pizzadescription1);


        // disable when starting add additional cheese to pizza
        if (addcheese.isChecked() == true) {
            addcheese.setChecked(false);
        }

        // adding or not additional cheese to pizza
        addcheese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adding cheese
                if (is_checkbox_Checked_or_not == false) {
                    //is pizza type small
                    if (selected_pizza_type == 1) {
                        Double pizzawithCheese1 = pizzaprice1 + 160;
                        Double pizzawithCheese11 = pizzawithCheese1 * qty;
                        allprice1.setText("Rs." + pizzawithCheese11);
                        pizzaprice.setText("Rs." + pizzawithCheese11);
                        Allprice = pizzawithCheese11;
                        is_checkbox_Checked_or_not = true;

                    } else if (selected_pizza_type == 2) {
                        Double pizzawithCheese2 = pizzaprice2 + 160;
                        Double pizzawithCheese22 = pizzawithCheese2 * qty;
                        allprice1.setText("Rs." + pizzawithCheese22);
                        pizzaprice.setText("Rs." + pizzawithCheese22);
                        Allprice = pizzawithCheese22;
                        is_checkbox_Checked_or_not = true;
                    } else if (selected_pizza_type == 3) {
                        Double pizzawithCheese3 = pizzaprice3 + 160;
                        Double pizzawithCheese33 = pizzawithCheese3 * qty;
                        allprice1.setText("Rs." + pizzawithCheese33);
                        pizzaprice.setText("Rs." + pizzawithCheese33);
                        Allprice = pizzawithCheese33;
                        is_checkbox_Checked_or_not = true;
                    }
                } else {
                    if (selected_pizza_type == 1) {
                        Double pizzaprice11 = pizzaprice1 * qty;
                        allprice1.setText("Rs." + pizzaprice11);
                        pizzaprice.setText("Rs." + pizzaprice11);
                        Allprice = pizzaprice11;
                        System.out.println("ggg");
                        is_checkbox_Checked_or_not = false;

                    } else if (selected_pizza_type == 2) {
                        Double pizzaprice22 = pizzaprice2 * qty;
                        allprice1.setText("Rs." + pizzaprice22);
                        pizzaprice.setText("Rs." + pizzaprice22);
                        Allprice = pizzaprice22;
                        is_checkbox_Checked_or_not = false;

                    } else if (selected_pizza_type == 3) {
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
                System.out.println(selected_pizza_type+"-----------------------------------------------------------------------");
                PizzaSize = "Small";

                //change price according to selection
                allprice1.setText("Rs." + pizzaprice1);
                pizzaprice.setText("Rs." + pizzaprice1);
                Allprice = pizzaprice1;

                //Reset number of items
                qty = 1;
                itemcount.setText(1 + " Items");
                minplus.setText(1 + "");

                // disable when starting add additional cheese to pizza
                if (addcheese.isChecked() == true) {
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
                System.out.println(selected_pizza_type+"-----------------------------------------------------------------------");
                PizzaSize = "Medium";

                //change price according to selection
                allprice1.setText("Rs." + pizzaprice2);
                pizzaprice.setText("Rs." + pizzaprice2);
                Allprice = pizzaprice2;

                //Reset number of items
                qty = 1;
                itemcount.setText(1 + " Items");
                minplus.setText(1 + "");

                // disable when starting add additional cheese to pizza
                if (addcheese.isChecked() == true) {
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
                System.out.println(selected_pizza_type+"-----------------------------------------------------------------------");
                PizzaSize = "Large";


                //change price according to selection
                allprice1.setText("Rs." + pizzaprice3);
                pizzaprice.setText("Rs." + pizzaprice3);
                Allprice = pizzaprice3;

                //Reset number of items
                qty = 1;
                itemcount.setText(1 + " Items");
                minplus.setText(1 + "");

                // disable when starting add additional cheese to pizza
                if (addcheese.isChecked() == true) {
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
                if (qty < 5) {
                    qty++;
                } else {
                    Toast.makeText(EditCartActivity.this, "FIVE Pizzas can be ordered maximally!", Toast.LENGTH_SHORT).show();
                }

                //calculate small pizza price
                if (selected_pizza_type == 1) {

                    //additional cheese check
                    if (is_checkbox_Checked_or_not == true) {
                        Double pizzawithCheese1 = pizzaprice1 + 160;
                        Double pizzawithCheese11 = pizzawithCheese1 * qty;
                        allprice1.setText("Rs." + pizzawithCheese11);
                        pizzaprice.setText("Rs." + pizzawithCheese11);
                        Allprice = pizzawithCheese11;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    } else {
                        Double pizzaprice11 = pizzaprice1 * qty;
                        allprice1.setText("Rs." + pizzaprice11);
                        pizzaprice.setText("Rs." + pizzaprice11);
                        Allprice = pizzaprice11;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }
                } else if (selected_pizza_type == 2) {

                    //additional cheese check
                    if (is_checkbox_Checked_or_not == true) {
                        Double pizzawithCheese2 = pizzaprice2 + 160;
                        Double pizzawithCheese22 = pizzawithCheese2 * qty;
                        allprice1.setText("Rs." + pizzawithCheese22);
                        pizzaprice.setText("Rs." + pizzawithCheese22);
                        Allprice = pizzawithCheese22;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    } else {
                        Double pizzaprice22 = pizzaprice2 * qty;
                        allprice1.setText("Rs." + pizzaprice22);
                        pizzaprice.setText("Rs." + pizzaprice22);
                        Allprice = pizzaprice22;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }
                } else if (selected_pizza_type == 3) {

                    //additional cheese check
                    if (is_checkbox_Checked_or_not == true) {
                        Double pizzawithCheese3 = pizzaprice3 + 160;
                        Double pizzawithCheese33 = pizzawithCheese3 * qty;
                        allprice1.setText("Rs." + pizzawithCheese33);
                        pizzaprice.setText("Rs." + pizzawithCheese33);
                        Allprice = pizzawithCheese33;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    } else {
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
                if (qty > 1) {
                    qty--;
                }

                //calculate small pizza price
                if (selected_pizza_type == 1) {

                    //additional cheese check
                    if (is_checkbox_Checked_or_not == true) {
                        Double pizzawithCheese1 = pizzaprice1 + 160;
                        Double pizzawithCheese11 = pizzawithCheese1 * qty;
                        allprice1.setText("Rs." + pizzawithCheese11);
                        pizzaprice.setText("Rs." + pizzawithCheese11);
                        Allprice = pizzawithCheese11;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    } else {
                        Double pizzaprice11 = pizzaprice1 * qty;
                        allprice1.setText("Rs." + pizzaprice11);
                        pizzaprice.setText("Rs." + pizzaprice11);
                        Allprice = pizzaprice11;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }
                } else if (selected_pizza_type == 2) {

                    //additional cheese check
                    if (is_checkbox_Checked_or_not == true) {
                        Double pizzawithCheese2 = pizzaprice2 + 160;
                        Double pizzawithCheese22 = pizzawithCheese2 * qty;
                        allprice1.setText("Rs." + pizzawithCheese22);
                        pizzaprice.setText("Rs." + pizzawithCheese22);
                        Allprice = pizzawithCheese22;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    } else {
                        Double pizzaprice22 = pizzaprice2 * qty;
                        allprice1.setText("Rs." + pizzaprice22);
                        pizzaprice.setText("Rs." + pizzaprice22);
                        Allprice = pizzaprice22;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    }
                } else if (selected_pizza_type == 3) {

                    //additional cheese check
                    if (is_checkbox_Checked_or_not == true) {
                        Double pizzawithCheese3 = pizzaprice3 + 160;
                        Double pizzawithCheese33 = pizzawithCheese3 * qty;
                        allprice1.setText("Rs." + pizzawithCheese33);
                        pizzaprice.setText("Rs." + pizzawithCheese33);
                        Allprice = pizzawithCheese33;
                        itemcount.setText(qty + " Items");
                        minplus.setText(qty + "");
                    } else {
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


        Update_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              RequestQueue queue1 = Volley.newRequestQueue(EditCartActivity.this);

              String  url1 ="http://"+MyIpAddress.MyIpAddress+":8080/demo/updateCart?id="+Item_id+"&size="+PizzaSize+"&item="+qty+"&total="+Allprice+"&telephone="+null+"&address="+null;

                JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.GET, url1,
                        null, new CartActivity.HTTPResponseListner(), new CartActivity.HTTPErrorListner());
                queue1.add(request1);

                Intent intent1 = new Intent(EditCartActivity.this, CartActivity.class);
                finish();
                startActivity(intent1);
            }
        });

    }


    private void LoadPizzaPrices() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray pizzas  = new JSONArray(response);

                    for (int i =0; i<pizzas.length(); i++){

                        JSONObject pizzasJSONObject  = pizzas.getJSONObject(i);
                        System.out.println("ttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
                        Double price1 = pizzasJSONObject.getDouble("price");
                        pizzaprice1 = price1;
                        System.out.println(pizzaprice1+"11111111111111111111111111111111111111111111111111111");

                        Double price2 = pizzasJSONObject.getDouble("medium_price");
                        pizzaprice2 = price2;
                        System.out.println(pizzaprice2+"11111111111111111111111111111111111111111111111111111");

                        Double price3 = pizzasJSONObject.getDouble("liarge_price");
                        pizzaprice3 = price3;
                        System.out.println(pizzaprice3+"11111111111111111111111111111111111111111111111111111");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EditCartActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditCartActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(EditCartActivity.this).add(stringRequest);

    }

}