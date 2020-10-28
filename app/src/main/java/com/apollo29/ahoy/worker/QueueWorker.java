package com.apollo29.ahoy.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;
import androidx.work.rxjava3.RxWorker;

import com.apollo29.ahoy.AhoyApplication;
import com.apollo29.ahoy.data.repository.DatabaseRepository;
import com.apollo29.ahoy.repository.MainRepository;

import io.reactivex.rxjava3.core.Single;

public class QueueWorker extends RxWorker {

    public QueueWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Single<Result> createWork() {
        DatabaseRepository databaseRepository = ((AhoyApplication) getApplicationContext()).getRepository();
        MainRepository mainRepository = new MainRepository(getApplicationContext());

        return databaseRepository.getCurrentEvent(mainRepository.profileId()).flatMap(event -> {
            if (event.isPresent()){
                return mainRepository.updateQueue(event.get().uid).map(success -> Result.success());
            }
            return Single.just(Result.success());
        });
    }
}
