package com.apollo29.ahoy.repository;

import com.apollo29.ahoy.BuildConfig;
import com.apollo29.ahoy.comm.RetrofitClientInstance;
import com.apollo29.ahoy.comm.queue.Queue;
import com.apollo29.ahoy.comm.queue.QueueService;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class QueueRepository {

    public static Single<Queue> putQueue(int eventId, Queue queue){
        String apikey = BuildConfig.apikey;
        QueueService service = RetrofitClientInstance.getRetrofitInstance().create(QueueService.class);
        return service.putQueue(apikey, eventId, queue)
                .doOnError(throwable -> Logger.w("Error on put Queue %s", throwable));
    }

    public static Single<List<Queue>> getQueuesByEventId(String authToken, int eventId){
        QueueService service = RetrofitClientInstance.getRetrofitInstance().create(QueueService.class);
        return service.getQueue(authToken, eventId)
                .doOnError(throwable -> Logger.w("Error on get Queue By Event Id %s", throwable));
    }

    public static Completable removeQueue(String authToken, int id){
        Logger.d("remove queue %s", id);
        QueueService service = RetrofitClientInstance.getRetrofitInstance().create(QueueService.class);
        return service.removeQueue(authToken, id)
                .doOnComplete(() -> {
                    Logger.w("COMPLETE Queue");
                })
                .doOnError(throwable -> Logger.w("Error on remove Queue %s", throwable));
    }
}
