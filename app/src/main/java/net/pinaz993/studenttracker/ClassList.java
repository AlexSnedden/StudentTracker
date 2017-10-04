package net.pinaz993.studenttracker;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

/**
 * Created by Patrick Shannon on 8/30/2017.
 */

public class ClassList {
    /**
     * A list of students in a single class.
     */
    private Student[] studentList;

    public Student[] getStudentList() {
        return studentList;
    }

    private final String JSON_FIRST_NAME = "fName";
    private final String JSON_LAST_NAME = "lName";
    private final String JSON_EMAIL = "email";
    private final String JSON_ID = "ID";

    public ClassList(JSONObject studentRecords) {
        //TODO: implement email handling
        JSONArray studentArray = null;
        JSONObject student = null;

        try {
            Log.v("boom", studentRecords.toString());
            studentArray = new JSONArray(studentRecords.get("test_students").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int jsonArrayLength = studentArray.length();
        studentList = new Student[jsonArrayLength];
        for(int i = 0; i < jsonArrayLength; i++) {
            try {
                student = studentArray.getJSONObject(i);
                // create student object and append it to the studentList array
                studentList[i] = new Student(student.get(JSON_FIRST_NAME).toString(),
                        student.get(JSON_LAST_NAME).toString(),
                        student.get(JSON_ID).toString());

            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static ClassList retrieve(String classListID){
        /*
        grab the classList from Paper with this ID
         */
        return null;
    }

    public void save(){
        /*
        save this object to disk with array list of student ids
         */
    }

    public void putToBed(){
        /*
        serialise each student, commiting student IDs to memory in array list
        Kill student objects
        Kill transient array list of student objects
        serialize this
        suicide

         */
    }

}
