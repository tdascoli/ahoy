package com.apollo29.ahoy.view.events.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.apollo29.ahoy.comm.event.EventLight;
import com.apollo29.ahoy.comm.queue.Queue;
import com.apollo29.ahoy.data.AhoyProfile;
import com.apollo29.ahoy.repository.EventRepository;
import com.apollo29.ahoy.repository.ProfileRepository;
import com.apollo29.ahoy.repository.QueueRepository;
import com.apollo29.ahoy.view.profile.ProfileDataViewModel;
import com.orhanobut.logger.Logger;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.processors.BehaviorProcessor;

public class RegisterViewModel extends ProfileDataViewModel {

    private final ProfileRepository profileRepository;
    private final BehaviorProcessor<Integer> eventId = BehaviorProcessor.createDefault(0);

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        this.profileRepository = new ProfileRepository(getApplication());
    }

    public LiveData<EventLight> getEvent(int eventId){
        return LiveDataReactiveStreams.fromPublisher(EventRepository.getEventLight(eventId).toFlowable());
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

    public void eventId(int eventId){
        this.eventId.onNext(eventId);
    }

    public LiveData<Queue> register(){
        Date date = new Date();
        Queue queue = new Queue(0,
                eventId.getValue(),
                firstname.getValue(),
                lastname.getValue(),
                address.getValue(),
                timestamp.getValue(),
                mobile.getValue(),
                email.getValue(),
                TimeUnit.MILLISECONDS.toSeconds(date.getTime()));
        return LiveDataReactiveStreams.fromPublisher(QueueRepository.putQueue(eventId.getValue(), queue).toFlowable());
    }
}
