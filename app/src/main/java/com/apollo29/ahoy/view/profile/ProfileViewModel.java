package com.apollo29.ahoy.view.profile;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apollo29.ahoy.data.AhoyProfile;
import com.apollo29.ahoy.repository.PreferencesRepository;
import com.orhanobut.logger.Logger;

import static com.apollo29.ahoy.repository.PreferencesRepository.SEC_AHOY_PROFILE;

public class ProfileViewModel extends AndroidViewModel {

    private final MutableLiveData<String> firstname = new MutableLiveData<>();
    private final MutableLiveData<String> lastname = new MutableLiveData<>();
    private final MutableLiveData<String> address = new MutableLiveData<>();
    private final MutableLiveData<String> birthday = new MutableLiveData<>();
    private final MutableLiveData<String> mobile = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();

    private final SharedPreferences prefs;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.prefs = PreferencesRepository.prefs(getApplication().getApplicationContext());
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
        prefs.edit().putString(SEC_AHOY_PROFILE, profile.toJson()).apply();
    }

    public void loadProfile(){
        String ahoyProfile = prefs.getString(SEC_AHOY_PROFILE, "");
        Logger.d(ahoyProfile);
        if (prefs.contains(SEC_AHOY_PROFILE) && ahoyProfile!=null && !ahoyProfile.equals("")){
            AhoyProfile profile = AhoyProfile.fromJson(ahoyProfile);
            firstname.setValue(profile.getFirstname());
            lastname.setValue(profile.getLastname());
            address.setValue(profile.getAddress());
            birthday.setValue(profile.getBirthday());
            mobile.setValue(profile.getMobile());
            email.setValue(profile.getEmail());
        }
    }
}
