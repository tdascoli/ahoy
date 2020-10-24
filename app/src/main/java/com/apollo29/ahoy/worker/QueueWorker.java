package com.apollo29.ahoy.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;
import androidx.work.rxjava3.RxWorker;

import com.apollo29.ahoy.AhoyApplication;
import com.apollo29.ahoy.data.repository.DatabaseRepository;
import com.apollo29.ahoy.repository.ProfileRepository;

import io.reactivex.rxjava3.core.Single;

public class QueueWorker extends RxWorker {

    public QueueWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Single<Result> createWork() {
        DatabaseRepository databaseRepository = ((AhoyApplication) getApplicationContext()).getRepository();
        ProfileRepository profileRepository = new ProfileRepository(getApplicationContext());

        return databaseRepository.getCurrentEvent(profileRepository.profileId()).flatMap(event -> {
            if (event.isPresent()){
                return profileRepository.updateQueue(event.get().uid).map(success -> Result.success());
            }
            return Single.just(Result.success());
        });
    }
}
