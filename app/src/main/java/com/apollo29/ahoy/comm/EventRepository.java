package com.apollo29.ahoy.comm;

import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.comm.event.EventService;

import java.util.Optional;

import io.reactivex.rxjava3.core.Single;

public class EventRepository {

    public static Single<Event> putEvent(String authToken, Event event){
        EventService service = RetrofitClientInstance.getRetrofitInstance().create(EventService.class);
        return service.putEvent(authToken, event);
    }

    public static Single<Optional<Event>> getEvent(String authToken, String eventId){
        EventService service = RetrofitClientInstance.getRetrofitInstance().create(EventService.class);
        return service.getEvent(authToken, eventId).defaultIfEmpty(Event.empty()).map(Optional::of);
    }
}
