package com.apollo29.ahoy.view.events.guests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apollo29.ahoy.R;
import com.apollo29.ahoy.view.events.EventUtil;
import com.apollo29.ahoy.view.events.EventsViewModel;
import com.apollo29.ahoy.view.events.download.DownloadFragment;
import com.apollo29.ahoy.view.events.download.DownloadViewModel;
import com.apollo29.ahoy.view.events.register.RegisterManuallyFragmentArgs;
import com.google.android.material.button.MaterialButton;

import static com.apollo29.ahoy.view.events.register.RegisterManuallyFragment.EVENT_ID;
import static com.apollo29.ahoy.view.events.register.RegisterManuallyFragment.REGISTER_MANUALLY;

public class GuestsFragment extends DownloadFragment {
    @Nullable
    private GuestAdapter adapter;
    private NavController navController;
    private EventsViewModel viewModel;
    private int eventId = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        viewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        downloadViewModel = new ViewModelProvider(requireActivity()).get(DownloadViewModel.class);
        return inflater.inflate(R.layout.guests_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments()!=null){
            eventId = RegisterManuallyFragmentArgs.fromBundle(getArguments()).getEventId();
        }

        TextView title = view.findViewById(R.id.guests_title);
        TextView subtitle = view.findViewById(R.id.guests_subtitle);

        RecyclerView list =  view.findViewById(R.id.guests_list);
        viewModel.guests(eventId).observe(getViewLifecycleOwner(), guests -> {
            if (adapter==null) {
                adapter = new GuestAdapter(guests);
                list.setAdapter(adapter);
            }
            else {
                adapter.update(guests);
            }
        });

        SwipeRefreshLayout refreshView =  view.findViewById(R.id.refresh_view);
        refreshView.setOnRefreshListener(() ->
                viewModel.refreshData(eventId).observe(getViewLifecycleOwner(), refreshView::setRefreshing));

        MaterialButton flowBack = view.findViewById(R.id.flow_back);
        flowBack.setOnClickListener(v -> requireActivity().onBackPressed());

        MaterialButton flowPrimary = view.findViewById(R.id.flow_primary);
        viewModel.loadEvent(eventId).observe(getViewLifecycleOwner(), event -> {
            if (!event.isEmpty()){
                title.setText(getString(R.string.events_guests_title, event.title));
                subtitle.setText(getString(R.string.events_guests_subtitle, EventUtil.getDefaultDateString(event)));
                if (EventUtil.isCurrent(event)){
                    flowPrimary.setText(R.string.label_add);
                    flowPrimary.setOnClickListener(view1 -> {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(REGISTER_MANUALLY, true);
                        bundle.putInt(EVENT_ID, event.uid);
                        navController.navigate(R.id.nav_register_event, bundle);
                    });
                }
                else {
                    flowPrimary.setText(R.string.label_download);
                    flowPrimary.setOnClickListener(view1 -> {
                        prepareCsvDownload(event);
                    });
                }
            }
        });
    }
}
