package com.apollo29.ahoy.view.events;

import com.apollo29.ahoy.comm.event.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EventUtil {

    final static Calendar calendar = Calendar.getInstance();

    public static boolean isFuture(Event event){
        return new Date().before(dateToValidate(event.date));
    }

    public static boolean isCurrent(Event event){
        if (event.active==0){
            return false;
        }
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.HOUR, -24);
        return dateToValidate(event.date).after(yesterday.getTime());
    }

    public static boolean isDoneOrCurrent(Event event){
        if (event.active==0){
            return false;
        }
        return !isArchived(event) && !isFuture(event);
    }

    public static boolean isArchived(Event event){
        Calendar currentDateBefore14Days = Calendar.getInstance();
        currentDateBefore14Days.add(Calendar.DAY_OF_MONTH, -14);
        return event.active==0 || dateToValidate(event.date).before(currentDateBefore14Days.getTime());
    }

    public static String getDateString(Event event, SimpleDateFormat formatter){
        return formatter.format(dateToValidate(event.date));
    }

    private static Date dateToValidate(Long date){
        calendar.setTimeInMillis(TimeUnit.SECONDS.toMillis(date));
        return calendar.getTime();
    }
}
