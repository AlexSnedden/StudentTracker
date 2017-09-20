package net.pinaz993.studenttracker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.chauthai.swipereveallayout.SwipeRevealLayout;

/**
 * StudentPaneView is a view that displays information about and presents interactivity for an
 * instance of the Student class. It contains the name of the student, a switch for present/absent,
 * and a swipe-left function for revealing additional options pertaining to attendance. Later, it
 * will also contain a long-press functionality for additional interactivity with the Student
 * instance.
 *
 * Naturally, when this is constructed, it will need to have access to a single student object.
 * Other than that, it shouldn't need anything other than the context. The color of the main pane
 * will be determined by the conditional formatting from the student object.
 * Created by Patrick Shannon on 9/20/2017.
 */

public class StudentPane extends SwipeRevealLayout {


    Student student;
    // Child views
    View rootView;
    TextView studentNameTxt;
    ToggleButton lateArrivalBtn;
    ToggleButton earlyDepartureBtn;
    SwitchCompat aPSwitch;
    // I need something for the long press, eventually

    LinearLayoutCompat.LayoutParams MATCHING_PARENT = new LinearLayoutCompat.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT);



    public StudentPane(Context context,Student student) {
        super(context);
        init(context);
        this.student = student;
    }

    public StudentPane(Context context, AttributeSet attrs, Student student ) {
        super(context, attrs);
        init(context);
        this.student = student;
    }

    public StudentPane(Context context, AttributeSet attrs, int defStyleAttr, Student student) {
        super(context, attrs, defStyleAttr);
        init(context);
        this.student = student;
    }

    private void init(Context context) {
        //Bottom Layout
        LinearLayoutCompat.LayoutParams bLParams = new LinearLayoutCompat.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        LinearLayout bottomLayout = new LinearLayout(context);
        bottomLayout.setLayoutParams(bLParams);
        bottomLayout.setGravity(5|10);
        bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
        bottomLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        bottomLayout.setLayoutDirection(LAYOUT_DIRECTION_RTL);

        //Excused Button
        ToggleButton excusedBtn = new ToggleButton(context);
        excusedBtn.setTextOff(getResources().getString(R.string.excusedTxt));
        excusedBtn.setTextOn(getResources().getString(R.string.excusedTxt));
        bottomLayout.addView(excusedBtn, bLParams);

        //Early Departure Button
        ToggleButton earlyDepartureBtn = new ToggleButton(context);
        earlyDepartureBtn.setTextOn(getResources().getString(R.string.earlyDepartureTxt));
        earlyDepartureBtn.setTextOff(getResources().getString(R.string.earlyDepartureTxt));
        bottomLayout.addView(earlyDepartureBtn, bLParams);

        //Late Arrival Button
        ToggleButton lateArrivalBtn = new ToggleButton(context);
        lateArrivalBtn.setTextOn(getResources().getString(R.string.lateArrivalTxt));
        lateArrivalBtn.setTextOff(getResources().getString(R.string.lateArrivalTxt));
        bottomLayout.addView(lateArrivalBtn,bLParams);

        //Top Layout
        LinearLayoutCompat.LayoutParams tLParams = new LinearLayoutCompat.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        LinearLayout topLayout = new LinearLayout(context);
        topLayout.setLayoutParams(tLParams);
        topLayout.setOrientation(LinearLayout.HORIZONTAL);
        topLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        topLayout.setWeightSum(1);

        //Student Name Text View
        TextView studentNameView = new TextView(context);
        LinearLayoutCompat.LayoutParams sNParams = new LinearLayoutCompat.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT,
                (float).85);
        sNParams.gravity = 10;
        studentNameView.setText(student.getFullName());
        studentNameView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        topLayout.addView(studentNameView, sNParams);

        //Absent|Present Switch
        SwitchCompat aPSwitch = new SwitchCompat(context);
        LinearLayoutCompat.LayoutParams aPParams = new LinearLayoutCompat.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT,
                (float).15);
        // Color states for the aforementioned switch
        int[][] statesList = new int[][] {
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_checked}
        };

        int[] thumbColorsList = new int[] {
                Color.GREEN,
                Color.RED
        };

        int[] trackColorsList = new int[] {
                Color.parseColor("#aaffaa"),
                Color.parseColor("#ffaaaa")
        };

        ColorStateList thumbColors = new ColorStateList(statesList, thumbColorsList);
        ColorStateList trackColors = new ColorStateList(statesList, trackColorsList);
        DrawableCompat.setTintList(DrawableCompat.wrap(aPSwitch.getThumbDrawable()), thumbColors);
        DrawableCompat.setTintList(DrawableCompat.wrap(aPSwitch.getTrackDrawable()), trackColors);

        topLayout.addView(aPSwitch, aPParams);








    }
}
