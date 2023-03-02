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

public class SignUp extends AppCompatActivity {

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
                if (gmail.getText().toString().equals("hola")) {
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
                }

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