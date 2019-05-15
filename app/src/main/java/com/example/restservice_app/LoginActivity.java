package com.example.restservice_app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class LoginActivity extends AppCompatActivity {

    EditText telephone, password;
    Button login;

    TextView text_signup,forgetpass;

    String Username1;
    String Password1;

    String outputString;
    String AES = "AES";

    int Log_id;


    private List<Cart> Cartdetails = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        telephone = findViewById(R.id.telephone);
        password = findViewById(R.id.password);

        login = findViewById(R.id.btn_login);

        text_signup  = findViewById(R.id.txt_signup);
        forgetpass = findViewById(R.id.txt_forgetpass);

        //url and Request Queue
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.42.131:8080/demo/showusers";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null, new LoginActivity.HTTPResponseListner(), new LoginActivity.HTTPErrorListner());
        queue.add(request);

        text_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPassActivity.class);
                startActivity(intent);
            }
        });


    }

        class HTTPResponseListner implements Response.Listener<JSONArray> {
            @Override
            public void onResponse(final JSONArray jsonArray) {

                        login.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        //getting data from database
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        final User user = new User();
                                        user.setTelephone(object.get("telephone").toString());
                                        user.setPassword(object.get("password").toString());
                                        user.setUser_id(object.getInt(("user_id")));

                                        //asign data to globle variable
                                        Username1 = user.getTelephone();
                                        Password1 = user.getPassword();

                                        //   System.out.println(Username1);
                                        //    System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkk"+Password1);

                                        try {
                                            //decrypt password
                                            outputString = decrypt(Password1, password.getText().toString());
                                            System.out.println(password.getText().toString() + "0000000000000000000000000000");
                                            System.out.println(outputString);
                                            System.out.println(password);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        System.out.println(outputString + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                                        //    System.out.println(telephone+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                                        System.out.println(Password1 + "fffffffffffffffffffffffffffffffffffffffffffffffffff");

                                        //decrypt password null check
                                        System.out.println(outputString);
                                        if (outputString == null) {

                                            Toast.makeText(LoginActivity.this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();

                                        } else {

                                            //Checking username and password
                                            if ((Username1.equals(telephone.getText().toString())) && outputString.equals(password.getText().toString())) {

                                                Log_id = user.getUser_id();

                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                intent.putExtra("USER_ID", Log_id);
                                                startActivity(intent);
                                                break;
                                            } else {


                                                Toast.makeText(LoginActivity.this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }

                        });

        }




        //AES decryption
    private String decrypt(String outputString, String password)throws Exception{
        SecretKeySpec Key = generateKey(password);
        Cipher bdside = Cipher.getInstance(AES);
        bdside.init(Cipher.DECRYPT_MODE,Key);
        byte[] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decVal = bdside.doFinal(decodedValue);
        String decryptedValue = new String(decVal);
        return decryptedValue;
    }

    //AES Generate KEY
    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] Key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(Key, "AES");
        return secretKeySpec;
    }
}


    class HTTPErrorListner implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
        }
    }

    }
