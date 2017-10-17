package net.pinaz993.studenttracker;

import android.app.Application;
import android.content.Context;

/**
 * Created by Alexander Snedden on 10/17/17.
 */

public class MyApplication extends Application {
    public static DatabaseHandler dbHandler;
    private static Context context;

    public void onCreate() {
        dbHandler = new DatabaseHandler(getApplicationContext(), null);
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}