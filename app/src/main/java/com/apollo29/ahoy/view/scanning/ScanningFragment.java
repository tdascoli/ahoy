package com.apollo29.ahoy.view.scanning;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.apollo29.ahoy.R;
import com.google.android.material.button.MaterialButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.orhanobut.logger.Logger;
import com.sergivonavi.materialbanner.Banner;
import com.sergivonavi.materialbanner.BannerInterface;

import java.util.concurrent.ExecutionException;

import static com.apollo29.ahoy.view.events.register.RegisterManuallyFragment.EVENT_ID;
import static com.apollo29.ahoy.view.events.register.RegisterManuallyFragment.REGISTER_MANUALLY;

public class ScanningFragment extends Fragment {

    private NavController navController;
    private Banner banner;

    private static final int PERMISSION_REQUEST_CAMERA = 0;

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private MaterialButton qrCodeFoundButton;
    private String qrCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        return inflater.inflate(R.layout.scanning_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        banner = view.findViewById(R.id.banner);
        banner.setRightButton(R.string.label_ok, BannerInterface::dismiss);

        previewView = view.findViewById(R.id.scanning_preview);

        qrCodeFoundButton = view.findViewById(R.id.flow_register);
        qrCodeFoundButton.setVisibility(View.INVISIBLE);
        qrCodeFoundButton.setOnClickListener(v -> {
            String eventIdString = QRCodeUtil.eventFromUrl(qrCode);
            if (eventIdString!=null){
                int eventId = Integer.parseInt(eventIdString);
                Bundle bundle = new Bundle();
                bundle.putBoolean(REGISTER_MANUALLY, false);
                bundle.putInt(EVENT_ID, eventId);
                navController.navigate(R.id.nav_register_event, bundle);
            }
        });

        MaterialButton flowCancel = view.findViewById(R.id.flow_cancel);
        flowCancel.setOnClickListener(v -> requireActivity().onBackPressed());

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        requestCamera();
    }

    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                banner.setMessage(R.string.scanning_banner_camera_denied);
            }
        }
    }

    private void startCamera() {
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                banner.setMessage(R.string.scanning_banner_camera_error);
                Logger.w("Error starting camera %s", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        previewView.setImplementationMode(PreviewView.ImplementationMode.PERFORMANCE);

        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext()), new QRCodeImageAnalyzer(new QRCodeFoundListener() {
            @Override
            public void qrCodeFound(String _qrCode) {
                qrCode = _qrCode;
                // todo better solution
                qrCodeFoundButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void qrCodeNotFound() {
                qrCodeFoundButton.setVisibility(View.INVISIBLE);
            }
        }));

        Camera camera = cameraProvider.bindToLifecycle(getViewLifecycleOwner(), cameraSelector, imageAnalysis, preview);
    }
}
