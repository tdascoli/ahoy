package com.apollo29.ahoy.view.profile;

import android.app.Application;

import androidx.annotation.NonNull;

import com.apollo29.ahoy.data.AhoyProfile;
import com.apollo29.ahoy.repository.MainRepository;

public class ProfileViewModel extends ProfileDataViewModel {

    private final MainRepository mainRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.mainRepository = new MainRepository(getApplication());
    }

    public void updateProfile(){
        AhoyProfile profile = new AhoyProfile(
                firstname.getValue(),
                lastname.getValue(),
                address.getValue(),
                timestamp.getValue(),
                mobile.getValue(),
                email.getValue());
        mainRepository.putAhoyProfile(profile);
    }

    public void loadProfile(){
        if (mainRepository.hasAhoyProfile()){
            String ahoyProfile = mainRepository.ahoyProfile();
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
