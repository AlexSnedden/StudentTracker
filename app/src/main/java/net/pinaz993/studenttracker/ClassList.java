package net.pinaz993.studenttracker;

import android.util.Log;

import com.opencsv.CSVReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.List;

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

    public ClassList(String studentRecords) {
        //TODO: implement email handling
        CSVReader csvReader = new CSVReader(new StringReader(studentRecords), '\t');
        List<String[]> csvStudents = null;
        try {
            csvStudents = csvReader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        studentList = new Student[csvStudents.size()];
        for(int i=0; i < studentList.length; i++) {
            studentList[i] = new Student(csvStudents.get(i)[0], csvStudents.get(i)[1], csvStudents.get(i)[2]);
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
