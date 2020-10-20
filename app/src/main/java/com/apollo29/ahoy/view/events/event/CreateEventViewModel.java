package com.apollo29.ahoy.view.events.event;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.provider.Settings;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import com.apollo29.ahoy.comm.EventRepository;
import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.repository.AuthenticationRepository;
import com.apollo29.ahoy.repository.PreferencesRepository;
import com.orhanobut.logger.Logger;

import io.reactivex.rxjava3.core.Single;

import static com.apollo29.ahoy.repository.PreferencesRepository.SEC_PROFILE_ID;

public class CreateEventViewModel extends AndroidViewModel {

    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<String> date = new MutableLiveData<>();
    private final SharedPreferences prefs;
    private final String deviceId;

    @SuppressLint("HardwareIds")
    public CreateEventViewModel(Application application) {
        super(application);
        prefs = PreferencesRepository.prefs(getApplication());
        deviceId = Settings.Secure.getString(application.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public MutableLiveData<String> getDate() {
        return date;
    }

    public LiveData<Event> createEvent(){
        String title = this.title.getValue();
        String date = this.date.getValue();
        int profileId = prefs.getInt(SEC_PROFILE_ID, 0);
        if (profileId!=0){
            // todo finalize
            Event event = Event.of(title, profileId, "secret", 0);

            return LiveDataReactiveStreams.fromPublisher(AuthenticationRepository.authToken(deviceId, prefs).flatMap(authToken -> {
                if (authToken.isPresent()) {
                    return EventRepository.putEvent(authToken.get(), event);
                }
                Logger.w("NO AUTH TOKEN");
                return Single.just(Event.empty());
            }).toFlowable());
        }
        return new MutableLiveData<>(Event.empty());
    }
}
