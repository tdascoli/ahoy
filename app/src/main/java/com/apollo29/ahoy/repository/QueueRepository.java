package com.apollo29.ahoy.repository;

import com.apollo29.ahoy.BuildConfig;
import com.apollo29.ahoy.comm.RetrofitClientInstance;
import com.apollo29.ahoy.comm.queue.Queue;
import com.apollo29.ahoy.comm.queue.QueueService;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class QueueRepository {

    public static Single<Queue> putQueue(int eventId, Queue queue){
        String apikey = BuildConfig.apikey;
        QueueService service = RetrofitClientInstance.getRetrofitInstance().create(QueueService.class);
        return service.putQueue(apikey, eventId, queue);
    }

    public static Single<List<Queue>> getQueuesByEventId(String authToken, int eventId){
        QueueService service = RetrofitClientInstance.getRetrofitInstance().create(QueueService.class);
        return service.getQueue(authToken, eventId);
    }

    public static Single<String> removeQueue(String authToken, int uid){
        QueueService service = RetrofitClientInstance.getRetrofitInstance().create(QueueService.class);
        return service.removeQueue(authToken, uid);
    }
}
