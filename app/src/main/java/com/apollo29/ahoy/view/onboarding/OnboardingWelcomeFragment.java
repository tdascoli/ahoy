package com.apollo29.ahoy.view.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.apollo29.ahoy.R;
import com.google.android.material.button.MaterialButton;

public class OnboardingWelcomeFragment extends Fragment {

    private NavController navController;
    private OnboardingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);
        return inflater.inflate(R.layout.onboarding_welcome_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel.hasProfile().observe(getViewLifecycleOwner(), hasProfile -> {
            if (hasProfile){
                navController.navigate(R.id.nav_main);
            }
            else {
                view.findViewById(R.id.ahoy_description).setVisibility(View.VISIBLE);
                view.findViewById(R.id.onboarding_start).setVisibility(View.VISIBLE);
            }
        });

        MaterialButton startButton = view.findViewById(R.id.onboarding_start);
        startButton.setOnClickListener(v -> {
            navController.navigate(R.id.nav_onboarding_profile);
        });
    }
}
