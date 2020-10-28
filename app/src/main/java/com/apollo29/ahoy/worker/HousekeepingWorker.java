package com.apollo29.ahoy.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;
import androidx.work.rxjava3.RxWorker;

import com.apollo29.ahoy.AhoyApplication;
import com.apollo29.ahoy.data.repository.DatabaseRepository;
import com.apollo29.ahoy.repository.MainRepository;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Single;

public class HousekeepingWorker extends RxWorker {

    public HousekeepingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Single<Result> createWork() {
        DatabaseRepository databaseRepository = ((AhoyApplication) getApplicationContext()).getRepository();
        MainRepository mainRepository = new MainRepository(getApplicationContext());

        return databaseRepository.getEventsToClear(mainRepository.profileId()).flatMap(events -> {
            if (!events.isEmpty()){
                List<Integer> eventIds = events.stream().map(event -> event.uid).collect(Collectors.toList());
                return databaseRepository.inactivateEvents(eventIds).andThen(Single.just(Result.success()));
            }
            return Single.just(Result.success());
        });
    }
}
