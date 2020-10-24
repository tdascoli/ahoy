package com.apollo29.ahoy.view.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.apollo29.ahoy.R;
import com.apollo29.ahoy.databinding.ProfileFragmentBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

import br.com.ilhasoft.support.validation.Validator;

public class ProfileFragment extends Fragment {

    private NavController navController;
    private ProfileViewModel viewModel;
    private ProfileFragmentBinding binding;
    private Validator validator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        validator = new Validator(binding);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel.loadProfile();
        MaterialDatePicker<Long> dialog = MaterialDatePicker.Builder
                .datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setSelection(viewModel.timestamp.getValue())
                .setTitleText(R.string.onboarding_profile_form_birthday)
                .build();
        dialog.addOnPositiveButtonClickListener(selection -> viewModel.setDate(selection));
        binding.birthdayInput.setOnClickListener(view1 -> {
            dialog.showNow(getParentFragmentManager(),"dialog");
        });

        binding.profileUpdate.setOnClickListener(view1 -> {
            if (validator.validate()) {
                viewModel.updateProfile();
                // todo spinner and banner
            }
        });
    }
}
