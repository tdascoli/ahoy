package com.apollo29.ahoy.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.apollo29.ahoy.comm.event.Event;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface AhoyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable putEvent(Event event);

    @Transaction
    @Query("SELECT * FROM event WHERE uid = :eventId ")
    Single<Event> getEvent(Integer eventId);

    @Transaction
    @Query("SELECT * FROM event WHERE profile_id = :profileId ")
    Flowable<List<Event>> getEventsByProfileId(Integer profileId);

    @Transaction
    @Query("SELECT * FROM event WHERE profile_id = :profileId AND date BETWEEN :startOfDay AND :endOfDay ORDER BY date")
    Single<List<Event>> getCurrentEvent(Integer profileId, Long startOfDay, Long endOfDay);
}
