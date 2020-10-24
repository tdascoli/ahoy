package com.apollo29.ahoy.repository;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import com.apollo29.ahoy.AhoyApplication;
import com.apollo29.ahoy.comm.RetrofitClientInstance;
import com.apollo29.ahoy.comm.profile.Profile;
import com.apollo29.ahoy.comm.profile.ProfileService;
import com.apollo29.ahoy.comm.queue.Queue;
import com.apollo29.ahoy.data.AhoyProfile;
import com.apollo29.ahoy.data.repository.DatabaseRepository;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

import static com.apollo29.ahoy.repository.PreferencesRepository.SEC_AHOY_PROFILE;
import static com.apollo29.ahoy.repository.PreferencesRepository.SEC_PROFILE_ID;
import static com.apollo29.ahoy.repository.PreferencesRepository.SEC_PROFILE_SECRET;
import static com.apollo29.ahoy.repository.PreferencesRepository.SEC_PROFILE_SECRET_EMPTY;

public class ProfileRepository {

    private final SharedPreferences prefs;
    private final DatabaseRepository databaseRepository;
    private final String deviceId;

    @SuppressLint("HardwareIds")
    public ProfileRepository(Context context) {
        prefs = PreferencesRepository.prefs(context);
        databaseRepository = ((AhoyApplication) context).getRepository();
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public Integer profileId(){
        return prefs.getInt(SEC_PROFILE_ID, 0);
    }

    public boolean hasProfileId(){
        return prefs.contains(SEC_PROFILE_ID) && this.profileId()>0;
    }

    public void putProfileId(int profileId){
        prefs.edit().putInt(SEC_PROFILE_ID,profileId).apply();
    }

    public String profileSecret(){
        return prefs.getString(SEC_PROFILE_SECRET, SEC_PROFILE_SECRET_EMPTY);
    }

    public boolean hasProfileSecret(){
        return prefs.contains(SEC_PROFILE_SECRET) && !profileSecret().equals(SEC_PROFILE_SECRET_EMPTY);
    }

    public void putProfileSecret(String secret){
        prefs.edit().putString(SEC_PROFILE_SECRET, secret).apply();
    }

    public String deviceId(){
        return deviceId;
    }

    public SharedPreferences preferences(){
        return prefs;
    }

    public Single<Optional<String>> authToken(){
        return AuthenticationRepository.authToken(deviceId, prefs);
    }

    public boolean hasAhoyProfile(){
        return prefs.contains(SEC_AHOY_PROFILE) && ahoyProfile()!=null && !ahoyProfile().equals("");
    }

    public String ahoyProfile(){
        return prefs.getString(SEC_AHOY_PROFILE, "");
    }

    public void putAhoyProfile(AhoyProfile profile){
        prefs.edit().putString(SEC_AHOY_PROFILE, AhoyProfile.toJson(profile)).apply();
    }

    public static Single<Profile> putProfile(Profile profile){
        ProfileService service = RetrofitClientInstance.getRetrofitInstance().create(ProfileService.class);
        return service.putProfile(profile);
    }

    public Single<Boolean> updateQueue(int eventId){
        return authToken().flatMap(authToken -> {
            if (authToken.isPresent()){
                return QueueRepository.getQueuesByEventId(authToken.get(), eventId)
                        .doOnError(throwable -> Logger.w("Error while getting queue %s", throwable))
                        .flatMap(queues ->
                                databaseRepository.putQueue(queues.toArray(new Queue[0]))
                                        .andThen(confirm(authToken.get(), queues)));
            }
            return Single.just(true);
        });
    }

    private Single<Boolean> confirm(String authToken, List<Queue> queues){
        return Flowable.fromIterable(queues).map(queue ->
                QueueRepository.removeQueue(authToken, queue.uid))
                .doOnError(throwable -> Logger.w("Error while removing queue %s", throwable))
                .toList().map(singles -> true);
    }
}
