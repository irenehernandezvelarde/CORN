package com.example.cornapp.view.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cornapp.UtilsHTTP;
import com.example.cornapp.databinding.FragmentProfileBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    public static String currentUser;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        setupListeners();
        logout();
        return binding.getRoot();
    }

    public void setupListeners(){
        binding.fab.setOnClickListener(view -> {
            JSONObject obj = null;
            try {
                obj = new JSONObject("{}");
                obj.put("type", "sync");
                obj.put("phone", binding.profileContactTelfValue.getText().toString());
                obj.put("email", binding.profileEmailEditValue.getText().toString());
                obj.put("name", binding.profileUserNameValue.getText().toString());
                obj.put("surname",binding.profileUserSurnameValue.getText().toString());

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
                                currentUser=user.getString("phone");
                                System.out.println(user);
                                System.out.println(objResponse.getString("message"));
                                if(objResponse.getString("message").equals("accepted")){

                                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                    alertDialog.setTitle("Hello " + user.getString("name"));
                                    alertDialog.setMessage("You're successfully logged in.\nWe are glad you are here again, enjoy!");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();

                                } else if(objResponse.getString("message").equals("created")){

                                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                    alertDialog.setTitle("Hello " + user.getString("name") + "!");
                                    alertDialog.setMessage("Your user has been created.\n" +
                                            "Welcome to Pairma, your most secure payment app, remember we're just a QR away, enjoy!");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();

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

        });
    }
    public void logout(){
        binding.signOut.setOnClickListener(view -> {
            currentUser = null;
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Good bye!");
            alertDialog.setMessage("We hope you will be back soon.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        });
    }

}