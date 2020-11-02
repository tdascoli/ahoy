package com.apollo29.ahoy.view.events.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.apollo29.ahoy.R;
import com.apollo29.ahoy.databinding.CreateEventFragmentBinding;
import com.apollo29.ahoy.view.OverlayFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.orhanobut.logger.Logger;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import br.com.ilhasoft.support.validation.Validator;

public class CreateEventFragment extends OverlayFragment implements PurchasesUpdatedListener {

    private NavController navController;
    private CreateEventViewModel viewModel;
    private CreateEventFragmentBinding binding;
    private Validator validator;

    // Billing
    private BillingClient billingClient;
    private List<String> products = new ArrayList<>(); //Collections.singletonList("ahoy_event");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        statusBarColor();
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
        viewModel.resetEvent();

        // Billing
        setupBillingClient();
        // endregion

        // todo add constraints for all events, async...
        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .build();

        MaterialDatePicker<Long> dialog = MaterialDatePicker.Builder
                .datePicker()
                .setCalendarConstraints(constraints)
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

        MaterialButton flowCancel = view.findViewById(R.id.flow_cancel);
        flowCancel.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void statusBarColor(){
        Window window = requireActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(requireActivity(),R.color.homeColor));
    }

    private void setupBillingClient(){
        billingClient = BillingClient.newBuilder(requireContext())
                .enablePendingPurchases()
                .setListener(this)
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is setup successfully
                    Logger.d("Connected to Google Play");
                    loadSku();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Logger.w("Failed to connect to Google Play");
                // todo Banner
            }
        });

    }

    private void loadSku(){
        if (billingClient.isReady()) {
            Logger.d("1 products");
            products.add("ahoy_event");
            Logger.d(products);
            SkuDetailsParams params = SkuDetailsParams
                    .newBuilder()
                    .setSkusList(products)
                    .setType(BillingClient.SkuType.INAPP)
                    .build();

            billingClient.querySkuDetailsAsync(params, (billingResult, list) -> {
                Logger.d("sku %s // %s", list, billingResult);
                // Process the result.
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && !list.isEmpty()) {
                    for (SkuDetails skuDetails : list) {
                        Logger.d("products");
                        Logger.d(skuDetails);
                        //this will return both the SKUs from Google Play Console
                        binding.purchaseEvent.setOnClickListener(view -> {
                            Logger.d("buy event");
                            BillingFlowParams billingFlowParams = BillingFlowParams
                                    .newBuilder()
                                    .setSkuDetails(skuDetails)
                                    .build();
                            billingClient.launchBillingFlow(requireActivity(), billingFlowParams);
                        });
                    }
                }
            });
        }
        else {
            Logger.w("Billing Client not ready");
        }
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

    }
}
