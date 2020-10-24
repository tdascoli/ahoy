package com.apollo29.ahoy.view.events.register;

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
import com.apollo29.ahoy.databinding.RegisterManuallyFragmentBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

import br.com.ilhasoft.support.validation.Validator;

public class RegisterManuallyFragment extends Fragment {

    private NavController navController;
    private RegisterViewModel viewModel;
    private RegisterManuallyFragmentBinding binding;
    private Validator validator;

    public final static String REGISTER_MANUALLY = "registerManually";
    public final static String EVENT_ID = "eventId";

    private boolean registerManually = false;
    private int eventId = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        viewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.register_manually_fragment, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        validator = new Validator(binding);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments()!=null){
            registerManually = RegisterManuallyFragmentArgs.fromBundle(getArguments()).getRegisterManually();
            eventId = RegisterManuallyFragmentArgs.fromBundle(getArguments()).getEventId();
        }

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder
                .datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setTitleText(R.string.onboarding_profile_form_birthday);

        viewModel.getEvent(eventId).observe(getViewLifecycleOwner(), event -> {
            if (!event.isEmpty()) {
                viewModel.eventId(event.uid);
                if (registerManually) {
                     binding.registerEventTitle.setText(getString(R.string.events_register_manually_title, event.title));
                } else {
                    binding.registerEventTitle.setText(getString(R.string.events_register_event_title, event.title));
                    viewModel.loadProfile();
                    builder.setSelection(viewModel.timestamp.getValue());
                }
            }
        });

        MaterialDatePicker<Long> dialog = builder.build();
        dialog.addOnPositiveButtonClickListener(selection -> viewModel.setDate(selection));
        binding.birthdayInput.setOnClickListener(view1 -> {
            dialog.showNow(getParentFragmentManager(),"dialog");
        });

        binding.flowRegister.setOnClickListener(view1 -> {
            if (validator.validate()) {
                viewModel.register(registerManually).observe(getViewLifecycleOwner(), success -> {
                    // todo spinner etc
                    navController.navigate(R.id.nav_main);
                });
            }
        });

        binding.flowCancel.setOnClickListener(view1 -> {
            navController.navigate(R.id.nav_main);
        });
    }
}
