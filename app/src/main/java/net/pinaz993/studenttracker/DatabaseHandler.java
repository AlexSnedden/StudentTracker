package net.pinaz993.studenttracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.ExecutionException;

/**
 * Handles all database operations for the app.
 * Created by Patrick Shannon on 10/3/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "StudentTracking.db";
    public static final int DATABASE_VERSION = 1; //See this.onUpgrade
    private SQLiteDatabase db;
    private Context context;
    private AsyncTask task;

    //<editor-fold desc="Constructors and init()">
    public DatabaseHandler(Context context, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
        init();
    }

    public DatabaseHandler(Context context, @Nullable SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION, errorHandler);
        this.context = context;
        init();
    }

    public void init() {
    }
    //</editor-fold>

    //<editor-fold desc="Send For and then Grab DB">
    public void sendForDB() {
        task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] dbh) {
                return null;
            }
            protected SQLiteDatabase doInBackground(DatabaseHandler dbh) {
                return dbh.getWritableDatabase();
            }
        };
        task.execute(this);
    }

    public void grabDB() {
        try {
            db = (SQLiteDatabase) task.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d("DB_ERROR", "The database could not be retrieved.", e);
        }
    }
    //</editor-fold>

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBehaviorRecordTable = "CREATE TABLE " +
                BehaviorRecordTableSchema.NAME + " (" +
                BehaviorRecordTableSchema.BEHAVIOR_ID_COL_DEF +
                BehaviorRecordTableSchema.STUDENT_ID_COL_DEF +
                BehaviorRecordTableSchema.CLASS_ID_COL_DEF +
                BehaviorRecordTableSchema.TIMESTAMP_COL_DEF +
                BehaviorRecordTableSchema.PRIMARY_KEY_DEF +
                BehaviorRecordTableSchema.BEHAVIOR_ID_ALIAS_DEF + ")";
        db.execSQL(createBehaviorRecordTable);
        String createBehaviorAliasTable = "CREATE TABLE " +
                BehaviorAliasTableSchema.NAME + " (" +
                BehaviorAliasTableSchema.BEHAVIOR_ID_COL_DEF +
                BehaviorAliasTableSchema.BEHAVIOR_NAME_COL_DEF +
                BehaviorAliasTableSchema.POSITIVITY_COL_DEF + " )";
        db.execSQL(createBehaviorAliasTable);
        String createAttendanceRecordsTable = "CREATE TABLE " +
                AttendanceRecordsTableSchema.NAME + " (" +
                AttendanceRecordsTableSchema.STUDENT_ID_COL_DEF +
                AttendanceRecordsTableSchema.CLASS_ID_COL_DEF +
                AttendanceRecordsTableSchema.INTERVAL_COL_DEF +
                AttendanceRecordsTableSchema.PRESENT_COL_DEF +
                AttendanceRecordsTableSchema.LATE_ARRIVAL_COL_DEF +
                AttendanceRecordsTableSchema.EARLY_DEPARTURE_COL_DEF +
                AttendanceRecordsTableSchema.EXCUSED_COL_DEF +
                AttendanceRecordsTableSchema.PRIMARY_KEY_DEF + ")";
        db.execSQL(createAttendanceRecordsTable);
        String createStudentClassMappingTable = "CREATE TABLE " +
                StudentClassMappingTableSchema.NAME + " (" +
                StudentClassMappingTableSchema.CLASS_ID_COL_DEF +
                StudentClassMappingTableSchema.STUDENT_ID_COL_DEF +
                StudentClassMappingTableSchema.PRIMARY_KEY_DEF +")";
        db.execSQL(createStudentClassMappingTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // This method is used to change the database around if ever the Schema is changed.
        // If you change the schema, you MUST update the version number. If there's something that
        // needs to be done when an old database is updated to reflect the new schema, this is where
        // you put it.
        // Right now, there is nothing that needs done, so it sits empty.
    }

    /**
     * Completely resets the database to factory defaults
     */
    public void resetDatabase(){
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if(c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String name = c.getString(c.getColumnIndex("name"));
                db.execSQL("DROP TABLE IF EXISTS" + name);
            }
        }
        c.close();
        onCreate(db);
    }

    //<editor-fold desc="Attendance Record Handling">
    /**
     * Grabs all attendance records from the current attendance interval.
     * @param classID The class for which to grab the attendance record
     * @return a Cursor containing all the records collected
     */
    public Cursor getCurrentAttendanceRecords(String classID){
        long period = AttendanceInterval.getCurrentStart();
        String query = "SELECT * FROM " + AttendanceRecordsTableSchema.NAME +
                "WHERE " + AttendanceRecordsTableSchema.INTERVAL_COL + " IS " +
                period + " AND " + AttendanceRecordsTableSchema.CLASS_ID_COL +
                " IS " + classID;
        return db.rawQuery(query, null);
    }

    /**
     * Grabs all attendance records for the studentID provided.
     * @return Cursor containing all matching records
     */
    public Cursor getAttendanceRecordsForStudent(String studentID) {
        String colList = AttendanceRecordsTableSchema.CLASS_ID_COL.concat(" ") +
                AttendanceRecordsTableSchema.INTERVAL_COL.concat(" ") +
                AttendanceRecordsTableSchema.PRESENT_COL.concat(" ") +
                AttendanceRecordsTableSchema.LATE_ARRIVAL_COL.concat(" ") +
                AttendanceRecordsTableSchema.EARLY_DEPARTURE_COL.concat(" ") +
                AttendanceRecordsTableSchema.EXCUSED_COL;
        String query = "SELECT " + colList + " FROM " + AttendanceRecordsTableSchema.NAME +
                " WHERE " + AttendanceRecordsTableSchema.STUDENT_ID_COL + " IS " + studentID +";";
        return db.rawQuery(query, null);
    }

    /**
     * Queries the database to see if a record exists to match the parameters.
     * @return Is there a record that matches?
     */
    public boolean attendanceRecordExists(String studentID, String classID, long interval) {
        String[] columns = { AttendanceRecordsTableSchema.EXCUSED_COL};
        String selection = AttendanceRecordsTableSchema.STUDENT_ID_COL + " =?" +
                AttendanceRecordsTableSchema.CLASS_ID_COL + " =?" +
                AttendanceRecordsTableSchema.INTERVAL_COL + " =?";
        String[] selectionArgs = {studentID, classID, Long.toString(interval)};
        Cursor c = db.query(AttendanceRecordsTableSchema.NAME, columns, selection, selectionArgs,
                null, null, null);
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    /**
     * Inserts a record in the attendance records table. Parameters are values to go into the table.
     */
    public void recordAttendance(String studentID, String classID, long interval, boolean present,
                                 boolean lateArrival, boolean earlyDeparture, boolean excused) {
        ContentValues cVal = new ContentValues(6);
        cVal.put(AttendanceRecordsTableSchema.STUDENT_ID_COL, studentID);
        cVal.put(AttendanceRecordsTableSchema.CLASS_ID_COL, classID);
        cVal.put(AttendanceRecordsTableSchema.INTERVAL_COL, interval);
        cVal.put(AttendanceRecordsTableSchema.PRESENT_COL, present);
        cVal.put(AttendanceRecordsTableSchema.LATE_ARRIVAL_COL, lateArrival);
        cVal.put(AttendanceRecordsTableSchema.EARLY_DEPARTURE_COL, earlyDeparture);
        cVal.put(AttendanceRecordsTableSchema.EXCUSED_COL, excused);
        db.insertOrThrow(AttendanceRecordsTableSchema.NAME, null, cVal);
    }

    /**
     * Uses the params provided to change a row in the table. Changes only the row where the
     * studentID, classID, and period are the same.
     * @param studentID value for studentID. Doesn't change.
     * @param classID value for classID. Doesn't change.
     * @param period value for period. Doesn't change.
     * @param lateArrival new value for lateArrival
     * @param earlyDeparture new value for earlyDeparture
     * @param excused new value for excused
     */
    public void updateAttendanceRecord(String studentID, String classID, long period, boolean present,
                                       boolean lateArrival, boolean earlyDeparture, boolean excused)
    {
        ContentValues cVal = new ContentValues(6);
        cVal.put(AttendanceRecordsTableSchema.PRESENT_COL, present);
        cVal.put(AttendanceRecordsTableSchema.LATE_ARRIVAL_COL, lateArrival);
        cVal.put(AttendanceRecordsTableSchema.EARLY_DEPARTURE_COL, earlyDeparture);
        cVal.put(AttendanceRecordsTableSchema.EXCUSED_COL, excused);
        String where = AttendanceRecordsTableSchema.STUDENT_ID_COL + " IS" + studentID +
                " AND " + AttendanceRecordsTableSchema.CLASS_ID_COL + " IS" + classID +
                " AND " + AttendanceRecordsTableSchema.INTERVAL_COL + "IS " + period;
        db.update(AttendanceRecordsTableSchema.NAME, cVal, where, null);
    }

    /**
     * Deletes an attendance record from the database. Params are all part of the composite key.
     */
    public void deleteAttendanceRecord(String studentID, String classID, long period) {
        String where = AttendanceRecordsTableSchema.STUDENT_ID_COL + " IS" + studentID +
                " AND " + AttendanceRecordsTableSchema.CLASS_ID_COL + " IS" + classID +
                " AND " + AttendanceRecordsTableSchema.INTERVAL_COL + "IS " + period;
        db.delete(AttendanceRecordsTableSchema.NAME, where, null);
    }
    //</editor-fold>

    //<editor-fold desc="Schema Classes">
    private static class BehaviorRecordTableSchema{
        public static final String NAME = "BehaviorRecords";

        public static final String BEHAVIOR_ID_COL_DEF = "behaviorID INTEGER NOT NULL,";
        public static final String STUDENT_ID_COL_DEF = "studentID TEXT NOT NULL,";
        public static final String CLASS_ID_COL_DEF = "classID TEXT NOT NULL,"; //Included because it might be useful to know when a student has different behaviors in different classes.
        public static final String TIMESTAMP_COL_DEF = "timestamp INTEGER NOT NULL,"; //use something that returns UNIX time in milliseconds
        public static final String PRIMARY_KEY_DEF = "PRIMARY KEY (behaviorID, studentID, timestamp),"; //Why, yes, I do need all of these columns. Don't question me.
        public static final String BEHAVIOR_ID_ALIAS_DEF = "FOREIGN KEY(behaviorID) REFERENCES behaviorAlias(behaviorID);";
        
        public static final String BEHAVIOR_ID_COL = "behaviorID";
        public static final String STUDENT_ID_COL = "studentID";
        public static final String CLASS_ID_COL = "classID";
        public static final String TIMESTAMP_COL = "timestamp";
    }

    private static class BehaviorAliasTableSchema{
        public static final String NAME = "BehaviorAlias";

        public static final String BEHAVIOR_ID_COL_DEF = "behaviorID INTEGER NOT NULL,";
        public static final String BEHAVIOR_NAME_COL_DEF = "behaviorName TEXT,";
        public static final String POSITIVITY_COL_DEF = "positivity INTEGER DEFAULT 0 CHECK(positivity IN(-1,0,1));";

        public static final String BEHAVIOR_ID_COL = "behaviorID";
        public static final String BEHAVIOR_NAME_COL = "behaviorName";
        public static final String POSITIVITY_COL = "positivity";
    }
    
    private static class AttendanceRecordsTableSchema{
        public static final String NAME = "AttendanceRecords";

        public static final String STUDENT_ID_COL_DEF = "studentID TEXT NOT NULL,";
        public static final String CLASS_ID_COL_DEF = "classID TEXT NOT NULL,";
        public static final String INTERVAL_COL_DEF = "interval INTEGER NOT NULL,";
        public static final String PRESENT_COL_DEF = "present INTEGER NOT NULL CHECK(present IN(0, 1)),";
        public static final String LATE_ARRIVAL_COL_DEF = "lateArrival INTEGER NOT NULL CHECK(lateArrival IN(0, 1) AND present IS 1),";
        public static final String EARLY_DEPARTURE_COL_DEF = "earlyDeparture INTEGER NOT NULL CHECK(earlyDeparture IN(0, 1) AND present IS 1),";
        public static final String EXCUSED_COL_DEF = "excused INTEGER NOT NULL CHECK(excused IN(0, 1)),";
        public static final String PRIMARY_KEY_DEF = "PRIMARY KEY(studentID, classID, period)";

        public static final String STUDENT_ID_COL = "studentID";
        public static final String CLASS_ID_COL = "classID";
        public static final String INTERVAL_COL = "interval";
        public static final String PRESENT_COL = "present";
        public static final String LATE_ARRIVAL_COL = "lateArrival";
        public static final String EARLY_DEPARTURE_COL = "earlyDeparture";
        public static final String EXCUSED_COL = "excused";
    }

    private static class StudentClassMappingTableSchema{
        public static final String NAME = "StudentClassMap";

        public static final String STUDENT_ID_COL_DEF = "studentID TEXT NOT NULL,";
        public static final String CLASS_ID_COL_DEF = "classID TEXT NOT NULL,";
        public static final String PRIMARY_KEY_DEF = "PRIMARY KEY(studentID, classID)";

        public static final String STUDENT_ID_COL = "studentID";
        public static final String CLASS_ID_COL = "classID";
    }

    private static class StudentTableSchema{
        public static final String NAME = "Students";

        public static final String STUDENT_ID_COL_DEF = "studentID TEXT PRIMARY KEY,";
        public static final String FIRST_NAME_COL_DEF = "firstName TEXT NOT NULL,";
        public static final String LAST_NAME_COL_DEF = "lastName TEXT NOT NULL,";
        public static final String EMAIL_COL_DEF = "email TEXT";

        public static final String STUDENT_ID_COL = "studentID";
        public static final String FIRST_NAME_COL = "firstName";
        public static final String LAST_NAME_COL = "lastName";
        public static final String EMAIL_COL = "email";
    }
    //</editor-fold>
}
