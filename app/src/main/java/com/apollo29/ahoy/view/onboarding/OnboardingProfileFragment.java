package com.apollo29.ahoy.view.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.apollo29.ahoy.R;
import com.apollo29.ahoy.databinding.OnboardingProfileFragmentBinding;
import com.apollo29.ahoy.view.OverlayFragment;
import com.google.android.material.datepicker.MaterialDatePicker;

import br.com.ilhasoft.support.validation.Validator;

public class OnboardingProfileFragment extends OverlayFragment {

    private NavController navController;
    private OnboardingViewModel viewModel;
    private OnboardingProfileFragmentBinding binding;
    private Validator validator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        statusBarColor();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.onboarding_profile_fragment, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        validator = new Validator(binding);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MaterialDatePicker<Long> dialog = MaterialDatePicker.Builder
                .datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setTitleText(R.string.onboarding_profile_form_birthday)
                .build();
        dialog.addOnPositiveButtonClickListener(selection -> viewModel.setDate(selection));
        binding.birthdayInput.setOnClickListener(view1 -> {
            dialog.showNow(getParentFragmentManager(),"dialog");
        });

        binding.flowNext.setOnClickListener(v -> {
            if (validator.validate()) {
                overlay(true);
                viewModel.storeProfile();
                overlay(false);
                navController.navigate(R.id.nav_main);
            }
        });

        binding.flowSkip.setOnClickListener(v -> {
            // todo add flag???
            navController.navigate(R.id.nav_main);
        });
    }

    private void statusBarColor(){
        Window window = requireActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(requireActivity(),R.color.white));
    }
}
