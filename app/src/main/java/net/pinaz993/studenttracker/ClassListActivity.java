package net.pinaz993.studenttracker;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import net.cachapa.expandablelayout.ExpandableLayout;

public class ClassListActivity extends AppCompatActivity {
    String classID;
    DatabaseHandler dbh = DatabaseHandler.getInstance();
    SettingsHandler settings = SettingsHandler.getInstance();
    Student[] students;
    private ListView studentList;
    private ListView optionsList;
    private StudentPaneAdapter studentPaneAdapter;
    private ExpandableLayout optionContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        optionContainer = (ExpandableLayout) findViewById(R.id.option_container);

        Button dropMenuBtn = (Button) findViewById(R.id.drop_menu_btn);
        dropMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu();
            }
        });

        Bundle extras = getIntent().getExtras();
        studentList = (ListView) findViewById(R.id.student_list);

        classID = (extras != null) ? extras.getString("CLASS_ID_KEY") : null;
        if (classID == null) {
            // activate the class selector dialog, use it to return a valid classID
        }
        optionsList = (ListView) findViewById(R.id.options_list);

        Cursor c = dbh.getStudentsInClass(classID);
        students = new Student[c.getCount()];
        while (c.moveToNext()) {
            String studentID = c.getString(c.getColumnIndex(
                    DatabaseHandler.StudentClassMappingTableSchema.STUDENT_ID_COL));
            students[c.getPosition()] = dbh.retrieveStudent(studentID);
        }

        studentPaneAdapter = new StudentPaneAdapter(this, students);
        studentList.setAdapter(studentPaneAdapter);

        studentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return true;
            }
        });

        c.close();
        settings.setLastActivityRun(SettingsHandler.ACTIVITY.CLASS_LIST);
        settings.setLastClassID(classID);
    }

    public void toggleMenu(){
        optionContainer.toggle(true);
    }

    public void onDropMenu(){
        studentList.setVisibility(View.INVISIBLE);
    }

    public void onRetractMenu(){
        studentList.setVisibility(View.VISIBLE);
    }
}
