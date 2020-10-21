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
import com.apollo29.ahoy.databinding.CreateEventFragmentBinding;
import com.apollo29.ahoy.view.events.event.CreateEventViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.orhanobut.logger.Logger;

public class RegisterEventFragment extends Fragment {

    // SCANNING

    private NavController navController;
    private CreateEventViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        viewModel = new ViewModelProvider(requireActivity()).get(CreateEventViewModel.class);
        CreateEventFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.create_event_fragment, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
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

        TextInputEditText date = view.findViewById(R.id.event_date_input);
        date.setOnClickListener(v -> dialog.showNow(getParentFragmentManager(),"dialog"));

        MaterialButton createEvent = view.findViewById(R.id.create_event);
        viewModel.isValid().observe(getViewLifecycleOwner(), createEvent::setEnabled);
        createEvent.setOnClickListener(view1 -> {
            // start spinner
            viewModel.createEvent().observe(getViewLifecycleOwner(), event -> {
                // todo navigate or show alert --> single to optional/maybe ??
                Logger.d(event);
                // todo store in room, show spinner
                navController.navigate(R.id.nav_main);
            });
        });
    }
}
