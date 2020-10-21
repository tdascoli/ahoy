package com.apollo29.ahoy.view.onboarding;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.apollo29.ahoy.comm.profile.Profile;
import com.apollo29.ahoy.data.AhoyProfile;
import com.apollo29.ahoy.repository.ProfileRepository;
import com.apollo29.ahoy.view.profile.ProfileDataViewModel;
import com.orhanobut.logger.Logger;

import net.andreinc.mockneat.MockNeat;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.reactivex.rxjava3.core.Single;

public class OnboardingViewModel extends ProfileDataViewModel {

    private final ProfileRepository profileRepository;
    private final MockNeat random = MockNeat.secure();

    public OnboardingViewModel(@NonNull Application application) {
        super(application);
        this.profileRepository = new ProfileRepository(getApplication());
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
        profileRepository.putAhoyProfile(profile);
    }

    public LiveData<Boolean> hasProfile(){
        return LiveDataReactiveStreams.fromPublisher(profileId().map(profileId ->
                profileRepository.hasAhoyProfile()).toFlowable());
    }

    private Single<Integer> profileId(){
        if (profileRepository.hasAhoyProfile()){
            return Single.just(profileRepository.profileId());
        }
        return generateProfile().map(profile -> profile.uid);
    }

    private Single<Profile> generateProfile(){
        String password = generatePassword();
        return ProfileRepository.putProfile(Profile.of(profileRepository.deviceId(), password)).map(profile -> {
            profileRepository.putProfileId(profile.uid);
            return profile;
        });
    }

    private String generatePassword(){
        String password = random.passwords().strong().get();
        profileRepository.putProfileSecret(password);
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}
