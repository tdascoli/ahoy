package com.apollo29.ahoy;

import android.app.Application;

import com.apollo29.ahoy.data.AhoyDatabase;
import com.apollo29.ahoy.data.repository.DatabaseRepository;

public class AhoyApplication extends Application {

    public AhoyDatabase getDatabase() {
        return AhoyDatabase.getInstance(this);
    }

    public DatabaseRepository getRepository() {
        return DatabaseRepository.getInstance(getDatabase());
    }
}
