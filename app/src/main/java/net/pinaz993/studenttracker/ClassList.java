package net.pinaz993.studenttracker;

import java.util.ArrayList;

import io.paperdb.Paper;

/**
 * Created by Patrick Shannon on 8/30/2017.
 */

public class ClassList {
    /**
     * A list of students in a single class.
     */
    final static String BOOK_ID = "ClassLists";
    private String classListID;
    private ArrayList<String> studentIDs;

    public static ClassList retrieve(String classListID){
        /*
        grab the classList from Paper with this ID
         */
        return Paper.book(BOOK_ID).read(classListID);
    }

    public void save(){
        /*
        save this object to disk with array list of student ids
         */
        Paper.book(BOOK_ID).write(this.classListID, this);
    }

    public void putToBed(){
        /*
        serialize each student, committing student IDs to memory in array list
        Kill student objects
        Kill transient array list of student objects
        serialize this
        suicide

         */
        //TODO: implement ClassList.putToBed
        ArrayList<Student> students;

    }

}
