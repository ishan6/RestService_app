package com.example.restservice_app;

import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemClickListner, DialogBox_address.DialogAddressListner {

    //Defining Text View Variables for layout
    TextView sub_tot, grand_tot, remove, allprice, qty;

    //Defining Card View Variables
    CardView explore, discount1;

    RadioButton cash,paypal;

    //Defining Button Variables
    Button address, order;

    //Defining String Variables
    String addressline1, addressline2, fulladdress, telephone;

    //Make a object of LoginActivity
    LoginActivity loginActivity = new LoginActivity();

    //Save Pizza id
    int pizza_id;

    //Recycle view item counter
    int item_count;

    //Static variables for clicked item in the recycle view
    static int clicked_item;

    //The Delivery Variable value should be come from database but as a test i assign a static value
    Double delivery = 100.0;

    //This variable saved total price in the recycle view
    Double tot_price = 0.00;

    //
    Double grand_price;
    Double discount;

    /*This URL get data from database according to the user_id and start status
    *cartstatus = 0 means user did not make a payment for the item, items still in the cart
    */
    String URL_DATA = "http://"+MyIpAddress.MyIpAddress+":8080/demo/findByCartIdAndUserId?user_id="+loginActivity.id+"&cartstatus=0";

    //Recycler view
    RecyclerView recyclerView;

    //Adapter of CartAdapter
    CartAdapter adapter;

    //ListView
    List<Cart> cartlist;

    //Volley RequestQueue define as a globle variable this variable value send to Cart Adapter
    static RequestQueue queue1;

    //Goble URL send value to the cart Adapter
    static String url1;

    Boolean cashpay = false;
    Boolean paypalpay = false;

    String PaymentMethod = "";

    String amount;

    public static final int PaypalRequestCode = 7171;

    private  static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PayPal_client_id);


    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cash =(RadioButton) findViewById(R.id.cash);
        paypal =(RadioButton) findViewById(R.id.paypal);

        order = findViewById(R.id.pay);
        address = findViewById(R.id.address);

        qty = findViewById(R.id.qty);


        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        //getting Delivery address
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


            //Loading cart_item layout to the CartActivity
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.cart_item, null);

            remove = view.findViewById(R.id.remove);

            //Initializing the RequestQueue
            queue1 = Volley.newRequestQueue(CartActivity.this);

            //Initializing the URL for getting click item id
            url1 = "http://" + MyIpAddress.MyIpAddress + ":8080/demo/deleteByCartId?id=" + clicked_item;

            //Open HomeActivity to Explore more items
            explore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                    finish();
                    startActivity(intent);
                }
            });

            //This should be comes form database but as a test i build a it as a static Toast Message
            discount1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CartActivity.this, "No Discount or Coupon Available for now!", Toast.LENGTH_SHORT).show();
                }
            });


            cash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cashpay = true;
                    paypalpay = false;
                    PaymentMethod = "Cash";

                    paypal.setChecked(false);
                }
            });

        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashpay = false;
                paypalpay = true;
                PaymentMethod = "PayPal";

                cash.setChecked(false);
            }
        });


            //User going to pay for the cart
            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        //Checking whether user enter the address and telephone
                        if (addressline1 == null) {
                            Toast.makeText(CartActivity.this, "Please Enter Address!", Toast.LENGTH_LONG).show();
                        } else if (telephone.equals("")) {
                            Toast.makeText(CartActivity.this, "Please Enter Telephone!", Toast.LENGTH_LONG).show();
                        } else {

                            if (paypalpay == true) {

                                paymentProcess();
                            }else if(cashpay == true) {

                                UpdateCart();
                            }else if(cashpay == false && paypalpay == false){
                                Toast.makeText(CartActivity.this, "Please Select a payment method!", Toast.LENGTH_LONG).show();
                            }
                        }
                }
            });
    }



    //this function load the cart details of the user
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


    //this override method get clicked item id
    @Override
    public void onItemClick( int position) {

        final Cart clickItem = cartlist.get(position);

        clicked_item = clickItem.getId();

        JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.GET, url1,
                null, new HTTPResponseListner(), new HTTPErrorListner());
        queue1.add(request1);

    }

    //getting Address of the user
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


    //this method is open little popup box to get address and telephone from the users
    public void openDialog(){

            DialogBox_address dialogBox_address = new DialogBox_address();
            dialogBox_address.show(getSupportFragmentManager(),"dialogBox_address");

    }

    //this method is used to notify the user when done the payment
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

    private void paymentProcess() {
        Double USDPrice = tot_price /186.0;
        amount = String.valueOf(USDPrice);

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount), "USD", "Payment",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PaypalRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PaypalRequestCode){
            System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");

            if(resultCode == RESULT_OK){
                System.out.println("OKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKkk");
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if(confirmation != null){
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2");

                    try {
                        String paymentDetails = confirmation.toString();

                        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setSmallIcon(R.drawable.mylogo2)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.mylogo2))
                                .setContentText("Hey")
                                .setContentTitle("Pizza me")
                                .setSubText("Your Order has been Placed Successfully")
                                .setContentText("Your Amount is $"+ amount);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(1,builder.build());

                        UpdateCart();

                        /*
                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                        finish();
                        startActivity(intent);*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else if(requestCode == Activity.RESULT_CANCELED){
                    Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                }
            }else{
                System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHhhh");
            }
        }else if(requestCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }


    public void UpdateCart(){
        //concat addressLine one and two and build full address of the user
        fulladdress = addressline1.concat(addressline2);

        /*
         *Load items to the cart according to the user id and cart status
         *this prevent updating previous payment data of the user
         *cart status = 0 means users did not complete his payment
         */
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

                        /*
                         *Update user telephone, address, and the cartstatus to 1 according to the user id
                         *cartstatus 0 means user did his payment
                         */
                        String url1 = "http://" + MyIpAddress.MyIpAddress + ":8080/demo/updateCart1?id=" + pizza_id + "&telephone=" + telephone + "&address=" + addressline1 + "&cart_status=1&paymentMethod="+PaymentMethod;

                        JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.GET, url1,
                                null, new CartActivity.HTTPResponseListner(), new CartActivity.HTTPErrorListner());
                        queue1.add(request1);

                        // show notification to the user when successfully done his payment
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


        //When user payfor the item it will automatically redirect to homeActivity
        Intent intent1 = new Intent(CartActivity.this, HomeActivity.class);
        finish();
        startActivity(intent1);
    }

}
