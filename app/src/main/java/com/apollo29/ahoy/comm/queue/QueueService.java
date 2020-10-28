package com.apollo29.ahoy.comm.queue;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface QueueService {

    @GET("queue/{eventId}")
    Single<List<Queue>> getQueue(@Header("Authorization") String token, @Path("eventId") int eventId);

    @Headers("Content-Type: application/json")
    @PUT("queue/{eventId}")
    Single<Queue> putQueue(@Header("Authorization") String apikey, @Path("eventId") int eventId, @Body Queue queue);

    @DELETE("queue/{uid}")
    Single<String> removeQueue(@Header("Authorization") String token, @Path("uid") int uid);
}
