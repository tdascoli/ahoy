package com.apollo29.ahoy.view.home;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.apollo29.ahoy.AhoyApplication;
import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.data.repository.DatabaseRepository;
import com.apollo29.ahoy.repository.ProfileRepository;

import java.util.Optional;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class HomeViewModel extends AndroidViewModel {

    private final DatabaseRepository databaseRepository;
    private final ProfileRepository profileRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(getApplication());
        databaseRepository = ((AhoyApplication) application).getRepository();
    }

    public LiveData<Optional<Event>> currentEvent(){
        return LiveDataReactiveStreams.fromPublisher(databaseRepository.getCurrentEvent(profileRepository.profileId()).toFlowable());
    }

    public LiveData<Boolean> hasEvent(){
        return LiveDataReactiveStreams.fromPublisher(databaseRepository.hasEvent(profileRepository.profileId()).toFlowable());
    }

    // todo refactor with only zxing
    public Bitmap qrcode(String inputValue) {
        QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, 500);
        return qrgEncoder.getBitmap();
    }

}
