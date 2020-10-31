package com.apollo29.ahoy.view.scanning;

import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QRCodeUtil {

    private final static Pattern EVENT_FROM_URL = Pattern.compile("([^/]+$)", Pattern.CASE_INSENSITIVE);

    public static boolean isValidUrl(String qrcode){
        return qrcode.startsWith("https://apollo29.com/ahoy/event/");
    }

    @Nullable
    public static String eventFromUrl(String url){
        Matcher matcher = EVENT_FROM_URL.matcher(url);
        if (matcher.find()){
            return matcher.group();
        }
        return null;
    }
}
