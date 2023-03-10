package com.example.cornapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cornapp.view.profile.ProfileFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    public static String sessionToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button loginButton = findViewById(R.id.btnLogin);
        final EditText gmail = findViewById(R.id.gmailIniciSessio);
        final EditText contra = findViewById(R.id.confirmaContrasenyaRegistre);
        TextView registerButton = findViewById(R.id.textViewSignUp);

        SharedPreferences sharedPref = Login.this.getPreferences(Context.MODE_PRIVATE);
        System.out.println("Preferences: "+sharedPref.getAll().get("session_token"));
            JSONObject obj = null;
            try {
                obj = new JSONObject("{}");
                obj.put("type", "get_profile");
                obj.put("token", sharedPref.getAll().get("session_token"));

                UtilsHTTP.sendPOST("https" + "://" + "corns-production.up.railway.app:" + 443 + "/dades", obj.toString(), (response) -> {
                    JSONObject objResponse = null;
                    try {
                        objResponse = new JSONObject(response);
                        System.out.println(response);
                        if (objResponse.getString("status").equals("OK")) {
                            JSONArray JSONlist = objResponse.getJSONArray("result");
                            System.out.println(JSONlist);
                            JSONObject user = null;
                            for (int i = 0; i < JSONlist.length(); i++) {
                                user = JSONlist.getJSONObject(i);
                                System.out.println(user);
                                ProfileFragment.currentUser = String.valueOf(user.get("phone"));
                                System.out.println("Token guradado "+sharedPref.getAll().get("session_token"));
                                System.out.println("Token database "+user.get("token"));
                                if(sharedPref.getAll().get("session_token").equals(user.get("token"))){
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    ProfileFragment.emailUser=user.get("email").toString();
                                    ProfileFragment.nameUser=user.get("name").toString();
                                    ProfileFragment.lastNameUser=user.get("surname").toString();
                                    sessionToken=String.valueOf(user.get("token"));
                                    System.out.println("Session token: "+sessionToken);
                                    startActivity(intent);
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
                                        ProfileFragment.currentUser = String.valueOf(user.get("phone"));
                                        ProfileFragment.emailUser=user.get("email").toString();
                                        ProfileFragment.nameUser=user.get("name").toString();
                                        ProfileFragment.lastNameUser=user.get("surname").toString();
                                        ProfileFragment.estat=user.get("state").toString();
                                        startActivity(new Intent(Login.this, MainActivity.class));
                                        System.out.println("Token: "+user.getString("token"));
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("session_token",user.getString("token"));
                                        sessionToken=String.valueOf(user.get("token"));
                                        editor.apply();
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