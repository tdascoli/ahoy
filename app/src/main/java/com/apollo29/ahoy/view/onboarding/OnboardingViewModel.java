package com.apollo29.ahoy.view.onboarding;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import com.apollo29.ahoy.comm.ProfileRepository;
import com.apollo29.ahoy.comm.profile.Profile;
import com.apollo29.ahoy.data.AhoyProfile;
import com.apollo29.ahoy.repository.PreferencesRepository;
import com.orhanobut.logger.Logger;

import net.andreinc.mockneat.MockNeat;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.reactivex.rxjava3.core.Single;

import static com.apollo29.ahoy.repository.PreferencesRepository.SEC_AHOY_PROFILE;
import static com.apollo29.ahoy.repository.PreferencesRepository.SEC_PROFILE_ID;
import static com.apollo29.ahoy.repository.PreferencesRepository.SEC_PROFILE_SECRET;

public class OnboardingViewModel extends AndroidViewModel {

    private final MutableLiveData<String> firstname = new MutableLiveData<>();
    private final MutableLiveData<String> lastname = new MutableLiveData<>();
    private final MutableLiveData<String> address = new MutableLiveData<>();
    private final MutableLiveData<String> birthday = new MutableLiveData<>();
    private final MutableLiveData<String> mobile = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();

    private final SharedPreferences prefs;
    private final MockNeat random = MockNeat.secure();
    private final String deviceId;

    @SuppressLint("HardwareIds")
    public OnboardingViewModel(@NonNull Application application) {
        super(application);
        this.prefs = PreferencesRepository.prefs(getApplication().getApplicationContext());
        this.deviceId = Settings.Secure.getString(getApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public MutableLiveData<String> getFirstname() {
        return firstname;
    }

    public MutableLiveData<String> getLastname() {
        return lastname;
    }

    public MutableLiveData<String> getAddress() {
        return address;
    }

    public MutableLiveData<String> getBirthday(){
        return birthday;
    }

    public MutableLiveData<String> getMobile() {
        return mobile;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public void storeProfile(){
        AhoyProfile profile = new AhoyProfile(
                firstname.getValue(),
                lastname.getValue(),
                address.getValue(),
                birthday.getValue(),
                mobile.getValue(),
                email.getValue());
        Logger.d(profile.toString());
        Logger.d("AHOY PROFILE %s", AhoyProfile.toJson(profile));
        prefs.edit().putString(SEC_AHOY_PROFILE, AhoyProfile.toJson(profile)).apply();
    }

    public LiveData<Boolean> hasProfile(){
        return LiveDataReactiveStreams.fromPublisher(profileId().map(profileId ->
                prefs.contains(SEC_AHOY_PROFILE)).toFlowable());
    }

    private Single<Integer> profileId(){
        int profileId = prefs.getInt(SEC_PROFILE_ID, 0);
        if (prefs.contains(SEC_PROFILE_ID) && profileId>0){
            return Single.just(profileId);
        }
        return generateProfile().map(profile -> profile.uid);
    }

    private Single<Profile> generateProfile(){
        String password = generatePassword();
        return ProfileRepository.putProfile(Profile.of(deviceId, password)).map(profile -> {
            prefs.edit().putInt(SEC_PROFILE_ID,profile.uid).apply();
            return profile;
        });
    }

    private String generatePassword(){
        String password = random.passwords().strong().get();
        prefs.edit().putString(SEC_PROFILE_SECRET, password).apply();
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}
