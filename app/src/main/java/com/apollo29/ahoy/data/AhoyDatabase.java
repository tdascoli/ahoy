package com.apollo29.ahoy.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.apollo29.ahoy.BuildConfig;
import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.comm.queue.LocalQueue;
import com.apollo29.ahoy.comm.queue.Queue;
import com.apollo29.ahoy.data.converter.DateConverter;
import com.apollo29.ahoy.data.dao.AhoyDao;
import com.apollo29.ahoy.repository.PreferencesRepository;

import net.andreinc.mockneat.MockNeat;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import static com.apollo29.ahoy.repository.PreferencesRepository.SEC_DB_SECRET;

@Database(entities = {
            Event.class,
            Queue.class,
            LocalQueue.class
        },
        version = AhoyDatabase.VERSION)
@TypeConverters(DateConverter.class)
public abstract class AhoyDatabase extends RoomDatabase {

    static final int VERSION = 4;

    private static final String PASSPHRASE = BuildConfig.dbsecret;

    private static AhoyDatabase instance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "ahoy.db";

    public abstract AhoyDao ahoyDao();

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

    // todo remove but check first passphrase
    private static String passphrase(final Context context){
        final MockNeat random = MockNeat.secure();
        String secret = random.passwords().strong().get();
        SharedPreferences sharedPreferences = PreferencesRepository.prefs(context);
        if (!sharedPreferences.contains(SEC_DB_SECRET)){
            sharedPreferences.edit().putString(SEC_DB_SECRET, secret).apply();
        }
        return sharedPreferences.getString(SEC_DB_SECRET, secret);
    }
}
