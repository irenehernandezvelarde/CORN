package com.example.cornapp.view.payment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.UUID;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cornapp.R;
import com.example.cornapp.UtilsHTTP;
import com.example.cornapp.databinding.FragmentPaymentBinding;
import com.example.cornapp.view.profile.ProfileFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class PaymentFragment extends Fragment {
    public String token;
    private FragmentPaymentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        generateQR();
        return binding.getRoot();

    }
    public void generateQR(){
        binding.button.setOnClickListener(view -> {
            System.out.println("Current user " +ProfileFragment.currentUser);
            JSONObject obj = null;
            try {
                obj = new JSONObject("{}");
                obj.put("type", "setup_payment");
                obj.put("id_destiny", ProfileFragment.currentUser);
                obj.put("quantity", Double.parseDouble(binding.textInputEditText.getText().toString()));

                UtilsHTTP.sendPOST("https" + "://" + "corns-production.up.railway.app:" + 443 + "/dades", obj.toString(), (response) -> {
                    JSONObject objResponse = null;
                    try {
                        objResponse = new JSONObject(response);
                        System.out.println(response);
                        if (objResponse.getString("status").equals("OK")) {
                            JSONArray JSONlist = objResponse.getJSONArray("result");
                            JSONObject user = null;
                            System.out.println("ESTE ES EL IF");
                            for (int i = 0; i < JSONlist.length(); i++) {
                                user = JSONlist.getJSONObject(i);
                                System.out.println(user);
                                token = user.getString("token");
                            }
                            generateQRCode(token);
                        }else{
                            System.out.println("ELSE");
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
    public void generateQRCode(String token) {
        try {
            // Generar el código QR a partir del token usando ZXing
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(token, BarcodeFormat.QR_CODE, 512, 512);
            // Convertir el BitMatrix en un Bitmap para mostrar en el ImageView
            Bitmap qrBitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < 512; x++) {
                for (int y = 0; y < 512; y++) {
                    qrBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            // Mostrar el Bitmap en el ImageView
            ImageView qrImageView = binding.qrImage;
            qrImageView.setImageBitmap(qrBitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}