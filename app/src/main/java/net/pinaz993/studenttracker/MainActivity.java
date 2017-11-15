package net.pinaz993.studenttracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SettingsHandler settings = new SettingsHandler(getApplicationContext());

        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext(), null);
        dbh.getDatabaseManually();

        Intent redirect = new Intent(MainActivity.this, ClassListActivity.class);
        redirect.putExtra("CLASS_ID_KEY", "Fiction101");
        startActivity(redirect);
    }

    private void prepDB(DatabaseHandler dbh){
        Student bob = new Student("Bobert", "Roberts", "0", null),
                will = new Student("Will", "Gladeson", "1", null),
                hope = new Student("Hope", "Gladeson", "2", null),
                joan = new Student("Joan",  "Glade", "3", null),
                heck = new Student("Hector", "Trent", "4", null),
                jamie = new Student("Benjamin", "Connors", "5", null);

        dbh.addStudentToClass("0", "Fiction101");
        dbh.addStudentToClass("1", "Fiction101");
        dbh.addStudentToClass("2", "Fiction101");
        dbh.addStudentToClass("3", "Fiction101");
        dbh.addStudentToClass("4", "Fiction101");
        dbh.addStudentToClass("5", "Fiction101");

        dbh.addStudentToClass("1", "Swordsmanship+");
        dbh.addStudentToClass("2", "Swordsmanship+");
        dbh.addStudentToClass("5", "Swordsmanship+");

        dbh.addStudentToClass("4", "AdvancedPhotomechanics");
        dbh.addStudentToClass("0", "AdvancedPhotomechanics");

        dbh.addStudentToClass("1", "Humanitarianism");


        dbh.addNewBehavior("PositiveTest1", 1);
        dbh.addNewBehavior("PositiveTest2", 1);
        dbh.addNewBehavior("NeutralTest1", 0);
        dbh.addNewBehavior("NeutralTest2", 0);
        dbh.addNewBehavior("NegativeTest1", -1);
        dbh.addNewBehavior("NegativeTest2", -1);
    }
}
