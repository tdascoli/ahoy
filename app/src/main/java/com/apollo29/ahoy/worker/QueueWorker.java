package com.apollo29.ahoy.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;
import androidx.work.rxjava3.RxWorker;

import com.apollo29.ahoy.AhoyApplication;
import com.apollo29.ahoy.comm.queue.Queue;
import com.apollo29.ahoy.data.repository.DatabaseRepository;
import com.apollo29.ahoy.repository.ProfileRepository;
import com.apollo29.ahoy.repository.QueueRepository;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
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
                return profileRepository.authToken().flatMap(authToken -> {
                    if (authToken.isPresent()){
                        return QueueRepository.getQueuesByEventId(authToken.get(), event.get().uid)
                                .doOnError(throwable -> Logger.w("Error while getting queue %s", throwable))
                                .flatMap(queues ->
                                    databaseRepository.putQueue(queues.toArray(new Queue[0]))
                                        .andThen(confirm(authToken.get(), queues)));
                    }
                    return Single.just(Result.success());
                });
            }
            return Single.just(Result.success());
        });
    }

    private Single<Result> confirm(String authToken, List<Queue> queues){
        return Flowable.fromIterable(queues).map(queue ->
                QueueRepository.removeQueue(authToken, queue.uid))
                    .doOnError(throwable -> Logger.w("Error while removing queue %s", throwable))
                    .toList().map(singles -> Result.success());
    }
}
