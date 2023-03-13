package com.example.cornapp.view.historial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cornapp.Login;
import com.example.cornapp.UtilsHTTP;
import com.example.cornapp.databinding.FragmentHistorialBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistorialFragment extends Fragment {

    private FragmentHistorialBinding binding;
    private ListView listview;
    private ArrayList<String> transactions;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistorialBinding.inflate(inflater, container, false);


        /*transactions = new ArrayList<String>();
        transactions.add("Veracruz");
        transactions.add("Tabasco");
        transactions.add("Chiapas");
        transactions.add("Campeche");
        transactions.add("Quintana Roo");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, transactions);
        listview.setAdapter(adapter);*/
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        ListView listView = binding.listView;
        JSONObject obj1 = null;
        try {
            obj1 = new JSONObject("{}");
            obj1.put("type", "get_profile");
            obj1.put("token", Login.sessionToken);

            UtilsHTTP.sendPOST("https" + "://" + "corns-production.up.railway.app:" + 443 + "/dades", obj1.toString(), (response) -> {
                JSONObject objResponse = null;
                try {
                    objResponse = new JSONObject(response);
                    System.out.println(response);
                    if (objResponse.getString("status").equals("OK")) {
                        JSONArray JSONlist = objResponse.getJSONArray("result");
                        JSONObject user = null;
                        for (int i = 0; i < JSONlist.length(); i++) {
                            user = JSONlist.getJSONObject(i);
                            System.out.println("Entra user");
                            binding.textView3.setText("Saldo: "+user.getDouble("balance") + "€");
                        }
                        System.out.println(transactions);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, transactions);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        transactions = new ArrayList<String>();
        JSONObject obj = null;
        try {
            obj = new JSONObject("{}");
            obj.put("type", "get_transactions_app");
            obj.put("token", Login.sessionToken);

            UtilsHTTP.sendPOST("https" + "://" + "corns-production.up.railway.app:" + 443 + "/dades", obj.toString(), (response) -> {
                JSONObject objResponse = null;
                try {
                    objResponse = new JSONObject(response);
                    System.out.println(response);
                    if (objResponse.getString("status").equals("OK")) {
                        JSONArray JSONlist = objResponse.getJSONArray("transactions");
                        JSONObject transaction = null;
                        for (int i = 0; i < JSONlist.length(); i++) {
                            transaction = JSONlist.getJSONObject(i);
                            System.out.println(transaction);
                            System.out.println("Entraaaaaa");
                            if(transaction.getInt("accepted")==1) {
                                transactions.add("Origen: " + transaction.getString("origin").toString() + "\nDesti: " + transaction.getString("destiny").toString() + "\nQuantitat: " + transaction.getString("quantity").toString()+"\nAcceptat");
                            }
                            else{
                                transactions.add("Origen: " + transaction.getString("origin").toString() + "\nDesti: " + transaction.getString("destiny").toString() + "\nQuantitat: " + transaction.getString("quantity").toString()+"\nRefusat");

                            }
                                System.out.println(transactions);
                        }
                        System.out.println(transactions);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, transactions);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}