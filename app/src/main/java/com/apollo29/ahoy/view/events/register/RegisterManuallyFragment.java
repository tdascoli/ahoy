package com.apollo29.ahoy.view.events.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.apollo29.ahoy.R;
import com.apollo29.ahoy.databinding.RegisterManuallyFragmentBinding;

public class RegisterManuallyFragment extends Fragment {

    private NavController navController;
    private RegisterViewModel viewModel;

    public final static String REGISTER_MANUALLY = "registerManually";
    public final static String EVENT_ID = "eventId";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        viewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
        RegisterManuallyFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.register_manually_fragment, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        boolean registerManually = false;
        int eventId = 0;
        if (getArguments()!=null){
            registerManually = RegisterManuallyFragmentArgs.fromBundle(getArguments()).getRegisterManually();
            eventId = RegisterManuallyFragmentArgs.fromBundle(getArguments()).getEventId();
        }

        TextView title = view.findViewById(R.id.register_event_title);
        if (registerManually){
            title.setText(getString(R.string.events_register_manually_title, "test"));
        }
        else {
            title.setText(getString(R.string.events_register_event_title, "test"));
            // load profile

        }
    }

    public static RegisterManuallyFragment newInstance(boolean registerManually, int eventId){
        RegisterManuallyFragment fragment = new RegisterManuallyFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(REGISTER_MANUALLY, registerManually);
        bundle.putInt(EVENT_ID, eventId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
