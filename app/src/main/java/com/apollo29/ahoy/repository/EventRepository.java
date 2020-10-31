package com.apollo29.ahoy.repository;

import com.apollo29.ahoy.BuildConfig;
import com.apollo29.ahoy.comm.RetrofitClientInstance;
import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.comm.event.EventLight;
import com.apollo29.ahoy.comm.event.EventService;
import com.orhanobut.logger.Logger;

import io.reactivex.rxjava3.core.Single;

public class EventRepository {

    public static Single<Event> putEvent(String authToken, Event event){
        EventService service = RetrofitClientInstance.getRetrofitInstance().create(EventService.class);
        return service.putEvent(authToken, event)
                .doOnError(throwable -> Logger.w("Error on put Event %s", throwable));
    }

    public static Single<Event> getEvent(String authToken, int eventId){
        EventService service = RetrofitClientInstance.getRetrofitInstance().create(EventService.class);
        return service.getEvent(authToken, eventId).defaultIfEmpty(Event.empty())
                .doOnError(throwable -> Logger.w("Error on get Event %s", throwable));
    }

    public static Single<EventLight> getEventLight(int eventId){
        String apikey = BuildConfig.apikey;
        EventService service = RetrofitClientInstance.getRetrofitInstance().create(EventService.class);
        return service.getEventLight(apikey, eventId).defaultIfEmpty(EventLight.empty())
                .doOnError(throwable -> Logger.w("Error on get Event Light %s", throwable));
    }
}
