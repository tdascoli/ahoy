package com.apollo29.ahoy.view.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.apollo29.ahoy.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.apollo29.ahoy.view.events.register.RegisterManuallyFragment.EVENT_ID;
import static com.apollo29.ahoy.view.events.register.RegisterManuallyFragment.REGISTER_MANUALLY;

public class HomeFragment extends Fragment {

    private NavController navController;
    private HomeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel.housekeeping();

        MaterialCardView currentEvent = view.findViewById(R.id.current_event);
        viewModel.currentEvent().observe(getViewLifecycleOwner(), maybeEvent -> {
            if (maybeEvent.isPresent()){
                viewModel.enqueue();

                ImageView qrcode = view.findViewById(R.id.event_qrcode);
                qrcode.setImageBitmap(viewModel.qrcode("https://apollo29.com/ahoy/event/"+maybeEvent.get().uid));

                TextView eventTitle = view.findViewById(R.id.event_title);
                eventTitle.setText(maybeEvent.get().title);

                MaterialButton registerManually = view.findViewById(R.id.event_register_manually);
                registerManually.setOnClickListener(view1 -> {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(REGISTER_MANUALLY, true);
                    bundle.putInt(EVENT_ID, maybeEvent.get().uid);
                    navController.navigate(R.id.nav_register_event, bundle);
                });

                MaterialButton guests = view.findViewById(R.id.event_participants);
                guests.setOnClickListener(view1 -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt(EVENT_ID, maybeEvent.get().uid);
                    navController.navigate(R.id.nav_events_guests, bundle);
                });
            }
            else {
                viewModel.dequeue();
            }
            currentEvent.setVisibility((maybeEvent.isPresent() ? View.VISIBLE : View.GONE));
        });

        ExtendedFloatingActionButton createEvent = view.findViewById(R.id.create_event);
        createEvent.setOnClickListener(v -> navController.navigate(R.id.nav_create_event));
        viewModel.hasEvent().observe(getViewLifecycleOwner(), hasEvent ->
                createEvent.setVisibility((hasEvent ? View.VISIBLE : View.GONE)));

        FloatingActionButton scanEvent = view.findViewById(R.id.scan_event);
        scanEvent.setOnClickListener(view1 -> {
            navController.navigate(R.id.nav_scan_event);
        });
    }
}