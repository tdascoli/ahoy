package com.apollo29.ahoy.comm.profile;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProfileService {

    @GET("profiles/{deviceId}")
    Single<Profile> getProfile(@Path("deviceId") String deviceId);

    @Headers("Content-Type: application/json")
    @PUT("profiles")
    Single<Profile> putProfile(@Body Profile profile);
}
