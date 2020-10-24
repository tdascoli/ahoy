package com.apollo29.ahoy.view.scanning;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.apollo29.ahoy.R;
import com.orhanobut.logger.Logger;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class ScanningFragment extends Fragment {

    // SCANNING

    private NavController navController;
    private ScanningViewModel viewModel;

    private final static int PERMISSION_CAMERA_REQUEST = 1;
    private final static double RATIO_4_3_VALUE = 4.0 / 3.0;
    private final static double RATIO_16_9_VALUE = 16.0 / 9.0;

    private PreviewView previewView;
    private ProcessCameraProvider cameraProvider;
    private CameraSelector cameraSelector;
    private int lensFacing = CameraSelector.LENS_FACING_BACK;
    private Preview previewUseCase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        viewModel = new ViewModelProvider(requireActivity()).get(ScanningViewModel.class);
        return inflater.inflate(R.layout.scanning_fragment, container, false);
        /*
        ScanningFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.scanning_fragment, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        return binding.getRoot();*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        previewView = view.findViewById(R.id.viewFinder);
        setupCamera();
    }

    private int screenAspectRatio(){
        // Get screen metrics used to setup camera for full screen resolution
        DisplayMetrics metrics = new DisplayMetrics();
        previewView.getDisplay().getRealMetrics(metrics);
        return aspectRatio(metrics.widthPixels, metrics.heightPixels);
    }

    /**
     *  [androidx.camera.core.ImageAnalysis],[androidx.camera.core.Preview] requires enum value of
     *  [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     *
     *  Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     *  of preview ratio to one of the provided values.
     *
     *  @param width - preview width
     *  @param height - preview height
     *  @return suitable aspect ratio
     */
    private int aspectRatio(int width, int height) {
        double previewRatio = max(width, height) / min(width, height);
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    private void setupCamera(){
        cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();

        viewModel.processCameraProvider().observe(getViewLifecycleOwner(), processCameraProvider -> {
            cameraProvider = processCameraProvider;
            if (isCameraPermissionGranted()) {
                bindPreviewUseCase();
            } else {
                ActivityCompat.requestPermissions(
                        requireActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_CAMERA_REQUEST);
            }
        });
    }

    private void bindPreviewUseCase() {
        if (cameraProvider == null) {
            return;
        }
        if (previewUseCase != null) {
            cameraProvider.unbind(previewUseCase);
        }
        previewUseCase = new Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio())
                .setTargetRotation(previewView.getDisplay().getRotation())
                .build();
        previewUseCase.setSurfaceProvider(previewView.getSurfaceProvider());

        try {
            cameraProvider.bindToLifecycle(getViewLifecycleOwner(),
                    cameraSelector,
                    previewUseCase);
        } catch (IllegalStateException | IllegalArgumentException e) {
            Logger.w("Unhandled exception %s", e);
        }
    }

    private boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA_REQUEST) {
            if (isCameraPermissionGranted()) {
                bindPreviewUseCase();
            } else {
                Logger.w("no camera permission");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
