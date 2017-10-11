package net.pinaz993.studenttracker;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.joda.time.Duration;

import java.util.Objects;

/**
 * Handles the settings for the app. Uses SharedPreferences to do so.
 * Created by Patrick Shannon on 10/11/2017.
 */

class SettingsHandler extends Application {
    private static final SettingsHandler ourInstance = new SettingsHandler();
    SharedPreferences settings;
    protected Duration attendanceIntervalDuration;
    protected boolean daily = false;
    protected boolean weekly = false;

    static SettingsHandler get() {
        return ourInstance;
    }

    private SettingsHandler() {
        settings = getApplicationContext().getSharedPreferences(
                getString(R.string.settings_key), Context.MODE_PRIVATE);
        String defaultAttendanceMode = getString(R.string.attendance_mode_daily);
        String attendanceModeKey = getString(R.string.attendance_mode_key);
        String attendanceMode = settings.getString(attendanceModeKey, defaultAttendanceMode);
        if(Objects.equals(attendanceMode, getString(R.string.attendance_mode_daily))) {
            attendanceIntervalDuration = new Duration(86400000); //Number of milliseconds in a day
            daily = true;
        }
        else if (Objects.equals(attendanceMode, getString(R.string.attendance_mode_weekly))) {
            attendanceIntervalDuration = new Duration(604800000);
            weekly = false;
        }




    }
}
