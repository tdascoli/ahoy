package com.apollo29.ahoy.comm.event;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EventService {

    @GET("event/{eventId}")
    Maybe<Event> getEvent(@Header("Authorization") String token, @Path("eventId") String eventId);

    @Headers("Content-Type: application/json")
    @PUT("event")
    Single<Event> putEvent(@Header("Authorization") String token, @Body Event event);
}
