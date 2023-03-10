package com.example.cornapp.view.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cornapp.FileUtil;
import com.example.cornapp.Login;
import com.example.cornapp.MainActivity;
import com.example.cornapp.UtilsHTTP;
import com.example.cornapp.databinding.FragmentProfileBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    public static String currentUser=null;
    public static String emailUser=null;
    public static String nameUser=null;
    public static String lastNameUser=null;
    public static String estat="";
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    ActivityResultLauncher<Intent> someActivityResultLauncher2;
    public static int RC_PHOTO_PICKER = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        setupListeners();
        logout();
        openSomeActivityForResult();
        openSomeActivityForResult2();
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            byte[] fileContent1 = data.getByteArrayExtra(Intent.EXTRA_LOCAL_ONLY);
                            File file1 = new File(String.valueOf(uri));
                            File file = null;
                            try {
                                file = FileUtil.from(getContext(),uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //System.out.println("Bytes: "+fileContent.toString());
                            byte[] fileContent = new byte[0];
                                try {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        System.out.println(uri.getPath());
                                        fileContent = Files.readAllBytes(file.toPath());
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            String lletres = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                            // La cadena en donde iremos agregando un carácter aleatorio
                            String name = "";
                            for (int x = 0; x < 30; x++) {
                                int indiceAleatorio = (int) (Math.random()*lletres.length()-1);
                                char caracterAleatorio = lletres.charAt(indiceAleatorio);
                                name += caracterAleatorio;
                            }


                            // Transformar el byte[] en una cadena de text
                            String base64 = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                base64 = Base64.getEncoder().encodeToString(fileContent);
                            }

                            // Preparar l’objecte Json que s’envia al servidor
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject("{}");
                                obj.put("type", "send_id");
                                obj.put("name", name);
                                obj.put("token",Login.sessionToken);
                                obj.put("photo", base64);
                                obj.put("foto","front");

                                // Enviar l’objecte
                                UtilsHTTP.sendPOST("https" + "://" + "corns-production.up.railway.app:" + 443 + "/dades",
                                        obj.toString(), (response) -> {
                                            System.out.println(response);
                                        });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
        someActivityResultLauncher2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            byte[] fileContent1 = data.getByteArrayExtra(Intent.EXTRA_LOCAL_ONLY);
                            File file1 = new File(String.valueOf(uri));
                            File file = null;
                            try {
                                file = FileUtil.from(getContext(),uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //System.out.println("Bytes: "+fileContent.toString());
                            byte[] fileContent = new byte[0];
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    System.out.println(uri.getPath());
                                    fileContent = Files.readAllBytes(file.toPath());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String lletres = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                            // La cadena en donde iremos agregando un carácter aleatorio
                            String name = "";
                            for (int x = 0; x < 30; x++) {
                                int indiceAleatorio = (int) (Math.random()*lletres.length()-1);
                                char caracterAleatorio = lletres.charAt(indiceAleatorio);
                                name += caracterAleatorio;
                            }


                            // Transformar el byte[] en una cadena de text
                            String base64 = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                base64 = Base64.getEncoder().encodeToString(fileContent);
                            }

                            // Preparar l’objecte Json que s’envia al servidor
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject("{}");
                                obj.put("type", "send_id");
                                obj.put("name", name);
                                obj.put("token",Login.sessionToken);
                                obj.put("photo", base64);
                                obj.put("foto","back");

                                // Enviar l’objecte
                                UtilsHTTP.sendPOST("https" + "://" + "corns-production.up.railway.app:" + 443 + "/dades",
                                        obj.toString(), (response) -> {
                                            System.out.println(response);
                                        });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

        binding.profileContactTelfValue.setText(currentUser);
        binding.profileEmailEditValue.setText(emailUser);
        binding.profileUserNameValue.setText(nameUser);
        binding.profileUserSurnameValue.setText(lastNameUser);
        if(estat.equalsIgnoreCase("Acceptat")) {
            String uri = "@drawable/green_circle";  // where myresource (without the extension) is the file

            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());

            Drawable res = getResources().getDrawable(imageResource);
            binding.imageView8.setImageDrawable(res);
            binding.textView4.setText("Usuari Acceptat: L'usuari validat correctament");
        }
        else if(estat.equalsIgnoreCase("NO_VERIFICAT")) {
            String uri = "@drawable/blue_circle";  // where myresource (without the extension) is the file

            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());

            Drawable res = getResources().getDrawable(imageResource);
            binding.imageView8.setImageDrawable(res);
            binding.textView4.setText("Usuari no verificat: Usa els botons per enviar les fotos i que es pugui validar");
        }
        else if(estat.equalsIgnoreCase("PER_VERIFICAR")) {
            String uri = "@drawable/orange_circle";  // where myresource (without the extension) is the file

            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());

            Drawable res = getResources().getDrawable(imageResource);
            binding.imageView8.setImageDrawable(res);
            binding.textView4.setText("Usuari per verificar: Les fotos han sigut enviades i encara s'ha de verificar");
        }
        else if(estat.equalsIgnoreCase("REBUTJAT")) {
            String uri = "@drawable/red_circle";  // where myresource (without the extension) is the file

            int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());

            Drawable res = getResources().getDrawable(imageResource);
            binding.imageView8.setImageDrawable(res);
            binding.textView4.setText("Usuari rebutjat: Les fotos no han sigut bones, aquest usuari ha sigut rebutjat");
        }
        //binding.imageView8.
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
            System.out.println(currentUser);
            JSONObject obj = null;
            try {
                obj = new JSONObject("{}");
                obj.put("type", "change_token");
                obj.put("token", Login.sessionToken);

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
                                currentUser = null;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }


            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Good bye!");
            alertDialog.setMessage("We hope you will be back soon.");

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            startActivity(new Intent(getActivity(),Login.class));
                        }
                    });
            alertDialog.show();

        });
    }
    public void openSomeActivityForResult() {
        //Create Intent
        binding.button2.setOnClickListener(view -> {

                System.out.println("Image "+binding.imageView8.getResources());
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                //Launch activity to get result
                //ImageView img = findViewById(R.id.img);
                someActivityResultLauncher.launch(intent);
        });
    }
    public void openSomeActivityForResult2() {
        //Create Intent
        binding.button3.setOnClickListener(view -> {

            System.out.println("Image "+binding.imageView8.getResources());
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/jpg");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            //Launch activity to get result
            //ImageView img = findViewById(R.id.img);
            someActivityResultLauncher2.launch(intent);
        });
    }
}