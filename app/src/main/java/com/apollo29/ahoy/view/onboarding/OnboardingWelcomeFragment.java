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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.apollo29.ahoy.R;
import com.google.android.material.button.MaterialButton;

public class OnboardingWelcomeFragment extends Fragment {

    private NavController navController;
    private OnboardingViewModel viewModel;

    private static final String SHORTCUT_SCANNING = "com.apollo29.ahoy.shortcuts.SCANNING";
    private static final String SHORTCUT_CREATE_EVENT = "com.apollo29.ahoy.shortcuts.CREATE_EVENT";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        // Shortcuts
        handleShortcut(requireActivity().getIntent().getAction());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        statusBarColor();
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
                view.findViewById(R.id.onboarding_start).setVisibility(View.VISIBLE);
            }
        });

        MaterialButton startButton = view.findViewById(R.id.onboarding_start);
        startButton.setOnClickListener(v -> {
            navController.navigate(R.id.nav_onboarding_profile);
        });
    }

    private void statusBarColor(){
        Window window = requireActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(requireActivity(),R.color.white));
    }

    /**
     * Starts the relevant activity/fragment based on which shortcut has been pressed
     * @param intentAction specific shortcut action
     */
    private void handleShortcut(@Nullable String intentAction) {
        if (intentAction!=null) {
            switch (intentAction) {
                case SHORTCUT_SCANNING:
                    navController.navigate(R.id.nav_scan_event);
                    break;
                case SHORTCUT_CREATE_EVENT:
                    navController.navigate(R.id.nav_create_event);
                    break;
            }
        }
    }
}
