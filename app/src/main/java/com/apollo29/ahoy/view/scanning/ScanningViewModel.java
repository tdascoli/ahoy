package com.apollo29.ahoy.view.scanning;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.common.util.concurrent.ListenableFuture;
import com.orhanobut.logger.Logger;

import java.util.concurrent.ExecutionException;

public class ScanningViewModel extends AndroidViewModel {

    private MutableLiveData<ProcessCameraProvider> cameraProviderLiveData = new MutableLiveData<ProcessCameraProvider>();

    public ScanningViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ProcessCameraProvider> processCameraProvider(){
        if (cameraProviderLiveData == null) {
            cameraProviderLiveData = new MutableLiveData<>();
            ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getApplication());
            cameraProviderFuture.addListener(
                    () -> {
                        try {
                            cameraProviderLiveData.setValue(cameraProviderFuture.get());
                        } catch (ExecutionException | InterruptedException e) {
                           Logger.w("Unhandled exception %s", e);
                        }
                    },
                    ContextCompat.getMainExecutor(getApplication()));
        }
        return cameraProviderLiveData;
    }
}
