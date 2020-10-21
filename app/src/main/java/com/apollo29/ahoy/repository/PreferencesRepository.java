package com.apollo29.ahoy.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;

class PreferencesRepository {

    private static final String SEC_PREF = "ahoy_preferences";

    static final String SEC_AHOY_PROFILE = "profile";
    static final String SEC_AUTH_TOKEN = "auth_token";
    static final String SEC_PROFILE_ID = "profile_id";
    static final String SEC_PROFILE_SECRET = "profile_password";
    static final String SEC_PROFILE_SECRET_EMPTY = "NONE";

    public static SharedPreferences prefs(Context context) {
        try {
            String masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            return EncryptedSharedPreferences.create(
                    SEC_PREF,
                    masterKey,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException | IOException e) {
            Logger.w("Failed initialize SharedPreferences: %s", e);
            return PreferenceManager.getDefaultSharedPreferences(context);
        }
    }


}
