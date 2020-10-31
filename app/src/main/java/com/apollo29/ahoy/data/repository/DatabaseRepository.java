package com.apollo29.ahoy.data.repository;

import androidx.lifecycle.LiveData;

import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.comm.queue.Queue;
import com.apollo29.ahoy.data.AhoyDatabase;
import com.orhanobut.logger.Logger;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DatabaseRepository {

    private static DatabaseRepository instance;

    private final AhoyDatabase database;

    private DatabaseRepository(final AhoyDatabase database) {
        this.database = database;
    }

    public static DatabaseRepository getInstance(final AhoyDatabase database) {
        if (instance == null) {
            synchronized (DatabaseRepository.class) {
                if (instance == null) {
                    instance = new DatabaseRepository(database);
                }
            }
        }
        return instance;
    }

    // Event region

    public Completable putEvent(Event event){
        return database.ahoyDao().putEvent(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Event> getEvent(Integer eventId){
        return database.ahoyDao().getEvent(eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<Event>> getEventsByProfileId(Integer profileId){
        return database.ahoyDao().getEventsByProfileId(profileId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> hasEvent(Integer profileId) {
        LocalDateTime now = LocalDateTime.now();
        Long startOfDay = now.with(LocalTime.MIN).toEpochSecond(ZoneOffset.UTC); // start of a day // T00:00
        Long endOfDay = now.with(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC); // end of a day // T23:59:59.999999999
        return database.ahoyDao().getCurrentEvent(profileId, startOfDay, endOfDay).map(List::isEmpty)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Optional<Event>> getCurrentEvent(Integer profileId) {
        Optional<Event> empty = Optional.empty();
        LocalDateTime now = LocalDateTime.now();
        Long startOfDay = now.with(LocalTime.MIN).toEpochSecond(ZoneOffset.UTC); // start of a day // T00:00
        Long endOfDay = now.with(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC); // end of a day // T23:59:59.999999999
        return database.ahoyDao().getCurrentEvent(profileId, startOfDay, endOfDay).map(events -> {
                    if (events.isEmpty()){
                        return empty;
                    }
                    if (events.size()>1){
                        Logger.d("More than One current Event.");
                    }
                    return Optional.of(events.get(0));
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Event>> getEventsToClear(Integer profileId) {
        LocalDateTime now = LocalDateTime.now();
        Long fromDay = now.minusDays(15).with(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);

        Logger.d("SELECT * FROM event WHERE active = 1 AND profile_id = %s AND date <= %s ORDER BY date", profileId, fromDay);

        return database.ahoyDao().getEventsToClear(profileId, fromDay)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable inactivateEvents(List<Integer> events) {
        return database.ahoyDao().removeQueues(events)
                .andThen(database.ahoyDao().inactivateEvents(events))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // endregion

    // Queue region

    public Completable putQueue(Queue... queue){
        return database.ahoyDao().putQueue(queue)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Queue>> getQueuesByEventId(Integer eventId){
        return database.ahoyDao().getQueuesByEventId(eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public LiveData<List<Queue>> streamQueuesByEventId(Integer eventId){
        return database.ahoyDao().streamQueuesByEventId(eventId);
    }

    // endregion
}
