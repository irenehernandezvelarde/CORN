package com.example.cornapp.view.scan;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.cornapp.R;
import com.example.cornapp.UtilsHTTP;
import com.example.cornapp.databinding.FragmentProfileBinding;
import com.example.cornapp.databinding.FragmentScanBinding;
import com.example.cornapp.view.profile.ProfileFragment;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScanFragment extends Fragment {
    private CodeScanner mCodeScanner;
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    private FragmentScanBinding binding;


    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                getActivity(),
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentScanBinding.inflate(inflater, container, false);
        mCodeScanner = new CodeScanner(getActivity(), binding.scannerView);

        requestPermission();
        if (hasCameraPermission()) {
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    getActivity().runOnUiThread(
                            () -> {
                                Toast.makeText(getActivity(), result.getText(), Toast.LENGTH_SHORT).show();
                                System.out.println("Current user " + ProfileFragment.currentUser);
                                JSONObject obj = null;
                                try {
                                    obj = new JSONObject("{}");
                                    obj.put("type", "setup_payment");
                                    obj.put("transationToken", result.getText());
                                    obj.put("user_id", result.getText());


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
                                                    System.out.println(user);
                                                }
                                            }else{
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            
                    );

                }

            });
            binding.scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
        }
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}


