package com.apollo29.ahoy.view.events.download;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.apollo29.ahoy.AhoyApplication;
import com.apollo29.ahoy.comm.queue.Queue;
import com.apollo29.ahoy.data.repository.DatabaseRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DownloadViewModel extends AndroidViewModel {

    private final DatabaseRepository databaseRepository;

    public DownloadViewModel(Application application) {
        super(application);
        databaseRepository = ((AhoyApplication) application).getRepository();
    }

    public LiveData<List<String[]>> guestlist(int eventId){
        return LiveDataReactiveStreams.fromPublisher(databaseRepository.getQueuesByEventId(eventId).map(queues ->
                queues.stream().map(Queue::asArray).collect(Collectors.toList())).toFlowable());
    }
}
