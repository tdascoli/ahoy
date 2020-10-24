package com.apollo29.ahoy.view.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.rxjava3.processors.BehaviorProcessor;

public abstract class ProfileDataViewModel extends AndroidViewModel {

    protected final MutableLiveData<String> firstname = new MutableLiveData<>();
    protected final MutableLiveData<String> lastname = new MutableLiveData<>();
    protected final MutableLiveData<String> address = new MutableLiveData<>();
    protected final MutableLiveData<String> birthday = new MutableLiveData<>();
    protected final MutableLiveData<String> mobile = new MutableLiveData<>();
    protected final MutableLiveData<String> email = new MutableLiveData<>();

    public final BehaviorProcessor<Long> timestamp = BehaviorProcessor.createDefault(0L);
    protected final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);

    public ProfileDataViewModel(@NonNull Application application) {
        super(application);
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

    public void setDate(Long timestamp){
        this.timestamp.onNext(timestamp);
        Logger.d("TIME %s", timestamp);
        this.birthday.setValue(toDate(timestamp));
    }

    protected String toDate(Long timestamp){
        Date date = new Date(timestamp);
        return formatter.format(date);
    }
}
