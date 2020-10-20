package com.apollo29.ahoy.comm.authentication;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthenticationService {

    @Headers({"Content-Type: application/json" })
    @POST("auth/profile")
    Single<Token> deviceSignIn(@Body Credentials credentials);
}
