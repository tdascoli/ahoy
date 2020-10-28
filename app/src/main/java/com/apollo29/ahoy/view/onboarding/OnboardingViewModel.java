package com.apollo29.ahoy.view.onboarding;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.apollo29.ahoy.comm.profile.Profile;
import com.apollo29.ahoy.data.AhoyProfile;
import com.apollo29.ahoy.repository.MainRepository;
import com.apollo29.ahoy.view.profile.ProfileDataViewModel;
import com.orhanobut.logger.Logger;

import net.andreinc.mockneat.MockNeat;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.reactivex.rxjava3.core.Single;

public class OnboardingViewModel extends ProfileDataViewModel {

    private final MainRepository mainRepository;
    private final MockNeat random = MockNeat.secure();

    public OnboardingViewModel(@NonNull Application application) {
        super(application);
        this.mainRepository = new MainRepository(getApplication());
    }

    public void storeProfile(){
        AhoyProfile profile = new AhoyProfile(
                firstname.getValue(),
                lastname.getValue(),
                address.getValue(),
                timestamp.getValue(),
                mobile.getValue(),
                email.getValue());
        Logger.d(profile.toString());
        Logger.d("AHOY PROFILE %s", AhoyProfile.toJson(profile));
        mainRepository.putAhoyProfile(profile);
    }

    public LiveData<Boolean> hasProfile(){
        return LiveDataReactiveStreams.fromPublisher(profileId().map(profileId ->
                mainRepository.hasAhoyProfile()).toFlowable());
    }

    private Single<Integer> profileId(){
        if (mainRepository.hasAhoyProfile()){
            return Single.just(mainRepository.profileId());
        }
        return generateProfile().map(profile -> profile.uid);
    }

    private Single<Profile> generateProfile(){
        String password = generatePassword();
        return MainRepository.putProfile(Profile.of(mainRepository.deviceId(), password)).map(profile -> {
            mainRepository.putProfileId(profile.uid);
            return profile;
        });
    }

    private String generatePassword(){
        String password = random.passwords().strong().get();
        mainRepository.putProfileSecret(password);
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}
