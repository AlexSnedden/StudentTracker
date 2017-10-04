package net.pinaz993.studenttracker;

import android.content.res.Resources;

import java.util.ArrayList;

/**
 * BehaviorHandler is a class for tracking user customizable behavior objects. It uses an
 * SQL Alias table to hold the values for these behaviors. It is also responsible for using this
 * table to look up the values for a ListView dialog that allows the user to record behaviors.
 * Created by Patrick Shannon on 9/28/2017.
 */

class BehaviorHandler {
    private static final BehaviorHandler ourInstance = new BehaviorHandler();

    private ArrayList<Behavior> behaviors;

    public enum Positivity{

        NEGATIVE (-1, Resources.getSystem().getString(R.string.behaviorNegative)),
        NEUTRAL (0, Resources.getSystem().getString(R.string.behaviorNeutral)),
        POSITIVE (1, Resources.getSystem().getString(R.string.behaviorPositive));

        public final int value;
        private String name;

        Positivity(int value, String name) {
            this.value = value;
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    static BehaviorHandler getInstance() {
        return ourInstance;
    }

    private BehaviorHandler() {
        //TODO: Implement the constructor for BehaviorHandler

        //query the database for the table
        //if it's not there, make it.
        //if it is there, use it to populate an array list for later use in the selection dialog

        //schema: one table, behaviors
        //three columns: int behaviorID,  String behaviorName,  int positivity

        //use the value for the positivity as the int value in the database.
    }

    public ArrayList<Behavior> getBehaviors() {
        return behaviors;
    }

    private class Behavior{
        public final int behaviorID;
        public String behaviorName;
        public final Positivity behaviorPositivity;

        public Behavior(int behaviorID, String behaviorName, Positivity behaviorPositivity) {
            this.behaviorID = behaviorID;
            this.behaviorName = behaviorName;
            this.behaviorPositivity = behaviorPositivity;
        }
    }
}
