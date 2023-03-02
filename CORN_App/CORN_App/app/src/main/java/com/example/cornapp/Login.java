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

                if (gmail.getText().toString().equals("hola")) {
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
                }

            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(Login.this,SignUp.class));
            }
        });
    }

}