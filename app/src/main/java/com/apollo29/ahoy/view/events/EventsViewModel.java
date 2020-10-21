package com.apollo29.ahoy.view.events;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import com.apollo29.ahoy.AhoyApplication;
import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.data.repository.DatabaseRepository;
import com.apollo29.ahoy.repository.ProfileRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventsViewModel extends AndroidViewModel {

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);

    private final ProfileRepository profileRepository;
    private final DatabaseRepository databaseRepository;

    public EventsViewModel(Application application) {
        super(application);
        profileRepository = new ProfileRepository(getApplication());
        databaseRepository = ((AhoyApplication) application).getRepository();
    }

    public LiveData<List<Event>> events(){
        int profileId = profileRepository.profileId();
        if (profileId!=0){
            return LiveDataReactiveStreams.fromPublisher(databaseRepository.getEventsByProfileId(profileId));
        }
        return new MutableLiveData<>(new ArrayList<>());
    }
}
