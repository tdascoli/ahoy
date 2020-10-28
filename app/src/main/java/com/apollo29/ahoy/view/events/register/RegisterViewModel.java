package com.apollo29.ahoy.view.events.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.apollo29.ahoy.AhoyApplication;
import com.apollo29.ahoy.comm.event.EventLight;
import com.apollo29.ahoy.comm.queue.Queue;
import com.apollo29.ahoy.data.AhoyProfile;
import com.apollo29.ahoy.data.repository.DatabaseRepository;
import com.apollo29.ahoy.repository.EventRepository;
import com.apollo29.ahoy.repository.MainRepository;
import com.apollo29.ahoy.repository.QueueRepository;
import com.apollo29.ahoy.view.profile.ProfileDataViewModel;
import com.orhanobut.logger.Logger;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.processors.BehaviorProcessor;

public class RegisterViewModel extends ProfileDataViewModel {

    private final MainRepository mainRepository;
    private final DatabaseRepository databaseRepository;
    private final BehaviorProcessor<Integer> eventId = BehaviorProcessor.createDefault(0);

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        mainRepository = new MainRepository(getApplication());
        databaseRepository = ((AhoyApplication) getApplication()).getRepository();
    }

    public LiveData<EventLight> getEvent(int eventId, boolean local){
        return LiveDataReactiveStreams.fromPublisher(event(eventId, local));
    }

    private Flowable<EventLight> event(int eventId, boolean local){
        if (local){
            return databaseRepository.getEvent(eventId).map(event ->
                    new EventLight(event.uid, event.title, event.date)).toFlowable();
        }
        return EventRepository.getEventLight(eventId).toFlowable();
    }

    public void loadProfile(){
        if (mainRepository.hasAhoyProfile()){
            String ahoyProfile = mainRepository.ahoyProfile();
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

    public void eventId(int eventId){
        this.eventId.onNext(eventId);
    }

    public LiveData<Boolean> register(boolean registerManually){
        Date date = new Date();
        Queue queue = new Queue(null,
                eventId.getValue(),
                firstname.getValue(),
                lastname.getValue(),
                address.getValue(),
                timestamp.getValue(),
                mobile.getValue(),
                email.getValue(),
                TimeUnit.MILLISECONDS.toSeconds(date.getTime()));
        if (registerManually){
            return LiveDataReactiveStreams.fromPublisher(databaseRepository.putQueue(queue).andThen(Flowable.just(true)));
        }
        return LiveDataReactiveStreams.fromPublisher(QueueRepository.putQueue(eventId.getValue(), queue).toFlowable().map(q -> true));
    }
}
