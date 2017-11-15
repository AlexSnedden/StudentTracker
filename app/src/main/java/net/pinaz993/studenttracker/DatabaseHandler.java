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

import org.joda.time.Instant;

import java.util.concurrent.ExecutionException;

/**
 * Handles all database operations for the app.
 * Created by Patrick Shannon on 10/3/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper{

    public static DatabaseHandler instance = null;

    public static final String DATABASE_NAME = "StudentTracking.db";
    public static final String IS_THAT = " =?";
    public static final String IS_THAT_AND = IS_THAT + " AND ";
    public static final int DATABASE_VERSION = 1; //See this.onUpgrade
    private SQLiteDatabase db;
    private AsyncDatabaseFetcher task;

    public static DatabaseHandler getInstance() {
        return instance;
        // The dirty deed is done. DatabaseHandler is now a demi-singleton.
        }

    //<editor-fold desc="Constructors">
    public DatabaseHandler(Context context, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        String EXCEPTION_MESSAGE = "Application tried to instantiate a second DatabaseHandler instance.";
        if((instance != this) && (instance != null)) throw new IllegalStateException(EXCEPTION_MESSAGE);
        instance = this; // UGH! This is even dirtier than a singleton!
    }

    private DatabaseHandler(@Nullable SQLiteDatabase.CursorFactory factory,
                           DatabaseErrorHandler errorHandler) {
        super(null, DATABASE_NAME, factory, DATABASE_VERSION, errorHandler);
    }
    //</editor-fold>

    //<editor-fold desc="Send For and then Grab DB">
    public void sendForDB() {
        task = new AsyncDatabaseFetcher();
        task.execute(this);
    }

    public void grabDB() {
        try {
            db = (SQLiteDatabase) task.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d("DB_ERROR", "The database could not be retrieved.", e);
        }

        Log.d("DB", db.toString());
    }

    public void getDatabaseManually(){
        db = getWritableDatabase();
    }
    //</editor-fold>

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStudentTable = "CREATE TABLE " +
            StudentTableSchema.NAME + " (" +
            StudentTableSchema.STUDENT_ID_COL_DEF +
            StudentTableSchema.FIRST_NAME_COL_DEF +
            StudentTableSchema.LAST_NAME_COL_DEF +
            StudentTableSchema.EMAIL_COL_DEF +")";
        db.execSQL(createStudentTable);
        String createStudentClassMappingTable = "CREATE TABLE " +
                StudentClassMappingTableSchema.NAME + " (" +
                StudentClassMappingTableSchema.CLASS_ID_COL_DEF +
                StudentClassMappingTableSchema.STUDENT_ID_COL_DEF +
                StudentClassMappingTableSchema.PRIMARY_KEY_DEF +
                StudentClassMappingTableSchema.STUDENT_ID_FOREIGN_KEY_DEF + ")";
        db.execSQL(createStudentClassMappingTable);
        String createBehaviorRecordTable = "CREATE TABLE " +
                BehaviorRecordTableSchema.NAME + " (" +
                BehaviorRecordTableSchema.BEHAVIOR_ID_COL_DEF +
                BehaviorRecordTableSchema.STUDENT_ID_COL_DEF +
                BehaviorRecordTableSchema.CLASS_ID_COL_DEF +
                BehaviorRecordTableSchema.TIMESTAMP_COL_DEF +
                BehaviorRecordTableSchema.PRIMARY_KEY_DEF +
                BehaviorRecordTableSchema.BEHAVIOR_ID_ALIAS_DEF +
                BehaviorRecordTableSchema.STUDENT_ID_FOREIGN_KEY_DEF + ")";
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
                AttendanceRecordsTableSchema.PRIMARY_KEY_DEF +
                AttendanceRecordsTableSchema.STUDENT_ID_FOREIGN_KEY_DEF + ")";
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

    //<editor-fold desc="Student Record Handling">
    public void addStudent(String studentID, String firstName,
                              String lastName, @Nullable String email) {
        ContentValues values = new ContentValues();
        values.put(StudentTableSchema.STUDENT_ID_COL, studentID);
        values.put(StudentTableSchema.FIRST_NAME_COL, firstName);
        values.put(StudentTableSchema.LAST_NAME_COL, lastName);
        if(email!=null) values.put(StudentTableSchema.EMAIL_COL, email);
        db.insert(StudentTableSchema.NAME, null, values);
    }

    public boolean studentExists(String studentID) {
        String selectString = StudentTableSchema.STUDENT_ID_COL + IS_THAT;
        Cursor c = db.query(StudentTableSchema.NAME,
                new String[]{StudentTableSchema.STUDENT_ID_COL}, selectString,
                new String[] {studentID}, null, null, null);
        boolean rtnval = c.moveToFirst();
        c.close();
        return rtnval;
    }

    public Student retrieveStudent(String studentID) {
        String where = StudentTableSchema.STUDENT_ID_COL + IS_THAT;
        String[] colList = new String[] {StudentTableSchema.STUDENT_ID_COL,
                StudentTableSchema.FIRST_NAME_COL,
                StudentTableSchema.LAST_NAME_COL,
                StudentTableSchema.EMAIL_COL};
        Cursor c = db.query(StudentTableSchema.NAME, null, where, new String[] {studentID},
                null, null, null);
        c.moveToFirst();
        String firstName = c.getString(c.getColumnIndex(StudentTableSchema.FIRST_NAME_COL));
        String lastName = c.getString(c.getColumnIndex(StudentTableSchema.LAST_NAME_COL));
        String email;
        if(!c.isNull(c.getColumnIndex(StudentTableSchema.EMAIL_COL))){
            email = c.getString(c.getColumnIndex(StudentTableSchema.EMAIL_COL));
        } else {
            email = null;
        }
        c.close();
        return new Student(firstName, lastName, studentID, email);
    }

    public void updateStudent(String studentID, @Nullable String firstName,
                              @Nullable String lastName, @Nullable String email){
        ContentValues cv = new ContentValues();
        String where = StudentTableSchema.STUDENT_ID_COL +IS_THAT;
        if(firstName != null) cv.put(StudentTableSchema.FIRST_NAME_COL, firstName);
        if(lastName != null) cv.put(StudentTableSchema.LAST_NAME_COL, lastName);
        if(email != null) cv.put(StudentTableSchema.EMAIL_COL, email);
        if(cv.size() != 0) db.update(StudentTableSchema.NAME, cv, where, new String[]{studentID});
    }

    public void deleteStudent(String studentID){
        String where = StudentTableSchema.STUDENT_ID_COL + IS_THAT;
        db.delete(StudentTableSchema.NAME, where,new String[]{studentID});
    }
    //</editor-fold>

    //<editor-fold desc="Attendance Record Handling">
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
        db.insert(AttendanceRecordsTableSchema.NAME, null, cVal);
    }

    /**
     * Uses the params provided to change a row in the table. Changes only the row where the
     * studentID, classID, and period are as specified.
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
        ContentValues cVal = new ContentValues(4);
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
        String where = AttendanceRecordsTableSchema.STUDENT_ID_COL + " IS " + studentID +
                " AND " + AttendanceRecordsTableSchema.CLASS_ID_COL + " IS " + classID +
                " AND " + AttendanceRecordsTableSchema.INTERVAL_COL + "IS " + period;
        db.delete(AttendanceRecordsTableSchema.NAME, where, null);
    }

    /**
     * Grabs all attendance records from the current attendance interval.
     * @param classID The class for which to grab the attendance record
     * @return a Cursor containing all the records collected
     */
    public Cursor getCurrentAttendanceRecordsForClass(String classID, SettingsHandler settings){
        Long interval = AttendanceInterval.getCurrentStart();
        String[] colList ={AttendanceRecordsTableSchema.STUDENT_ID_COL,
                AttendanceRecordsTableSchema.PRESENT_COL,
                AttendanceRecordsTableSchema.LATE_ARRIVAL_COL,
                AttendanceRecordsTableSchema.EARLY_DEPARTURE_COL,
                AttendanceRecordsTableSchema.EXCUSED_COL};
        String where = AttendanceRecordsTableSchema.CLASS_ID_COL + IS_THAT_AND +
                AttendanceRecordsTableSchema.INTERVAL_COL + IS_THAT;
        String[] wheres = {classID, interval.toString()};
        return db.query(AttendanceRecordsTableSchema.NAME, colList, where, wheres, null, null,
                AttendanceRecordsTableSchema.INTERVAL_COL, null);
    }

    /**
     * Grabs all attendance records for the studentID provided.
     * @return Cursor containing all matching records
     */
    public Cursor getAttendanceRecordsForStudent(String studentID) {
        String[] colList = {AttendanceRecordsTableSchema.CLASS_ID_COL,
                AttendanceRecordsTableSchema.INTERVAL_COL,
                AttendanceRecordsTableSchema.PRESENT_COL,
                AttendanceRecordsTableSchema.LATE_ARRIVAL_COL,
                AttendanceRecordsTableSchema.EARLY_DEPARTURE_COL,
                AttendanceRecordsTableSchema.EXCUSED_COL};
        String where = AttendanceRecordsTableSchema.STUDENT_ID_COL + IS_THAT;
        return db.query(AttendanceRecordsTableSchema.NAME, colList, where, new String[] {studentID},
                null, null, AttendanceRecordsTableSchema.INTERVAL_COL);
    }

    /**
     * Quries the database for all record that pertain to the given student and class.
     * @return The resulting cursor, or null if no records are found.
     */
    public Cursor getAttendanceRecordsForStudentInClass(String studentID, String classID) {
        String[] colList = {
                AttendanceRecordsTableSchema.INTERVAL_COL,
                AttendanceRecordsTableSchema.PRESENT_COL,
                AttendanceRecordsTableSchema.LATE_ARRIVAL_COL,
                AttendanceRecordsTableSchema.EARLY_DEPARTURE_COL,
                AttendanceRecordsTableSchema.EXCUSED_COL};
        String where = AttendanceRecordsTableSchema.STUDENT_ID_COL + IS_THAT_AND +
                AttendanceRecordsTableSchema.CLASS_ID_COL + IS_THAT;
        String[] args = {studentID, classID};
        Cursor c = db.query(AttendanceRecordsTableSchema.NAME, colList, where, args, null,
                null, AttendanceRecordsTableSchema.INTERVAL_COL);
        if(c.moveToFirst()) return c;
        else return null;
    }

    /**
     * Queries the database for (what should be the only) current student record for a specific class.
     * Returns a the resulting Cursor, or null if the record does not exist.
     * @param studentID The id of the student to query for
     * @param classID The ID of the class ot query for
     * @return A cursor holding the record, or null if the record doesn't exist.
     */
    public Cursor getCurrentAttendanceRecordForStudentInClass(String studentID, String classID){
        long interval = AttendanceInterval.getCurrentStart();
        String[] colList = {
                AttendanceRecordsTableSchema.PRESENT_COL,
                AttendanceRecordsTableSchema.LATE_ARRIVAL_COL,
                AttendanceRecordsTableSchema.EARLY_DEPARTURE_COL,
                AttendanceRecordsTableSchema.EXCUSED_COL
        };
        String where = AttendanceRecordsTableSchema.STUDENT_ID_COL + IS_THAT_AND +
                AttendanceRecordsTableSchema.CLASS_ID_COL + IS_THAT_AND +
                AttendanceRecordsTableSchema.INTERVAL_COL + IS_THAT;
        String[] args = {studentID, classID, Long.toString(interval)};
        Cursor c = db.query(AttendanceRecordsTableSchema.NAME, colList, where, args,
                null, null, null);
        if (c.moveToFirst()) return c;
        else return null;
    }

    /**
     * Queries the database to see if a record exists to match the parameters.
     * @return Is there a record that matches?
     */
    public boolean attendanceRecordExists(String studentID, String classID, long interval) {
        String[] columns = { AttendanceRecordsTableSchema.EXCUSED_COL};
        String selection = AttendanceRecordsTableSchema.STUDENT_ID_COL + IS_THAT_AND +
                AttendanceRecordsTableSchema.CLASS_ID_COL + IS_THAT_AND +
                AttendanceRecordsTableSchema.INTERVAL_COL + IS_THAT;
        String[] selectionArgs = {studentID, classID, Long.toString(interval)};
        Cursor c = db.query(AttendanceRecordsTableSchema.NAME, columns, selection, selectionArgs,
                null, null, null);
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }
    //</editor-fold>

    //<editor-fold desc="Behavior Record Handling">
    public void recordStudentBehavior(String studentID, String behaviorID,
                                      String classID, long timestamp) {
        ContentValues cval = new ContentValues(3);
        cval.put(BehaviorRecordTableSchema.STUDENT_ID_COL, studentID);
        cval.put(BehaviorRecordTableSchema.BEHAVIOR_ID_COL, behaviorID);
        cval.put(BehaviorRecordTableSchema.CLASS_ID_COL, classID);
        cval.put(BehaviorRecordTableSchema.TIMESTAMP_COL, timestamp);
        db.insert(BehaviorRecordTableSchema.NAME, null, cval);
    }

    public void recordStudentBehaviorNow(String studentID, String behaviorID, String classID) {
        recordStudentBehavior(studentID, behaviorID, classID, new Instant().getMillis());
    }

    public void deleteBehaviorRecord(String studentID, String behaviorID, long timestamp) {
        String where = BehaviorRecordTableSchema.STUDENT_ID_COL.concat(" IS ") + studentID +
                " AND " + BehaviorRecordTableSchema.BEHAVIOR_ID_COL.concat(" IS ") + behaviorID +
                " AND " + BehaviorRecordTableSchema.TIMESTAMP_COL.compareTo(" IS ") + timestamp;
        db.delete(BehaviorRecordTableSchema.NAME, where, null);
    }

    public Cursor getStudentBehaviorRecords(String studentID){
        String[] colList = {BehaviorRecordTableSchema.CLASS_ID_COL,
                BehaviorRecordTableSchema.BEHAVIOR_ID_COL};
        String where = BehaviorRecordTableSchema.STUDENT_ID_COL + " = ?";
        return db.query(BehaviorRecordTableSchema.NAME,
                colList, where, new String[]{studentID}, null ,null,
                BehaviorRecordTableSchema.CLASS_ID_COL);
    }

    public Cursor getClassBehaviorRecords(String classId){
        String[] colLost = {BehaviorRecordTableSchema.STUDENT_ID_COL,
                BehaviorRecordTableSchema.BEHAVIOR_ID_COL};
        String where = BehaviorRecordTableSchema.CLASS_ID_COL + " = ?";
        return db.query(BehaviorRecordTableSchema.NAME,
                colLost, where, new String[]{classId}, null, null,
                BehaviorRecordTableSchema.STUDENT_ID_COL);
    }
    //</editor-fold>

    //<editor-fold desc="Behavior Alias Handling">
    public void addNewBehavior(String behaviorName, int positivity){
        ContentValues cval = new ContentValues(2);
        cval.put(BehaviorAliasTableSchema.BEHAVIOR_NAME_COL, behaviorName);
        cval.put(BehaviorAliasTableSchema.POSITIVITY_COL, positivity);
        db.insert(BehaviorAliasTableSchema.NAME, null, cval);
    }

    public void updateBehavior(int behaviorID, @Nullable String behaviorName, int positivity){
        ContentValues cval = new ContentValues();
        String where = BehaviorAliasTableSchema.BEHAVIOR_ID_COL + " = ?";
        if (behaviorName != null) cval.put(BehaviorAliasTableSchema.BEHAVIOR_NAME_COL, behaviorName);
        db.update(BehaviorAliasTableSchema.NAME, cval, where, new String[] {Integer.toString(behaviorID)});
    }

    public void deleteBehavior(int behaviorID){
        String where = BehaviorAliasTableSchema.BEHAVIOR_ID_COL + " = ?";
        db.delete(BehaviorAliasTableSchema.NAME, where, new String[]{Integer.toString(behaviorID)});
    }

    public Cursor getAllBehaviors(){
        return db.query(BehaviorAliasTableSchema.NAME, null, null, null, null, null,
                BehaviorAliasTableSchema.BEHAVIOR_ID_COL);
    }

    public Behavior[] getBehaviorObjects(Context context){
        Cursor c = getAllBehaviors();
        Behavior[] behaviors = new Behavior[c.getCount()];
        for(c.moveToFirst(); c.moveToNext(); c.isAfterLast()){
            Behavior currentBehavior = new Behavior(
                    c.getInt(c.getColumnIndex(BehaviorAliasTableSchema.BEHAVIOR_ID_COL)),
                    c.getString(c.getColumnIndex(BehaviorAliasTableSchema.BEHAVIOR_NAME_COL)),
                    Behavior.Positivity.toPos(c.getInt(c.getColumnIndex(BehaviorAliasTableSchema.POSITIVITY_COL))),
                    context
            );
            behaviors[c.getPosition()] = currentBehavior;
        }
        c.close();
        return behaviors;
    }

    public boolean behaviorExists(int behaviorID){
        String where = BehaviorAliasTableSchema.BEHAVIOR_ID_COL + " = ?";
        Cursor c = db.query(BehaviorAliasTableSchema.NAME, null, where,
                new String[]{Integer.toString(behaviorID)}, null, null, null);
        boolean rtnval =  c.moveToFirst();
        c.close();
        return rtnval;
    }
    //</editor-fold>

    //<editor-fold desc="Class List Handling">
    public void addStudentToClass(String studentID, String classID){
        ContentValues cval = new ContentValues();
        cval.put(StudentClassMappingTableSchema.STUDENT_ID_COL, studentID);
        cval.put(StudentClassMappingTableSchema.CLASS_ID_COL, classID);
        db.insert(StudentClassMappingTableSchema.NAME, null, cval);
    }

    public void removeStudentFromClass(String studentID, String classID){
        String where = StudentClassMappingTableSchema.STUDENT_ID_COL + " = ? AND " +
                StudentClassMappingTableSchema.CLASS_ID_COL + " = ?";
        db.delete(StudentClassMappingTableSchema.NAME, where, new String[]{studentID, classID});
    }

    public void removeStudentfromAllClasses(String studentID){
        String where = StudentClassMappingTableSchema.STUDENT_ID_COL + " = ?";
        db.delete(StudentClassMappingTableSchema.NAME, where, new String[]{studentID});
    }

    public boolean isStudentInClass(String studentID, String classID){
        String where = StudentClassMappingTableSchema.STUDENT_ID_COL + " = ? AND " +
                StudentClassMappingTableSchema.CLASS_ID_COL_DEF + " = ?";
     Cursor c = db.query(StudentClassMappingTableSchema.NAME, null, where,
             new String[]{studentID, classID},null, null, null);
        boolean rtnval = c.moveToFirst();
        c.close();
        return rtnval;
    }

    public Cursor getStudentsInClass(String classID){
        String where = StudentClassMappingTableSchema.CLASS_ID_COL + " = ?";
        return db.query(StudentClassMappingTableSchema.NAME,
                new String[]{StudentClassMappingTableSchema.STUDENT_ID_COL}, where,
                new String[]{classID}, null, null,
                StudentClassMappingTableSchema.STUDENT_ID_COL);
    }

    public Cursor getClassesForStudent(String studentID){
        String where = StudentClassMappingTableSchema.STUDENT_ID_COL + " = ?";
        return db.query(StudentClassMappingTableSchema.NAME,
                new String[]{StudentClassMappingTableSchema.CLASS_ID_COL}, where,
                new String[]{studentID}, null, null, null);
    }

    public Cursor getAllClasses () {
        return db.query(StudentClassMappingTableSchema.NAME,
                new String[]{StudentClassMappingTableSchema.CLASS_ID_COL},
                null, null, null, null,
                StudentClassMappingTableSchema.CLASS_ID_COL);
    }

    public Cursor getAllStudentsInClasses() {
        return db.query(StudentClassMappingTableSchema.NAME,
                new String[]{StudentClassMappingTableSchema.STUDENT_ID_COL},
                null, null, null, null,
                StudentClassMappingTableSchema.STUDENT_ID_COL);
    }
    //</editor-fold>

    //<editor-fold desc="Schema Classes">
    static class BehaviorRecordTableSchema{
        public static final String NAME = "BehaviorRecords";

        public static final String BEHAVIOR_ID_COL_DEF = "behaviorID INTEGER NOT NULL,";
        public static final String STUDENT_ID_COL_DEF = "studentID TEXT NOT NULL,";
        public static final String CLASS_ID_COL_DEF = "classID TEXT NOT NULL,"; //Included because it might be useful to know when a student has different behaviors in different classes.
        public static final String TIMESTAMP_COL_DEF = "timestamp INTEGER NOT NULL,"; //use something that returns UNIX time in milliseconds
        public static final String PRIMARY_KEY_DEF = "PRIMARY KEY (behaviorID, studentID, timestamp),"; //Why, yes, I do need all of these columns. Don't question me.
        public static final String BEHAVIOR_ID_ALIAS_DEF = "FOREIGN KEY(behaviorID) REFERENCES BehaviorAlias(behaviorID),";
        public static final String STUDENT_ID_FOREIGN_KEY_DEF = "FOREIGN KEY (studentID) REFERENCES Students(studentID)";
        
        public static final String BEHAVIOR_ID_COL = "behaviorID";
        public static final String STUDENT_ID_COL = "studentID";
        public static final String CLASS_ID_COL = "classID";
        public static final String TIMESTAMP_COL = "timestamp";
    }

    static class BehaviorAliasTableSchema{
        public static final String NAME = "BehaviorAlias";

        public static final String BEHAVIOR_ID_COL_DEF = "behaviorID INTEGER PRIMARY KEY AUTOINCREMENT,";
        public static final String BEHAVIOR_NAME_COL_DEF = "behaviorNameTxt TEXT,";
        public static final String POSITIVITY_COL_DEF = "positivity INTEGER DEFAULT 0 CHECK(positivity IN(-1,0,1))";

        public static final String BEHAVIOR_ID_COL = "behaviorID";
        public static final String BEHAVIOR_NAME_COL = "behaviorNameTxt";
        public static final String POSITIVITY_COL = "positivity";
    }
    
    static class AttendanceRecordsTableSchema{
        public static final String NAME = "AttendanceRecords";

        public static final String STUDENT_ID_COL_DEF = "studentID TEXT NOT NULL,";
        public static final String CLASS_ID_COL_DEF = "classID TEXT NOT NULL,";
        public static final String INTERVAL_COL_DEF = "interval INTEGER NOT NULL,";
        public static final String PRESENT_COL_DEF = "present INTEGER NOT NULL CHECK(present IN(0, 1)),";
        public static final String LATE_ARRIVAL_COL_DEF = "lateArrival INTEGER NOT NULL CHECK(lateArrival IN(0, 1) AND present IS 1),";
        public static final String EARLY_DEPARTURE_COL_DEF = "earlyDeparture INTEGER NOT NULL CHECK(earlyDeparture IN(0, 1) AND present IS 1),";
        public static final String EXCUSED_COL_DEF = "excused INTEGER NOT NULL CHECK(excused IN(0, 1)),";
        public static final String PRIMARY_KEY_DEF = "PRIMARY KEY(studentID, classID, interval)";
        public static final String STUDENT_ID_FOREIGN_KEY_DEF = "FOREIGN KEY (studentID) REFERENCES Students(studentID)";

        public static final String STUDENT_ID_COL = "studentID";
        public static final String CLASS_ID_COL = "classID";
        public static final String INTERVAL_COL = "interval";
        public static final String PRESENT_COL = "present";
        public static final String LATE_ARRIVAL_COL = "lateArrival";
        public static final String EARLY_DEPARTURE_COL = "earlyDeparture";
        public static final String EXCUSED_COL = "excused";
    }

    static class StudentClassMappingTableSchema{
        public static final String NAME = "StudentClassMap";

        public static final String STUDENT_ID_COL_DEF = "studentID TEXT NOT NULL,";
        public static final String CLASS_ID_COL_DEF = "classID TEXT NOT NULL,";
        public static final String PRIMARY_KEY_DEF = "PRIMARY KEY(studentID, classID)";
        public static final String STUDENT_ID_FOREIGN_KEY_DEF = "FOREIGN KEY (studentID) REFERENCES Students(studentID)";

        public static final String STUDENT_ID_COL = "studentID";
        public static final String CLASS_ID_COL = "classID";
    }

    static class StudentTableSchema{
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

    private static class AsyncDatabaseFetcher extends AsyncTask{

        /**
         * There, you happy now, compiler?
         *
         * @param objects Thing to do stuff to.
         * @return Stuff with things done to it.
         */
        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }

        protected SQLiteDatabase doInBackground(DatabaseHandler dbh) {
            return dbh.getWritableDatabase();
        }
    }
}
