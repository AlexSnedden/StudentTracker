package net.pinaz993.studenttracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
    private long attendanceIntervalLength;// Number of milliseconds in interval

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
                BehaviorRecordTableSchema.name + " (" +
                BehaviorRecordTableSchema.behaviorIdColDef +
                BehaviorRecordTableSchema.studentIdColDef +
                BehaviorRecordTableSchema.classIdColDef +
                BehaviorRecordTableSchema.timestampColDef +
                BehaviorRecordTableSchema.primaryKeyDef +
                BehaviorRecordTableSchema.behaviorIdAliasDef + ")";
        db.execSQL(createBehaviorRecordTable);
        String createBehaviorAliasTable = "CREATE TABLE " +
                BehaviorAliasTableSchema.name + " (" +
                BehaviorAliasTableSchema.behaviorIdColDef +
                BehaviorAliasTableSchema.behaviorNameColDef +
                BehaviorAliasTableSchema.positivityColDef + " )";
        db.execSQL(createBehaviorAliasTable);
        String createAttendanceRecordsTable = "CREATE TABLE " +
                AttendanceRecordsTableSchema.name + " (" +
                AttendanceRecordsTableSchema.studentIdColDef +
                AttendanceRecordsTableSchema.classIdColDef +
                AttendanceRecordsTableSchema.periodColDef +
                AttendanceRecordsTableSchema.presentColDef +
                AttendanceRecordsTableSchema.lateArrivalColDef +
                AttendanceRecordsTableSchema.earlyDepartureColDef +
                AttendanceRecordsTableSchema.excusedColDef +
                AttendanceRecordsTableSchema.primaryKeyDef + ")";
        db.execSQL(createAttendanceRecordsTable);
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
     * Grabs all attendance records from the current attendance period.
     * @param classID The class for which to grab the attendance record
     * @return a Cursor containing all the records collected
     */
    public Cursor getCurrentAttendanceRecords(String classID){
        int period = 0; //Set to beginning of current attendance interval
        //TODO: Implement attendance period using SharedPreferences
        String query = "SELECT * FROM " + AttendanceRecordsTableSchema.name +
                "WHERE " + AttendanceRecordsTableSchema.periodCol + " IS " +
                period + " AND " + AttendanceRecordsTableSchema.classIdCol +
                " IS " + classID;

        return db.rawQuery(query, null);
    }

    /**
     * Grabs all attendance records for the studentID provided.
     * @return Cursor containing all matching records
     */
    public Cursor getAttendanceRecordsForStudent(String studentID) {
        String colList = AttendanceRecordsTableSchema.classIdCol.concat(" ") +
                AttendanceRecordsTableSchema.periodCol.concat(" ") +
                AttendanceRecordsTableSchema.presentCol.concat(" ") +
                AttendanceRecordsTableSchema.lateArrivalCol.concat(" ") +
                AttendanceRecordsTableSchema.earlyDepartureCol.concat(" ") +
                AttendanceRecordsTableSchema.excusedCol;
        String query = "SELECT " + colList + " FROM " + AttendanceRecordsTableSchema.name +
                " WHERE " + AttendanceRecordsTableSchema.studentIdCol + " IS " + studentID +";";
        return db.rawQuery(query, null);
    }

    /**
     * Queries the database to see if a record exists to match the parameters.
     * @return Is there a record that matches?
     */
    public boolean attendanceRecordExists(String studentID, String classID, int period) {
        String[] columns = { AttendanceRecordsTableSchema.excusedCol };
        String selection = AttendanceRecordsTableSchema.studentIdCol + " =?" +
                AttendanceRecordsTableSchema.classIdCol + " =?" +
                AttendanceRecordsTableSchema.periodCol + " =?";
        String[] selectionArgs = {studentID, classID, Integer.toString(period)};
        Cursor c = db.query(AttendanceRecordsTableSchema.name, columns, selection, selectionArgs,
                null, null, null);
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    /**
     * Inserts a record in the attendance records table. Parameters are values to go into the table.
     */
    public void recordAttendance(String studentID, String classID, int period, boolean present,
                                 boolean lateArrival, boolean earlyDeparture, boolean excused) {
        ContentValues cVal = new ContentValues(6);
        cVal.put(AttendanceRecordsTableSchema.studentIdCol, studentID);
        cVal.put(AttendanceRecordsTableSchema.classIdCol, classID);
        cVal.put(AttendanceRecordsTableSchema.periodCol, period);
        cVal.put(AttendanceRecordsTableSchema.presentCol, present);
        cVal.put(AttendanceRecordsTableSchema.lateArrivalCol, lateArrival);
        cVal.put(AttendanceRecordsTableSchema.earlyDepartureCol, earlyDeparture);
        cVal.put(AttendanceRecordsTableSchema.excusedCol, excused);
        db.insertOrThrow(AttendanceRecordsTableSchema.name, null, cVal);
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
    public void updateAttendanceRecord(String studentID, String classID, int period, boolean present,
                                       boolean lateArrival, boolean earlyDeparture, boolean excused)
    {
        ContentValues cVal = new ContentValues(6);
        cVal.put(AttendanceRecordsTableSchema.presentCol, present);
        cVal.put(AttendanceRecordsTableSchema.lateArrivalCol, lateArrival);
        cVal.put(AttendanceRecordsTableSchema.earlyDepartureCol, earlyDeparture);
        cVal.put(AttendanceRecordsTableSchema.excusedCol, excused);
        String where = AttendanceRecordsTableSchema.studentIdCol + " IS" + studentID +
                " AND " + AttendanceRecordsTableSchema.classIdCol + " IS" + classID +
                " AND " + AttendanceRecordsTableSchema.periodCol + "IS " + period;
        db.update(AttendanceRecordsTableSchema.name, cVal, where, null);
    }

    /**
     * Deletes an attendance record from the database. Params are all part of the composite key.
     */
    public void deleteAttendanceRecord(String studentID, String classID, int period) {
        String where = AttendanceRecordsTableSchema.studentIdCol + " IS" + studentID +
                " AND " + AttendanceRecordsTableSchema.classIdCol + " IS" + classID +
                " AND " + AttendanceRecordsTableSchema.periodCol + "IS " + period;
        db.delete(AttendanceRecordsTableSchema.name, where, null);
    }

    private long getAttendanceIntervalLength() {
        return attendanceIntervalLength;
    }
    //</editor-fold>

    //<editor-fold desc="Schema Classes">
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
        public static final String periodColDef = "period INTEGER NOT NULL,";
        public static final String presentColDef = "present INTEGER NOT NULL CHECK(present IN(0, 1)),";
        public static final String lateArrivalColDef = "lateArrival INTEGER NOT NULL CHECK(lateArrival IN(0, 1) AND present IS 1),";
        public static final String earlyDepartureColDef = "earlyDeparture INTEGER NOT NULL CHECK(earlyDeparture IN(0, 1) AND present IS 1),";
        public static final String excusedColDef = "excused INTEGER NOT NULL CHECK(excused IN(0, 1)),";
        public static final String primaryKeyDef = "PRIMARY KEY(studentID, classID, period)";

        public static final String studentIdCol = "studentID";
        public static final String classIdCol = "classID";
        public static final String periodCol = "period";
        public static final String presentCol = "present";
        public static final String lateArrivalCol = "lateArrival";
        public static final String earlyDepartureCol = "earlyDeparture";
        public static final String excusedCol = "excused";
    }
    //</editor-fold>
}
