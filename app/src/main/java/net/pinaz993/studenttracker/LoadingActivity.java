package net.pinaz993.studenttracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent nextActivity;
        final String CLASS_ID_KEY = getString(R.string.class_id_key);
        final String STUDENT_ID_KEY = getString((R.string.student_id_key));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Context context = getApplicationContext();

        DatabaseHandler dbh = new DatabaseHandler(context, null);
        dbh.sendForDB();

        //Make sure the settings are good to go. Anywhere else in the application, use SettingsHandler.getInstance()
        SettingsHandler settings = new SettingsHandler(context);

        // Is this the first time the app has been run?
        if(settings.isFirstTimeLaunch()){ //Yes
            settings.setAttendanceMode("default"); // It's not in the list, so the default will be used.
            settings.setIsFirstTimeLaunch(false); // It's not the first time anymore.
            nextActivity = new Intent(this, ClassListActivity.class);
            //The above is to satisfy the IDE. Change to startup activity when it is implemented.
            //TODO: Implement setup activity
        } else { //No
            Class activityRelaunch;
            String classID = null;
            String studentID = null;
            switch (settings.getLastActivityRun()) {
                case NONE:
                    //class picker dialog will be launched when ClassListActivity
                    // doesn't receive a valid CLASS_ID in the extras.
                    activityRelaunch = ClassListActivity.class;
                    break;
//                case SETTINGS:
//                    //set activityRelaunch to SettingsActivity.class
                //TODO: Implement settings activity
//                    break;
                case CLASS_LIST:
                    activityRelaunch = ClassListActivity.class;
                    classID = settings.getLastClassID();
                    break;
//                case CLASS_EDITOR:
//                    // set activityRelaunch to ClassEditorActivity.class
                //TODO: Implement Class Editor Activity
//                    break;
//                case STUDENT_STATS:
//                    studentID = settings.getLastStudentID();
//                    // set activityRelaunch to StudentStatsActivity.class
                //TODO: Implement student statistics activity.
//                    break;
                default:
                    //class picker dialog will be launched when ClassListActivity
                    // doesn't receive a valid CLASS_ID in the extras.
                    activityRelaunch = ClassListActivity.class;
            }
            nextActivity = new Intent(LoadingActivity.this, activityRelaunch);
            if (classID != null) nextActivity.putExtra(CLASS_ID_KEY, classID);
            if(studentID != null) nextActivity.putExtra(STUDENT_ID_KEY, studentID);
        }
        dbh.grabDB();
        startActivity(nextActivity);
    }
}
