package net.pinaz993.studenttracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONObject;

import java.io.InputStream;

public class ClassListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        InputStream csvFileStream = getApplicationContext().getResources().openRawResource(R.raw.test_students);
        byte[] buffer = null;
        String studentData = null;
        String csvString = null;
        try {
            buffer = new byte[csvFileStream.available()];
            csvFileStream.read(buffer);
            csvFileStream.close();
            csvString = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassList studentList = new ClassList(csvString);
        StudentPaneAdapter studentPaneList = new StudentPaneAdapter(getApplicationContext(),
                studentList.getStudentList());
        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(studentPaneList);
    }
}