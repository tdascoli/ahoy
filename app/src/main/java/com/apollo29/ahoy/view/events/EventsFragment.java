package com.apollo29.ahoy.view.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo29.ahoy.R;
import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.view.events.download.DownloadFragment;
import com.apollo29.ahoy.view.events.download.DownloadViewModel;
import com.sergivonavi.materialbanner.Banner;
import com.sergivonavi.materialbanner.BannerInterface;

public class EventsFragment extends DownloadFragment {

    @Nullable
    private EventAdapter adapter;
    private EventsViewModel viewModel;
    private NavController navController;

    private final static String EVENT_ID = "eventId";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        viewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        downloadViewModel = new ViewModelProvider(requireActivity()).get(DownloadViewModel.class);
        return inflater.inflate(R.layout.events_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Banner banner = view.findViewById(R.id.banner);
        banner.setRightButton(R.string.label_ok, BannerInterface::dismiss);

        RecyclerView eventList =  view.findViewById(R.id.events_list);
        EventAdapter.OnItemClickListener eventClickListener = (view1, position) -> {
            if (adapter!=null) {
                Event event = adapter.getItem(position);
                if (event != null) {
                    if (EventUtil.isDoneOrCurrent(event)) {
                        banner.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putInt(EVENT_ID, event.uid);
                        navController.navigate(R.id.nav_events_guests, bundle);
                    }
                    else {
                        banner.show();
                    }
                }
            }
        };

        EventAdapter.OnDownloadClickListener downloadClickListener = (view1, position) -> {
            if (adapter!=null) {
                Event event = adapter.getItem(position);
                if (event != null) {
                    prepareCsvDownload(event);
                }
            }
        };

        viewModel.events().observe(getViewLifecycleOwner(), events -> {
            if (adapter==null) {
                adapter = new EventAdapter(events, eventClickListener, downloadClickListener);
                eventList.setAdapter(adapter);
            }
            else {
                adapter.updateEvents(events);
            }
        });
    }
}
