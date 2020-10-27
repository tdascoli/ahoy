package com.apollo29.ahoy.view.home;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.apollo29.ahoy.AhoyApplication;
import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.data.repository.DatabaseRepository;
import com.apollo29.ahoy.repository.ProfileRepository;
import com.apollo29.ahoy.worker.HousekeepingWorker;
import com.apollo29.ahoy.worker.QueueWorker;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class HomeViewModel extends AndroidViewModel {

    private final DatabaseRepository databaseRepository;
    private final ProfileRepository profileRepository;
    private final WorkManager workManager;

    private final static String QUEUE_WORKER = "queue_worker";
    private final static String HOUSEKEEPING_WORKER = "housekeeping_worker";

    public HomeViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(getApplication());
        databaseRepository = ((AhoyApplication) application).getRepository();
        workManager = WorkManager.getInstance(application);
    }

    public LiveData<Optional<Event>> currentEvent(){
        return LiveDataReactiveStreams.fromPublisher(databaseRepository.getCurrentEvent(profileRepository.profileId()).toFlowable());
    }

    public LiveData<Boolean> hasEvent(){
        return LiveDataReactiveStreams.fromPublisher(databaseRepository.hasEvent(profileRepository.profileId()).toFlowable());
    }

    // todo refactor with only zxing
    public Bitmap qrcode(String inputValue) {
        QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, 500);
        return qrgEncoder.getBitmap();
    }

    public void dequeue(){
        workManager.cancelUniqueWork(QUEUE_WORKER);
    }

    public void enqueue(){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(QueueWorker.class, 5, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .build();
        workManager.enqueueUniquePeriodicWork(QUEUE_WORKER, ExistingPeriodicWorkPolicy.KEEP, work);
    }

    public void housekeeping(){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(HousekeepingWorker.class, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build();
        workManager.enqueueUniquePeriodicWork(HOUSEKEEPING_WORKER, ExistingPeriodicWorkPolicy.KEEP, work);
    }
}
