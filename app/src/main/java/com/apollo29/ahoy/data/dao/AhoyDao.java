package com.apollo29.ahoy.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.comm.queue.Queue;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface AhoyDao {

    // Event region

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable putEvent(Event event);

    @Transaction
    @Query("SELECT * FROM event WHERE uid = :eventId ")
    Single<Event> getEvent(Integer eventId);

    @Transaction
    @Query("SELECT * FROM event WHERE profile_id = :profileId ORDER BY date DESC")
    Flowable<List<Event>> getEventsByProfileId(Integer profileId);

    @Transaction
    @Query("SELECT * FROM event WHERE active = 1 AND profile_id = :profileId AND date BETWEEN :startOfDay AND :endOfDay ORDER BY date")
    Single<List<Event>> getCurrentEvent(Integer profileId, Long startOfDay, Long endOfDay);

    @Transaction
    @Query("SELECT * FROM event WHERE active = 1 AND profile_id = :profileId AND date <= :fromDay ORDER BY date")
    Single<List<Event>> getEventsToClear(Integer profileId, Long fromDay);

    @Transaction
    @Query("UPDATE event SET active = 0 WHERE uid IN (:events)")
    Completable inactivateEvents(List<Integer> events);

    // endregion

    // Queue region

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable putQueue(Queue... queue);

    @Transaction
    @Query("SELECT * FROM queue WHERE event_id = :eventId ")
    Single<List<Queue>> getQueuesByEventId(Integer eventId);

    @Query("DELETE FROM queue WHERE uid = :uid")
    Completable removeQueue(int uid);

    @Query("DELETE FROM queue WHERE event_id IN (:events)")
    Completable removeQueues(List<Integer> events);

    // endregion
}
