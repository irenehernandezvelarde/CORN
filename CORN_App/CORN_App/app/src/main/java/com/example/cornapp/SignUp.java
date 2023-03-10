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

public class SignUp extends AppCompatActivity {
    public String userToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final EditText gmail = findViewById(R.id.gmailRegistre);
        final EditText telefon = findViewById(R.id.telefonRegistre);
        final EditText nom = findViewById(R.id.nomRegistre);
        final EditText cognom = findViewById(R.id.cognomRegistre);
        final EditText contra = findViewById(R.id.contrasenyaRegistre);
        final EditText repitContra = findViewById(R.id.confirmaContrasenyaRegistre);
        final Button registerButton = findViewById(R.id.btnRegistre);
        TextView btn = findViewById(R.id.alreadyHaveAccount);
        SharedPreferences sharedPref = SignUp.this.getPreferences(Context.MODE_PRIVATE);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject("{}");
                    System.out.println("Mail: "+gmail.getText().toString());
                    obj.put("type", "signup");
                    obj.put("phone", telefon.getText().toString());
                    obj.put("email", gmail.getText().toString());
                    obj.put("name", nom.getText().toString());
                    obj.put("surname", cognom.getText().toString());
                    if(contra.getText().toString().equalsIgnoreCase(repitContra.getText().toString())) {
                        obj.put("password", contra.getText().toString());
                    }

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
                                    userToken = user.getString("token");
                                    if(objResponse.getString("message").equals("created")) {
                                        ProfileFragment.currentUser = String.valueOf(user.get("phone"));
                                        ProfileFragment.emailUser=user.get("email").toString();
                                        ProfileFragment.nameUser=user.get("name").toString();
                                        ProfileFragment.lastNameUser=user.get("surname").toString();
                                        ProfileFragment.estat=user.get("state").toString();
                                        System.out.println("Token: "+user.getString("token"));
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("session_token",user.getString("token"));
                                        editor.apply();
                                        Login.sessionToken=String.valueOf(user.get("token"));
                                        System.out.println("Session token: "+Login.sessionToken);
                                        startActivity(new Intent(SignUp.this, MainActivity.class));
                                    }
                                }
                            }else if(objResponse.getString("status").equals("ERROR")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                builder.setTitle("ERROR");
                                builder.setMessage("Omple els camps correctament!");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }
                                });
                                builder.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                /*if (gmail.getText().toString().equals("hola")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setTitle("Hola!");
                    builder.setMessage("Perfecto");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            startActivity(new Intent(SignUp.this, MainActivity.class));
                        }
                    });
                    builder.show();

                } else {
                    startActivity(new Intent(SignUp.this,Login.class));
                }*/

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this,Login.class));
            }
        });
    }



}