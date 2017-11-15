package net.pinaz993.studenttracker;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Space;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

public class ClassListActivity extends AppCompatActivity implements
        ExpandableLayout.OnExpansionUpdateListener, View.OnClickListener, AdapterView.OnItemClickListener {
    String classID;
    DatabaseHandler dbh = DatabaseHandler.getInstance();
    SettingsHandler settings = SettingsHandler.getInstance();
    Student[] students;
    private ListView studentList;
    private Space spacer;
    private ListView optionsList;
    private ExpandableLayout optionContainer;
    private Button dropMenuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        Bundle extras = getIntent().getExtras();
        classID = (extras != null) ? extras.getString("CLASS_ID_KEY") : null;
        if (classID == null) {
            // activate the class selector dialog, use it to return a valid classID
        }

        optionContainer = (ExpandableLayout) findViewById(R.id.option_container);
        optionContainer.setOnExpansionUpdateListener(this);

        dropMenuBtn = (Button) findViewById(R.id.drop_menu_btn);
        dropMenuBtn.setOnClickListener(this);

        studentList = (ListView) findViewById(R.id.student_list);
        students = getStudentsInClass();
        StudentPaneAdapter studentPaneAdapter = new StudentPaneAdapter(this, students);
        studentList.setAdapter(studentPaneAdapter);
        spacer = (Space)findViewById(R.id.spacer);

        String[] classes = getAllClasses();
        optionsList = (ListView) findViewById(R.id.options_list);
        optionsList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, classes));
        optionsList.setOnItemClickListener(this);

        settings.setLastActivityRun(SettingsHandler.ACTIVITY.CLASS_LIST);
        settings.setLastClassID(classID);
    }

    //<editor-fold desc="Get Stuff From Database">
    private String[] getAllClasses(){
        Cursor c = dbh.getAllClasses();
        c.moveToFirst();
        ArrayList<String> classes = new ArrayList<String>();
        do{
            String newClass = c.getString(c.getColumnIndex(DatabaseHandler.StudentClassMappingTableSchema.CLASS_ID_COL));
            classes.add(newClass);
        } while (c.moveToNext());
        c.close();
        return classes.toArray(new String[]{});
    }

    private Student[] getStudentsInClass(){
        Cursor c = dbh.getStudentsInClass(classID);
        Student[] students = new Student[c.getCount()];
        while (c.moveToNext()) {
            String studentID = c.getString(c.getColumnIndex(
                    DatabaseHandler.StudentClassMappingTableSchema.STUDENT_ID_COL));
            students[c.getPosition()] = dbh.retrieveStudent(studentID);
        }
        c.close();
        return students;
    }
    //</editor-fold>

    //<editor-fold desc="Listener Callbacks">
    @Override
    public void onExpansionUpdate(float expansionFraction, int state) {
        dropMenuBtn.setRotation(expansionFraction * 180);
        // set spacer weight to expansion fraction

        //set studentList weight to 1 - expansion fraction
    }

    @Override
    public void onClick(View v) {
        optionContainer.toggle(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    //</editor-fold>
}
