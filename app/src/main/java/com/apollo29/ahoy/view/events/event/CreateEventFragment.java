package com.apollo29.ahoy.view.events.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.apollo29.ahoy.R;
import com.apollo29.ahoy.databinding.CreateEventFragmentBinding;
import com.apollo29.ahoy.view.OverlayFragment;
import com.google.android.material.datepicker.MaterialDatePicker;

import br.com.ilhasoft.support.validation.Validator;

public class CreateEventFragment extends OverlayFragment {

    private NavController navController;
    private CreateEventViewModel viewModel;
    private CreateEventFragmentBinding binding;
    private Validator validator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        viewModel = new ViewModelProvider(requireActivity()).get(CreateEventViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.create_event_fragment, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        validator = new Validator(binding);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MaterialDatePicker<Long> dialog = MaterialDatePicker.Builder
                .datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTitleText(R.string.events_create_form_date)
                .build();
        dialog.addOnPositiveButtonClickListener(selection -> viewModel.setDate(selection));
        binding.eventDateInput.setOnClickListener(v -> dialog.showNow(getParentFragmentManager(),"dialog"));
        binding.createEvent.setOnClickListener(view1 -> {
            // start spinner
            if (validator.validate()) {
                overlay(true);
                viewModel.createEvent().observe(getViewLifecycleOwner(), event -> {
                    overlay(false);
                    navController.navigate(R.id.nav_main);
                });
            }
        });
    }
}
