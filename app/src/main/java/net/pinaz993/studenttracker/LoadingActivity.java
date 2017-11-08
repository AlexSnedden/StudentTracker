package net.pinaz993.studenttracker;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    private Context context;
    private DatabaseHandler dbh = DatabaseHandler.getInstance();
    private SettingsHandler settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        context = getApplicationContext();
        dbh.sendForDB();
        //Make sure the settings are good to go. Anywhere else in the application, use SettingsHandler.getInstance()
        settings = new SettingsHandler(context);
        // Is this the first time the app has been run?
        if(settings.isFirstTimeLaunch()){
            settings.setAttendanceMode("default"); // It's not in the list, so the default will be used.
            settings.setIsFirstTimeLaunch(false); // It's not the first time anymore.
            // remember to start the setup activity
        } else {
            //If there's something we need to do during startup, but it can't be done on first launch,
            //this is where it goes.
        }

        // if this was the first time, launch the setup activity

        // else, grab the last activity that was run, or the first class list if that can't be found

        // Load up that activity.
    }
}
