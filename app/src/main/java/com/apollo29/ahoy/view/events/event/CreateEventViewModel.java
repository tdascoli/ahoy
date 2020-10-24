package com.apollo29.ahoy.view.events.event;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import com.apollo29.ahoy.AhoyApplication;
import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.data.repository.DatabaseRepository;
import com.apollo29.ahoy.repository.AuthenticationRepository;
import com.apollo29.ahoy.repository.EventRepository;
import com.apollo29.ahoy.repository.ProfileRepository;
import com.orhanobut.logger.Logger;

import net.andreinc.mockneat.MockNeat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.processors.BehaviorProcessor;

public class CreateEventViewModel extends AndroidViewModel {

    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<String> date = new MutableLiveData<>();
    private final BehaviorProcessor<Long> timestamp = BehaviorProcessor.createDefault(0L);

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
    private final MockNeat random = MockNeat.secure();

    private final ProfileRepository profileRepository;
    private final DatabaseRepository databaseRepository;

    public CreateEventViewModel(Application application) {
        super(application);
        profileRepository = new ProfileRepository(getApplication());
        databaseRepository = ((AhoyApplication) application).getRepository();
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public MutableLiveData<String> getDate() {
        return date;
    }

    public void setDate(Long timestamp){
        Date date = new Date(timestamp);
        this.timestamp.onNext(TimeUnit.MILLISECONDS.toSeconds(timestamp));
        this.date.setValue(formatter.format(date));
    }

    public LiveData<Event> createEvent(){
        String title = this.title.getValue();
        int profileId = profileRepository.profileId();
        if (profileId!=0){
            Event event = Event.of(title, profileId, random.passwords().strong().get(), timestamp.getValue());
            return LiveDataReactiveStreams.fromPublisher(AuthenticationRepository.authToken(profileRepository.deviceId(), profileRepository.preferences()).flatMap(authToken -> {
                if (authToken.isPresent()) {
                    return EventRepository.putEvent(authToken.get(), event).flatMap(finalEvent ->
                            databaseRepository.putEvent(finalEvent).andThen(Single.just(finalEvent)));
                }
                Logger.w("NO AUTH TOKEN");
                return Single.just(Event.empty());
            }).toFlowable());
        }
        return new MutableLiveData<>(Event.empty());
    }
}
