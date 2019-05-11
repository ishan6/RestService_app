package com.example.restservice_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SignUpActivity extends AppCompatActivity {

    Button btn_signUp;
    EditText username, password, email, nic, confpass, telephone;

    TextView login;

    final String URL = "http://192.168.42.211:8080/demo/register";

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String nicPatte = "[0-9-]+[a-zA-z]+";

    String newusername, newpassword, newemail, newnic, newtelephone, newconfpass;

    String AES = "AES";

    String encript;

    String checktelephone, checkemail, checknic;

    int databaseroundcount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
/*
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.42.108:8080/demo/showusers";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null, new SignUpActivity.HTTPResponseListner(), new SignUpActivity.HTTPErrorListner());
        queue.add(request);

*/
        btn_signUp = findViewById(R.id.btn_sign_up);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        telephone = findViewById(R.id.telephone);
        nic = findViewById(R.id.nic);
        confpass = findViewById(R.id.confpass);

        login = findViewById(R.id.txt_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });



        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Optional Parameters to pass as POST request
                getdata();

                if (newusername.isEmpty() == true) {

                    Toast.makeText(getApplicationContext(),"Username is Empty!",Toast.LENGTH_SHORT).show();
                } else if(newtelephone.isEmpty() == true){

                    Toast.makeText(getApplicationContext(),"Mobile Number is Empty!",Toast.LENGTH_SHORT).show();
                }else if(newtelephone.length() != 10){

                    Toast.makeText(getApplicationContext(),"Invalid Mobile Number!",Toast.LENGTH_SHORT).show();
                }else if(newemail.isEmpty() == true) {

                    Toast.makeText(getApplicationContext(),"Email Address is Empty!",Toast.LENGTH_SHORT).show();
                }else if(!(newemail.matches(emailPattern))){

                    Toast.makeText(getApplicationContext(),"Invalid Email Address!",Toast.LENGTH_SHORT).show();
                }else if(newnic.isEmpty() == true) {

                    Toast.makeText(getApplicationContext(),"NIC is Empty!",Toast.LENGTH_SHORT).show();
                }else if(newnic.length() != 10){

                    Toast.makeText(getApplicationContext(),"Invalid NIC!",Toast.LENGTH_SHORT).show();
                }else if(!(newnic.matches(nicPatte))){

                    Toast.makeText(getApplicationContext(),"Invalid NIC!",Toast.LENGTH_SHORT).show();
                }else if(newpassword.isEmpty() == true){

                    Toast.makeText(getApplicationContext(),"Password is Empty!",Toast.LENGTH_SHORT).show();
                }else if(newconfpass.isEmpty() == true){

                    Toast.makeText(getApplicationContext(),"Password Confirm is Empty!",Toast.LENGTH_SHORT).show();
                }else if(!(newconfpass.equals(newpassword))){

                    Toast.makeText(getApplicationContext(),"Passwords are not Equal!",Toast.LENGTH_SHORT).show();
                }else{
                    JSONObject js = new JSONObject();

                    try {
                        encript = encrypt(newpassword,newpassword);
                        System.out.println("encrypt my key1111111111111111111111111111111111111"+encript);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    try {
                        js.put("name", newusername);
                        js.put("telephone", newtelephone);
                        js.put("email", newemail);
                        js.put("nic", newnic);
                        js.put("password", encript);
                        //Default user is active when registered moment
                        js.put("user_status",1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Make request for JSONObject
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                            Request.Method.POST, URL, js,
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


                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            return headers;
                        }

                    };

                    // Adding request to request queue
                    Volley.newRequestQueue(SignUpActivity.this).add(jsonObjReq);

                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
            }
        });


    }

    //getting data and asign it to globle variables
    public void getdata(){
        String username1 = username.getText().toString();
        newusername = username1;

        String emmail1 = email.getText().toString();
        newemail = emmail1;

        String telephone1 = telephone.getText().toString();
        newtelephone = telephone1;

        String nic1 = nic.getText().toString();
        newnic = nic1;

        String password1 = password.getText().toString();
        newpassword = password1;

        String confpass1 = confpass.getText().toString();
        newconfpass = confpass1;

    }

    private String encrypt(String Data, String password) throws Exception{
        SecretKeySpec Key = generateKey(password);
        Cipher bdside = Cipher.getInstance(AES);
        bdside.init(Cipher.ENCRYPT_MODE,Key);
        byte[] encVal = bdside.doFinal(Data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] Key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(Key, "AES");
        return secretKeySpec;
    }




}
