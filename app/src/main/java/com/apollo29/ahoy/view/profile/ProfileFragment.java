package com.apollo29.ahoy.view.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.apollo29.ahoy.R;
import com.apollo29.ahoy.databinding.ProfileFragmentBinding;
import com.apollo29.ahoy.view.OverlayFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.sergivonavi.materialbanner.Banner;
import com.sergivonavi.materialbanner.BannerInterface;

import br.com.ilhasoft.support.validation.Validator;

public class ProfileFragment extends OverlayFragment {

    private ProfileViewModel viewModel;
    private ProfileFragmentBinding binding;
    private Validator validator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        validator = new Validator(binding);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Banner banner = view.findViewById(R.id.banner);
        banner.setRightButton(R.string.label_ok, BannerInterface::dismiss);

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
                overlay(true);
                viewModel.updateProfile();
                overlay(false);
                banner.show();
            }
        });
    }
}
