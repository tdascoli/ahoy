package com.apollo29.ahoy.view.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.apollo29.ahoy.R;
import com.apollo29.ahoy.view.about.AboutFragment;
import com.apollo29.ahoy.view.events.EventsFragment;
import com.apollo29.ahoy.view.home.HomeFragment;
import com.apollo29.ahoy.view.profile.ProfileFragment;

import java.util.Arrays;
import java.util.List;

import me.ibrahimsn.lib.SmoothBottomBar;

public class MainFragment extends Fragment {

    private NavController navController;
    private SmoothBottomBar bottomBar;
    private int selectedPage = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        statusBarColor();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedPage",selectedPage);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null){
            selectedPage = savedInstanceState.getInt("selectedPage", 0);
        }

        ViewPager2 viewPager = view.findViewById(R.id.pager);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(selectedPage);

        bottomBar = view.findViewById(R.id.bottom_bar);
        bottomBar.setItemActiveIndex(selectedPage);
        bottomBar.setOnItemSelectedListener(i -> {
            selectedPage=i;
            viewPager.setCurrentItem(i);
            return true;
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.bottom_navigation_menu,menu);
        bottomBar.setupWithNavController(menu,navController);
    }

    private void setupViewPager(ViewPager2 viewPager) {
        List<Fragment> fragments = Arrays.asList(
                new HomeFragment(),
                new EventsFragment(),
                new ProfileFragment(),
                new AboutFragment());
        FragmentStateAdapter pagerAdapter = new PagerAdapter(requireActivity(), fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setUserInputEnabled(false); // todo to disable sync with bottombar!!
    }

    static class PagerAdapter extends FragmentStateAdapter {

        private final List<Fragment> fragments;

        public PagerAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments) {
            super(fragmentActivity);
            this.fragments = fragments;
        }

        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }

    private void statusBarColor(){
        Window window = requireActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(requireActivity(),R.color.homeColor));
    }
}