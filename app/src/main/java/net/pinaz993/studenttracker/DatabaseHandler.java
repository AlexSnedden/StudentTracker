package net.pinaz993.studenttracker;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.*;
/**
 * Handles all database operations for the app.
 * Created by Patrick Shannon on 10/3/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper{

    private static final String databaseName = "StudentTracking"

    public DatabaseHandler(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, databaseName, factory, version);
    }

    public DatabaseHandler(Context context, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, databaseName, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private static class BehaviorRecordTableSchema{
        public static final String name = "BehaviorRecords";

        public static final String behaviorIdColDef = "behaviorID INTEGER NOT NULL,";
        public static final String studentIdColDef = "studentID TEXT NOT NULL,";
        public static final String classIdColDef = "classID TEXT NOT NULL,"; //Included because it might be useful to know when a student has different behaviors in different classes.
        public static final String timestampColDef = "timestamp INTEGER NOT NULL,"; //use something that returns UNIX time in milliseconds
        public static final String primaryKeyDef = "PRIMARY KEY (behaviorID, studentID, timestamp),"; //Why, yes, I do need all of these columns. Don't question me.
        public static final String behaviorIdAliasDef = "FOREIGN KEY(behaviorID) REFERENCES behaviorAlias(behaviorID);";
        
        public static final String behaviorIdCol = "behaviorID";
        public static final String studentIdCol = "studentID";
        public static final String classIdCol = "classID";
        public static final String timestampCol = "timestamp";
    }

    private static class BehaviorAliasTableSchema{
        public static final String name = "BehaviorAlias";

        public static final String behaviorIdColDef = "behaviorID INTEGER NOT NULL,";
        public static final String behaviorNameColDef = "behaviorName TEXT,";
        public static final String positivityColDef = "positivity INTEGER DEFAULT 0 CHECK(positivity IN(-1,0,1));";

        public static final String behaviorIdCol = "behaviorID";
        public static final String behaviorNameCol = "behaviorName";
        public static final String positivityCol = "positivity";
    }
    
    private static class AttendanceRecordsTableSchema{
        public static final String name = "AttendanceRecords";

        public static final String studentIdColDef = "studentID TEXT NOT NULL,";
        public static final String classIdColDef = "classID TEXT NOT NULL,";
        public static final String timestampColDef = "timestamp INTEGER NOT NULL,";
        public static final String presentColDef = "present INTEGER NOT NULL CHECK(present IN(0, 1)),";
        public static final String lateArrivalColDef = "lateArrival INTEGER NOT NULL CHECK(lateArrival IN(0, 1) AND present IS 1),";
        public static final String earlyDepartureColDef = "earlyDeparture INTEGER NOT NULL CHECK(earlyDeparture IN(0, 1) AND present IS 1),";
        public static final String excusedColDef = "excused INTEGER NOT NULL CHECK(excused IN(0, 1)),";
        public static final String primaryKeyDef = "PRIMARY KEY(studentID, classID, timestamp)";

        public static final String studentIdCol = "studentID";
        public static final String classIdCol = "classID";
        public static final String timestampCol = "timestamp";
        public static final String presentCol = "present";
        public static final String lateArrivalCol = "lateArrival";
        public static final String earlyDepartureCol = "earlyDeparture";
        public static final String excusedCol = "excused";
    }

    private String createBehaviorRecordTable = "CREATE TABLE " +
            BehaviorRecordTableSchema.name + " (" +
            BehaviorRecordTableSchema.behaviorIdColDef +
            BehaviorRecordTableSchema.studentIdColDef +
            BehaviorRecordTableSchema.classIdColDef +
            BehaviorRecordTableSchema.timestampColDef +
            BehaviorRecordTableSchema.primaryKeyDef+
            BehaviorRecordTableSchema.behaviorIdAliasDef + ")";

    private String createBehaviorAliasTable = "CREATE TABLE " +
            BehaviorAliasTableSchema.name + " (" +
            BehaviorAliasTableSchema.behaviorIdColDef +
            BehaviorAliasTableSchema.behaviorNameColDef +
            BehaviorAliasTableSchema.positivityColDef + " )"
}
