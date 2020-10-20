package com.apollo29.ahoy.comm.queue;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface QueueService {

    @GET("queue/{eventId}")
    Single<List<Queue>> getQueue(@Header("Authorization") String token, @Path("eventId") String eventId);
}
