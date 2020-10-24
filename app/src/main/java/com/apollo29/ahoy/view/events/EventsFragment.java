package com.apollo29.ahoy.view.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo29.ahoy.R;
import com.apollo29.ahoy.comm.event.Event;
import com.orhanobut.logger.Logger;

import static com.apollo29.ahoy.view.events.register.RegisterManuallyFragment.EVENT_ID;

public class EventsFragment extends Fragment {

    @Nullable
    private EventAdapter adapter;
    private EventsViewModel viewModel;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        viewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        return inflater.inflate(R.layout.events_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView eventList =  view.findViewById(R.id.events_list);
        EventAdapter.OnItemClickListener eventClickListener = (view1, position) -> {
            Logger.d("click %s", position);
            if (adapter!=null) {
                Event event = adapter.getItem(position);
                Logger.d("event %s", event);
                if (event != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(EVENT_ID, event.uid);
                    navController.navigate(R.id.nav_events_guests, bundle);
                }
            }
        };

        viewModel.events().observe(getViewLifecycleOwner(), events -> {
            if (adapter==null) {
                adapter = new EventAdapter(events, eventClickListener);
                eventList.setAdapter(adapter);
            }
            else {
                adapter.updateEvents(events);
            }
        });
    }
}
