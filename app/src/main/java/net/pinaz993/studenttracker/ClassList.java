package net.pinaz993.studenttracker;

import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.Iterator;
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
    private transient ArrayList<Student> students;
    private ArrayList<String> studentIDs;

    public static ClassList retrieve(String classListID){
        /*
        grab the classList from Paper with this ID
         */
        ClassList classList = Paper.book(BOOK_ID).read(classListID);
        classList.populateStudents();
        return classList;
    }

    ClassList(String classListID) {
        this.classListID = classListID;
        this.students = new ArrayList<Student>();
        this.studentIDs = new ArrayList<String>();
    }

    public void importStudents(String csvStudentIDData) {
        CharArrayReader csvStudentIDDataReader = new CharArrayReader(csvStudentIDData.toCharArray());
    }

    public void addStudent(Student student) {
        students.add(student);
        studentIDs.add(student.getID());
    }

    public void removeStudent(Student student) {
        /* assumes student points to the same object that needs to be removed */
        Iterator<Student> studentsIterator = students.listIterator();
        Student currentStudent;
        while(studentsIterator.hasNext()) {
            currentStudent = studentsIterator.next();
            if(currentStudent == student) {
                students.remove(currentStudent);
                break;
            }
        }
    }


    public void save(){
        /*
        save this object to disk with array list of student ids
         */

        Paper.book(BOOK_ID).write(this.classListID, this);
    }

    public void populateStudents() {
        /*
        read each student from disk and insert them into transient student array list
         */
        Iterator<String> studentIDsIterator = studentIDs.listIterator();
        while(studentIDsIterator.hasNext()) {
            students.add(Student.retrieve(studentIDsIterator.next()));
        }
    }

    public void putToBed(){
        /*
        serialize each student, committing student IDs to memory in array list
        Kill student objects
        Kill transient array list of student objects
        serialize this
        suicide

         */

        Iterator<Student> studentsIterator = students.listIterator();
        Student student;
        while(studentsIterator.hasNext()) {
            student = studentsIterator.next();
            studentIDs.add(student.getID());
            student.save();
        }
        // ensure student pointer is null as well as pointer to array list of students, then
        // call garbage collector on them.
        student = null;
        students = null;
        save();
        // pointer to this object must be set to null from outside to ensure garbage collection.
    }

}