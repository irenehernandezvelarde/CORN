package com.example.cornapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button loginButton = findViewById(R.id.btnLogin);
        final EditText gmail = findViewById(R.id.gmailIniciSessio);
        final EditText contra = findViewById(R.id.confirmaContrasenyaRegistre);
        TextView registerButton = findViewById(R.id.textViewSignUp);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject("{}");
                    obj.put("type", "login");
                    obj.put("email", gmail.getText().toString());
                    obj.put("password", contra.getText().toString());

                    UtilsHTTP.sendPOST("https" + "://" + "corns-production.up.railway.app:" + 443 + "/dades", obj.toString(), (response) -> {
                        JSONObject objResponse = null;
                        try {
                            objResponse = new JSONObject(response);
                            System.out.println(response);
                            if (objResponse.getString("status").equals("OK")) {
                                JSONArray JSONlist = objResponse.getJSONArray("result");
                                JSONObject user = null;
                                for (int i = 0; i < JSONlist.length(); i++) {
                                    user = JSONlist.getJSONObject(i);
                                    if(objResponse.getString("message").equals("accepted")) {
                                        startActivity(new Intent(Login.this, MainActivity.class));
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*if (gmail.getText().toString().equals("hola")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setTitle("Hola!");
                    builder.setMessage("Perfecto");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            startActivity(new Intent(Login.this, MainActivity.class));
                        }
                    });
                    builder.show();

                } else {
                    startActivity(new Intent(Login.this,SignUp.class));
                }*/

            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(Login.this,SignUp.class));
            }
        });
    }

}