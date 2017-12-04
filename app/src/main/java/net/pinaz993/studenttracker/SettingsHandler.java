package net.pinaz993.studenttracker;

import android.content.Context;
import android.content.SharedPreferences;

import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Handles the settings for the app. Uses SharedPreferences to do so.
 * Created by Patrick Shannon on 10/11/2017.
 */

public class SettingsHandler {
    private static SettingsHandler instance;
    private final String attendanceMode;
    private Duration attendanceIntervalDuration;
    private boolean daily = false;
    private boolean weekly = false;
    private final SharedPreferences settings;

    private final String ATTENDANCE_MODE_DAILY;
    private final String ATTENDANCE_MODE_WEEKLY;
    private final String ATTENDANCE_MODE_DEFAULT;
    private final String ATTENDANCE_MODE_KEY;

    private final String LAST_ACTIVITY_RUN_KEY;
    private final String LAST_CLASS_ID_KEY;
    private final String LAST_STUDENT_ID_KEY;

    private final String IS_FIRST_TIME_LAUNCH;

    public enum  ACTIVITY {
        NONE,
        SETTINGS,
        CLASS_LIST,
        STUDENT_STATS,
        CLASS_EDITOR
    }

    public SettingsHandler(Context context) {
        settings = context.getSharedPreferences(
                context.getString(R.string.settings_key), Context.MODE_PRIVATE);
        ATTENDANCE_MODE_DAILY = context.getString(R.string.attendance_mode_daily);
        ATTENDANCE_MODE_WEEKLY = context.getString(R.string.attendance_mode_weekly);
        ATTENDANCE_MODE_DEFAULT = ATTENDANCE_MODE_DAILY;
        ATTENDANCE_MODE_KEY = context.getString(R.string.attendance_mode_key);
        IS_FIRST_TIME_LAUNCH = context.getString(R.string.is_first_time_launch);
        LAST_ACTIVITY_RUN_KEY = context.getString(R.string.last_activity_run);
        LAST_CLASS_ID_KEY = context.getString(R.string.last_class_id);
        LAST_STUDENT_ID_KEY = context.getString(R.string.last_student_id);

        attendanceMode = settings.getString(ATTENDANCE_MODE_KEY, ATTENDANCE_MODE_DEFAULT);
        setAttendanceIntervalDuration(context);

        //<editor-fold desc="Dirty Demi-Singleton Nonsense">
        String EXCEPTION_MESSAGE = "Application tried to instantiate a second SettingsHandler instance.";
        if((instance != this) && (instance != null)) throw new IllegalStateException(EXCEPTION_MESSAGE);
        instance = this; // UGH! This is even dirtier than a singleton!
        //</editor-fold>
    }

    public static SettingsHandler getInstance() {
        return instance;
    }

    public ArrayList<String> getAttendanceModeChoices(){
        ArrayList r =  new ArrayList<String>();
         r.add(ATTENDANCE_MODE_DAILY);
         r.add(ATTENDANCE_MODE_WEEKLY);
         return r;
    }

    public void setAttendanceMode(String s){
        SharedPreferences.Editor editor = settings.edit();
        if(getAttendanceModeChoices().contains(s)) {
            editor.putString(ATTENDANCE_MODE_KEY, s);
            editor.apply();
        }
        else {
            editor.putString(ATTENDANCE_MODE_KEY, ATTENDANCE_MODE_DEFAULT);
            editor.apply();
        }
    }

    public void setAttendanceIntervalDuration(Context context) {
        SharedPreferences.Editor editor = settings.edit();
        if(Objects.equals(attendanceMode, context.getString(R.string.attendance_mode_daily))) {
            attendanceIntervalDuration = new Duration(86400000); //Number of milliseconds in a day
            daily = true;
            weekly = false;
        }
        else if (Objects.equals(attendanceMode, context.getString(R.string.attendance_mode_weekly))) {
            attendanceIntervalDuration = new Duration(604800000); // Number of milliseconds in a week.
            weekly = true;
            daily = false;
        }
        else {
            attendanceIntervalDuration = new Duration(86400000); //Number of milliseconds in a day
            daily = true;
            weekly = false;
            editor.putString(ATTENDANCE_MODE_KEY, ATTENDANCE_MODE_DEFAULT);
            editor.apply();
        }
    }

    public boolean isFirstTimeLaunch() {
        // By default, assume this is the first time the app is being launched.
        return settings.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setIsFirstTimeLaunch(boolean b){
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, b);
        editor.apply();
    }

    public boolean isDaily() {
        return daily;
    }

    public boolean isWeekly() {
        return weekly;
    }

    public Duration getAttendanceIntervalDuration() {
        return attendanceIntervalDuration;
    }

    public ACTIVITY getLastActivityRun() {
        return ACTIVITY.values()[settings.getInt(LAST_ACTIVITY_RUN_KEY, 0)];
    }

    public void setLastActivityRun(ACTIVITY lastActivityRun) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(LAST_ACTIVITY_RUN_KEY, lastActivityRun.ordinal());
        editor.apply();
    }

    public String getLastClassID() {
        return settings.getString(LAST_CLASS_ID_KEY, "");
    }

    public void setLastClassID(String lastClassID) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(LAST_CLASS_ID_KEY, lastClassID);
        editor.apply();
    }

    public String getLastStudentID() {
        return settings.getString(LAST_STUDENT_ID_KEY, "");
    }

    public void setLastStudentID(String lastStudentID) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(LAST_STUDENT_ID_KEY, lastStudentID);
        editor.apply();
    }
}
