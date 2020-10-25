package com.apollo29.ahoy.view;

import androidx.fragment.app.Fragment;

import com.apollo29.ahoy.MainActivity;

public abstract class OverlayFragment extends Fragment {

    protected void overlay(boolean show){
        if (requireActivity() instanceof MainActivity){
            MainActivity activity = (MainActivity) requireActivity();
            activity.overlay(show);
        }
    }

}
