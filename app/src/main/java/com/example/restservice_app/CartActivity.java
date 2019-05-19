package com.example.restservice_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemClickListner, DialogBox_address.DialogAddressListner {

    TextView sub_tot, grand_tot, remove, allprice, qty;

    CardView explore, discount1;

    Button address, order;
    String addressline1, addressline2, fulladdress, telephone;

    LoginActivity loginActivity = new LoginActivity();

    int pizza_id;
    int item_count;

    static int clicked_item;

    Double delivery = 100.0;
    Double tot_price = 0.00;
    Double grand_price;
    Double discount;

    String URL_DATA = "http://"+MyIpAddress.MyIpAddress+":8080/demo/findByCartIdAndUserId?user_id="+loginActivity.id+"&cartstatus=0";

    RecyclerView recyclerView;
    CartAdapter adapter;

    List<Cart> cartlist;

   static RequestQueue queue1;
   static String url1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        order = findViewById(R.id.pay);
        address = findViewById(R.id.address);


            qty = findViewById(R.id.qty);

            address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog();
                }
            });

            sub_tot = findViewById(R.id.txt_tot);
            grand_tot = findViewById(R.id.txt_grandtot);

            explore = findViewById(R.id.card2);
            discount1 = findViewById(R.id.card3);

            cartlist = new ArrayList<>();

            recyclerView = findViewById(R.id.recyclerview);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            allprice = findViewById(R.id.allprice);

            loadRecyclerviewData();

            // CheckCartEmptiness();

            //  Intent intent = getIntent();
            //  User_id = intent.getIntExtra("USER_ID",1);
            //  System.out.println(User_id +"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");

            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.cart_item, null);

            remove = view.findViewById(R.id.remove);

            queue1 = Volley.newRequestQueue(CartActivity.this);

            url1 = "http://" + MyIpAddress.MyIpAddress + ":8080/demo/deleteByCartId?id=" + clicked_item;

            explore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                    finish();
                    startActivity(intent);
                }
            });

            discount1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CartActivity.this, "No Discount or Coupon Available for now!", Toast.LENGTH_SHORT).show();
                }
            });


            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (addressline1 == null) {
                        Toast.makeText(CartActivity.this, "Please Enter Address!", Toast.LENGTH_LONG).show();
                    } else if (telephone.equals("")) {
                        Toast.makeText(CartActivity.this, "Please Enter Telephone!", Toast.LENGTH_LONG).show();
                    } else {

                        fulladdress = addressline1.concat(addressline2);

                        String url2 = "http://" + MyIpAddress.MyIpAddress + ":8080/demo/findByCartIdAndUserId?user_id=" + loginActivity.id + "&cartstatus=0";

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONArray orders = new JSONArray(response);

                                    for (int i = 0; i < orders.length(); i++) {

                                        JSONObject order = orders.getJSONObject(i);

                                        int id = order.getInt("id");
                                        pizza_id = id;
                                        System.out.println(pizza_id + "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%555");


                                        RequestQueue queue1 = Volley.newRequestQueue(CartActivity.this);

                                        String url1 = "http://" + MyIpAddress.MyIpAddress + ":8080/demo/updateCart1?id=" + pizza_id + "&telephone=" + telephone + "&address=" + addressline1 + "&cart_status=1";

                                        JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.GET, url1,
                                                null, new CartActivity.HTTPResponseListner(), new CartActivity.HTTPErrorListner());
                                        queue1.add(request1);

                                        notifyme();

                                    }


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

                        Volley.newRequestQueue(CartActivity.this).add(stringRequest);


                        Intent intent1 = new Intent(CartActivity.this, HomeActivity.class);
                        finish();
                        startActivity(intent1);
                    }

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
                        item_count = i;

                        Cart cart = new Cart(id, 1, pizza_id, name, size, description, item, total, img_url, cart_status);
                        cartlist.add(cart);

                    }

                    sub_tot.setText("Rs." + tot_price);
                    tot_price = tot_price + delivery;
                    grand_tot.setText("Rs." + tot_price);
                    allprice.setText("Rs." + tot_price );
                    qty.setText(item_count+1 + " Item");


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

    @Override
    public void applyText(String address1, String address2, String telephone1) {
        if (address1.equals("")) {
            Toast.makeText(CartActivity.this, "Address is Empty!", Toast.LENGTH_LONG).show();

        } else if(telephone1.equals("")){
            Toast.makeText(CartActivity.this, "Mobile Number is Empty!", Toast.LENGTH_LONG).show();

        }else if(telephone1.length() != 10) {
            Toast.makeText(CartActivity.this, "Invalid Mobile Number!", Toast.LENGTH_LONG).show();

        }else{
            addressline1 = address1;
            System.out.println(addressline1 + "0000000000000000000000000000000000000000000000000000");

            addressline2 = address2;
            System.out.println(addressline2 + "2222222222222222222222222222222222222222222222222222");

            telephone = telephone1;
            System.out.println(telephone + "2222222222222222222222222222222222222222222222222222");
        }
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


    public void openDialog(){

            DialogBox_address dialogBox_address = new DialogBox_address();
            dialogBox_address.show(getSupportFragmentManager(),"dialogBox_address");

    }

    public void notifyme(){
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.mylogo2)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.mylogo2))
                .setContentText("Hey")
                .setContentTitle("Pizza me")
                .setContentText("Your Order has been Placed Successfully");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }
/*
    public void CartItemCount(){
        String URL_DATA11 = "http://"+MyIpAddress.MyIpAddress+":8080/demo/findByCartIdAndUserId?user_id="+LoginActivity.id+"&cartstatus=0";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA11, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray carts  = new JSONArray(response);

                    for (int i =0; i<carts.length(); i++){

                        JSONObject cartobject  = carts.getJSONObject(i);

                        int id = cartobject.getInt("id");

                        Item_count = i;
                        HomeActivity.Item_count = Item_count;
                        System.out.println(Item_count+"pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp");

                    }


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

*/
}
