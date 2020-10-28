package com.apollo29.ahoy.comm.profile;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface ProfileService {

    @Headers("Content-Type: application/json")
    @PUT("profiles")
    Single<Profile> putProfile(@Header("Authorization") String apikey, @Body Profile profile);
}
