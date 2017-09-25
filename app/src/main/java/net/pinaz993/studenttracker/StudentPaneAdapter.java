package net.pinaz993.studenttracker;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

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

public class StudentPaneAdapter extends ArrayAdapter {


    private Student student;

    private final LayoutInflater inflater;
    private final ViewBinderHelper binderHelper;

    public StudentPaneAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull Object[] objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(getContext());
        binderHelper = new ViewBinderHelper();
        binderHelper.setOpenOnlyOne(true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.student_pane_template, parent, false);

            holder = new ViewHolder();
            holder.bottomLayout = convertView.findViewById(R.id.bottomLayout);
            holder.excusedBtn = convertView.findViewById(R.id.excusedBtn);
            holder.earlyDepartureBtn = convertView.findViewById(R.id.earlyDepartureBtn);
            holder.lateArrivalBtn = convertView.findViewById(R.id.lateArrivalBtn);

            holder.topLayout = convertView.findViewById(R.id.topLayout);
            holder.studentNameTxt = convertView.findViewById(R.id.studentNameTxt);
            holder.absentPresentSwitch = convertView.findViewById(R.id.absentPresentSwitch);

            holder.swipe = convertView.findViewById(R.id.swipe);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final Student student = (Student)getItem(position);
        if(student != null) {
            binderHelper.bind(holder.swipe, student.getStudentID());
        }






        return convertView;
    }


    private void toggleAbsentPresent(SwitchCompat switchCompat, Student student) {}

    private void toggleLateArrival(ToggleButton toggleButton, Student student) {}

    private void toggleEarlyDeparture(ToggleButton toggleButton, Student student) {}

    private void toggleExcused(ToggleButton toggleButton, Student student) {}

    private class ViewHolder {
        LinearLayout bottomLayout;
        ToggleButton excusedBtn,
                lateArrivalBtn,
                earlyDepartureBtn;

        LinearLayout topLayout;
        TextView studentNameTxt;
        SwitchCompat absentPresentSwitch;

        SwipeRevealLayout swipe;
    }
}