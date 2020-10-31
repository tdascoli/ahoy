package com.apollo29.ahoy.repository;

import android.content.SharedPreferences;

import com.apollo29.ahoy.comm.RetrofitClientInstance;
import com.apollo29.ahoy.comm.authentication.AuthenticationService;
import com.apollo29.ahoy.comm.authentication.Credentials;
import com.apollo29.ahoy.comm.authentication.Token;
import com.orhanobut.logger.Logger;

import java.util.Optional;

import io.reactivex.rxjava3.core.Single;

import static com.apollo29.ahoy.repository.PreferencesRepository.SEC_AUTH_TOKEN;
import static com.apollo29.ahoy.repository.PreferencesRepository.SEC_PROFILE_ID;
import static com.apollo29.ahoy.repository.PreferencesRepository.SEC_PROFILE_SECRET;


public class AuthenticationRepository {

    public static Single<Token> deviceSignIn(Credentials credentials){
        AuthenticationService service = RetrofitClientInstance.getRetrofitInstance().create(AuthenticationService.class);
        return service.deviceSignIn(credentials)
                .doOnError(throwable -> Logger.w("Error on device sign in %s", throwable));
    }

    public static Single<Optional<String>> authToken(String deviceId, SharedPreferences prefs){
        if (prefs.contains(SEC_PROFILE_ID)){
            String authToken = prefs.getString(SEC_AUTH_TOKEN, "");
            if (!prefs.contains(SEC_AUTH_TOKEN) && !authToken.isEmpty()) {
                return Single.just(Optional.of(authToken));
            }

            String password = prefs.getString(SEC_PROFILE_SECRET, "");
            if (!deviceId.isEmpty() && !password.isEmpty()) {
                return AuthenticationRepository.deviceSignIn(Credentials.of(deviceId, password)).map(token -> {
                    prefs.edit().putString(SEC_AUTH_TOKEN, token.authToken()).apply();
                    return Optional.of(token.authToken());
                }).doOnError(throwable -> Logger.w("No Device Sign In possible: %s", throwable));
            }
        }
        return Single.just(Optional.empty());
    }
}
