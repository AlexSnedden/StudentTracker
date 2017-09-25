package net.pinaz993.studenttracker;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(getApplicationContext());
        ConstraintLayout mainLayout = (ConstraintLayout)findViewById(R.id.mainLayout);

        Student bob = new Student("Bobert", "Roberts", "12345");
        Log.d("", "onCreate: created student");
        StudentPaneAdapter bobpane = new StudentPaneAdapter(getApplicationContext(), bob);
        Log.d("", "onCreate: created student pane");
        mainLayout.addView(bobpane);



    }
    /* temporary function to test ui with students objects */
    private Student[] generateStudents(int numStudents) {
        ArrayList<Student> students = new ArrayList<>();
        for(int i=0; i < numStudents; i++) {
            students.add(new Student("Richie", "Rich", String.valueOf(i), "richierich@gmail.com"));
        }
        return (Student[])students.toArray();
    }
}
