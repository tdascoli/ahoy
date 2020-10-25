package com.apollo29.ahoy.view.profile;

import android.app.Application;

import androidx.annotation.NonNull;

import com.apollo29.ahoy.data.AhoyProfile;
import com.apollo29.ahoy.repository.ProfileRepository;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Single;

public class ProfileViewModel extends ProfileDataViewModel {

    private final ProfileRepository profileRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.profileRepository = new ProfileRepository(getApplication());
    }

    public void updateProfile(){
        AhoyProfile profile = new AhoyProfile(
                firstname.getValue(),
                lastname.getValue(),
                address.getValue(),
                timestamp.getValue(),
                mobile.getValue(),
                email.getValue());
        profileRepository.putAhoyProfile(profile);
    }

    public void loadProfile(){
        if (profileRepository.hasAhoyProfile()){
            String ahoyProfile = profileRepository.ahoyProfile();
            Logger.d(ahoyProfile);

            AhoyProfile profile = AhoyProfile.fromJson(ahoyProfile);
            firstname.setValue(profile.getFirstname());
            lastname.setValue(profile.getLastname());
            address.setValue(profile.getAddress());
            timestamp.onNext(profile.getBirthday());
            birthday.setValue(toDate(profile.getBirthday()));
            mobile.setValue(profile.getMobile());
            email.setValue(profile.getEmail());
        }
    }
}
