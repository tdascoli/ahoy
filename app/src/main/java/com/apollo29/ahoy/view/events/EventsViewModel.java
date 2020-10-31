package com.apollo29.ahoy.view.events;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import com.apollo29.ahoy.AhoyApplication;
import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.comm.queue.Queue;
import com.apollo29.ahoy.data.repository.DatabaseRepository;
import com.apollo29.ahoy.repository.MainRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class EventsViewModel extends AndroidViewModel {

    private final MainRepository mainRepository;
    private final DatabaseRepository databaseRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public EventsViewModel(Application application) {
        super(application);
        mainRepository = new MainRepository(getApplication());
        databaseRepository = ((AhoyApplication) application).getRepository();
    }

    public LiveData<List<Event>> events(){
        int profileId = mainRepository.profileId();
        if (profileId!=0){
            return LiveDataReactiveStreams.fromPublisher(databaseRepository.getEventsByProfileId(profileId));
        }
        return new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Queue>> guests(int eventId){
        return databaseRepository.streamQueuesByEventId(eventId);
    }

    public LiveData<Boolean> refreshData(int eventId){
        return LiveDataReactiveStreams.fromPublisher(mainRepository.authToken().flatMap(authToken -> {
                    if (authToken.isPresent()){
                        return mainRepository.updateQueue(eventId).map(refreshing -> !refreshing);
                    }
                    return Single.just(false);
                }).toFlowable());
    }

    public LiveData<Event> loadEvent(int eventId){
        return LiveDataReactiveStreams.fromPublisher(databaseRepository.getEvent(eventId).toFlowable());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
