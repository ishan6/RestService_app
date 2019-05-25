package com.example.restservice_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.restservice_app.Config.MyIpAddress;
import com.example.restservice_app.DataSource.Cart;

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
    static int id;

    String outputString;
    String AES = "AES";

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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if(networkInfo != null && networkInfo.isConnected()) {

                    loadData();
                }else{

                    Snackbar.make(findViewById(R.id.top), "No Internet Connection",Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void loadData() {
        String url ="http://"+ MyIpAddress.MyIpAddress+":8080/demo/findByTelephone?telephone="+telephone.getText().toString();
        System.out.println(MyIpAddress.MyIpAddress+"EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEeee");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray carts = new JSONArray(response);

                    for (int i = 0; i < carts.length(); i++) {

                        JSONObject Userobject = carts.getJSONObject(i);

                         id = Userobject.getInt("user_id");
                         Username1 = Userobject.getString("telephone");
                         Password1 = Userobject.getString("password");

                         System.out.println(id+"00000000000000000000000000000000000000000000000000000");

                            try {
                                outputString = decrypt(Password1, password.getText().toString());

                                if (outputString.equals(password.getText().toString())) {
                                    System.out.println(outputString+"Okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");

                                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                                    progressDialog.setMessage("Validating User....");
                                    progressDialog.show();

                                    Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
                                    intent.putExtra("ID", id);
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, "Username or Password is wrong!", Toast.LENGTH_LONG).show();
                            }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);


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




