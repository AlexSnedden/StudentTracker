package net.pinaz993.studenttracker;

/**
 * Created by Patrick Shannon on 10/3/2017.
 */

public class DatabaseHandler {

    private static class BehaviorRecordTableSchema{
        public final String name = "BehaviorRecords";

        public final String behaviorIdColDef = "behaviorID INTEGER NOT NULL,";
        public final String studentIdColDef = "studentID TEXT NOT NULL,";
        public final String classIdColDef = "classID TEXT NOT NULL,"; //Included because it might be useful to know when a student has different behaviors in different classes.
        public final String timestampColDef = "timestamp INTEGER NOT NULL,"; //use something that returns UNIX time in milliseconds
        public final String primaryKeyDef = "PRIMARY KEY (behaviorID, studentID, timestamp),"; //Why, yes, I do need all of these columns. Don't question me.
        public final String behaviorIdAliasDef = "FOREIGN KEY(behaviorID) REFERENCES behaviorAlias(behaviorID);";
        
        public final String behaviorIdCol = "behaviorID";
        public final String studentIdCol = "studentID";
        public final String classIdCol = "classID";
        public final String timestampCol = "timestamp";
    }

    private static class BehaviorAliasTableSchema{
        public final String name = "BehaviorAlias";

        public final String behaviorIdColDef = "behaviorID INTEGER NOT NULL,";
        public final String behaviorNameColDef = "behaviorName TEXT,";
        public final String positivityColDef = "positivity INTEGER DEFAULT 0 CHECK(positivity IN(-1,0,1));";

        public final String behaviorIdCol = "behaviorID";
        public final String behaviorNameCol = "behaviorName";
        public final String positivityCol = "positivity";
    }
    
    private static class AttendanceRecordsTableSchema{
        public final String name = "AttendanceRecords";

        public final String studentIdColDef = "studentID TEXT NOT NULL,";
        public final String classIdColDef = "classID TEXT NOT NULL,";
        public final String timestampColDef = "timestamp INTEGER NOT NULL,";
        public final String presentColDef = "present INTEGER NOT NULL CHECK(present IN(0, 1)),";
        public final String lateArrivalColDef = "lateArrival INTEGER NOT NULL CHECK(lateArrival IN(0, 1) AND present IS 1),";
        public final String earlyDepartureColDef = "earlyDeparture INTEGER NOT NULL CHECK(earlyDeparture IN(0, 1) AND present IS 1),";
        public final String excusedColDef = "excused INTEGER NOT NULL CHECK(excused IN(0, 1)),";
        public final String primaryKeyDef = "PRIMARYKEY(studentID, classID, timestamp)";

        public final String studentIdCol = "studentID";
        public final String classIdCol = "classID";
        public final String timestampCol = "timestamp";
        public final String presentCol = "present";
        public final String lateArrivalCol = "lateArrival";
        public final String earlyDepartureCol = "earlyDeparture";
        public final String excusedCol = "excused";
    }
}
