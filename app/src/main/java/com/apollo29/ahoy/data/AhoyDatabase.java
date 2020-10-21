package com.apollo29.ahoy.data;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.apollo29.ahoy.BuildConfig;
import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.data.converter.DateConverter;
import com.apollo29.ahoy.data.dao.AhoyDao;

import net.andreinc.mockneat.MockNeat;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

@Database(entities = {
            Event.class
        },
        exportSchema = false,
        version = AhoyDatabase.VERSION)
@TypeConverters(DateConverter.class)
public abstract class AhoyDatabase extends RoomDatabase {

    static final int VERSION = 1;
    // todo change
    private static final String PASSPHRASE = "test_passphrase";

    private static AhoyDatabase instance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "ahoy.db";

    public abstract AhoyDao ahoyDao();

    private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();

    public static AhoyDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AhoyDatabase.class) {
                if (instance == null) {
                    if (BuildConfig.DEBUG) {
                        instance = buildDatabase(context.getApplicationContext());
                    }
                    else {
                        instance = buildSecuredDatabase(context.getApplicationContext());
                    }
                }
            }
        }
        return instance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AhoyDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, AhoyDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    private static AhoyDatabase buildSecuredDatabase(final Context appContext) {
        final byte[] passphrase = SQLiteDatabase.getBytes(PASSPHRASE.toCharArray());
        final SupportFactory factory = new SupportFactory(passphrase);
        return Room.databaseBuilder(appContext, AhoyDatabase.class, DATABASE_NAME)
                .openHelperFactory(factory)
                .build();
    }
}
