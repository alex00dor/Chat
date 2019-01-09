package com.kiko.chat.domain.util;

import android.annotation.SuppressLint;
import android.icu.util.Calendar;

import java.text.SimpleDateFormat;

public class DateUtil {
    @SuppressLint("SimpleDateFormat")
    static public String getDateString(Long millis){

        String timePatternCurrentDay = "hh:mm aaa";
        String timePatternForDay = "E";
        String timePatternForOther = "MM/dd/yyyy";

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mMonth = calendar.get(Calendar.MONTH);
        int mYear = calendar.get(Calendar.YEAR);
        int mWeek = calendar.get(Calendar.WEEK_OF_MONTH);

        Calendar current = Calendar.getInstance();
        int cDay = current.get(Calendar.DAY_OF_MONTH);
        int cMonth = current.get(Calendar.MONTH);
        int cYear = current.get(Calendar.YEAR);
        int cWeek = current.get(Calendar.WEEK_OF_MONTH);

        if(mDay == cDay && mMonth == cMonth && mYear == cYear){
            return new SimpleDateFormat(timePatternCurrentDay).format(calendar.getTime());
        }

        if(mMonth == cMonth && mYear == cYear && mWeek == cWeek){
            return new SimpleDateFormat(timePatternForDay).format(calendar.getTime());
        }

        return new SimpleDateFormat(timePatternForOther).format(calendar.getTime());
    }
}
