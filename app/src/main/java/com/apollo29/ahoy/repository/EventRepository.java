package com.apollo29.ahoy.repository;

import com.apollo29.ahoy.comm.RetrofitClientInstance;
import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.comm.event.EventLight;
import com.apollo29.ahoy.comm.event.EventService;

import io.reactivex.rxjava3.core.Single;

public class EventRepository {

    public static Single<Event> putEvent(String authToken, Event event){
        EventService service = RetrofitClientInstance.getRetrofitInstance().create(EventService.class);
        return service.putEvent(authToken, event);
    }

    public static Single<Event> getEvent(String authToken, int eventId){
        EventService service = RetrofitClientInstance.getRetrofitInstance().create(EventService.class);
        return service.getEvent(authToken, eventId).defaultIfEmpty(Event.empty());
    }

    public static Single<EventLight> getEventLight(int eventId){
        EventService service = RetrofitClientInstance.getRetrofitInstance().create(EventService.class);
        return service.getEventLight(eventId).defaultIfEmpty(EventLight.empty());
    }
}
