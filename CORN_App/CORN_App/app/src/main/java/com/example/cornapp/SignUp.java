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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject("{}");
                    obj.put("type", "signup");
                    obj.put("phone", telefon.getText().toString());
                    obj.put("email", gmail.getText().toString());
                    obj.put("name", nom.getText().toString());
                    obj.put("surname", cognom.getText().toString());
                    if(contra.getText().toString().equalsIgnoreCase(repitContra.getText().toString())) {
                        obj.put("password", contra.getText().toString());
                    }

                    UtilsHTTP.sendPOST("http" + "://" + "localhost:" + 3000 + "/dades", obj.toString(), (response) -> {
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
                                        startActivity(new Intent(SignUp.this, MainActivity.class));
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