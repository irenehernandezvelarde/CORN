package com.example.cornapp.view.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cornapp.UtilsHTTP;
import com.example.cornapp.databinding.FragmentPaymentBinding;
import com.example.cornapp.view.profile.ProfileFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentFragment extends Fragment {

    private FragmentPaymentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        generateQR();
        return binding.getRoot();

    }
    public void generateQR(){
        binding.button.setOnClickListener(view -> {
            System.out.println(ProfileFragment.currentUser);
            JSONObject obj = null;
            try {
                obj = new JSONObject("{}");
                obj.put("type", "setup_payment");
                obj.put("id_destiny", ProfileFragment.currentUser);
                obj.put("quantity", Double.parseDouble(binding.textInputEditText.getText().toString()));

                UtilsHTTP.sendPOST("http" + "://" + "10.0.2.2:" + 3000 + "/dades", obj.toString(), (response) -> {
                    JSONObject objResponse = null;
                    try {
                        objResponse = new JSONObject(response);
                        System.out.println(response);
                        if (objResponse.getString("status").equals("OK")) {
                            JSONArray JSONlist = objResponse.getJSONArray("result");
                            JSONObject user = null;
                            for (int i = 0; i < JSONlist.length(); i++) {
                                // Get console information
                                user = JSONlist.getJSONObject(i);
                                // Fill template with console information
                                System.out.println(user);
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
}