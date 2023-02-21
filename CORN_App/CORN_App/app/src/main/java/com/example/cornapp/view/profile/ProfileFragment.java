package com.example.cornapp.view.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cornapp.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        setupListeners();
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        return binding.getRoot();
    }


    public void setupListeners(){
        binding.fab.setOnClickListener(view -> {
            viewModel.updateUser(
                    binding.profileUserNameValue.getText(),
                    binding.profileUserSurnameValue.getText(),
                    binding.profileContactTelfValue.getText(),
                    binding.profileEmailEditValue.getText()
            );
        });
    }

}