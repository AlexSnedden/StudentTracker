package net.pinaz993.studenttracker;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
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

public class StudentPaneAdapter extends ArrayAdapter implements BehaviorDialog.BehaviorDialogListener {
    private final LayoutInflater inflater;
    private final ViewBinderHelper binderHelper;

    public StudentPaneAdapter(@NonNull Context context, @NonNull Object[] objects) {
        super(context, R.layout.student_pane_template, objects);
        inflater = LayoutInflater.from(getContext());
        binderHelper = new ViewBinderHelper();
        binderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
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
            holder = (ViewHolder) convertView.getTag();
        }

        final Student student = (Student) getItem(position);
        if (student != null) {
            binderHelper.bind(holder.swipe, student.getID());
            //Set click handlers
            holder.excusedBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    toggleExcused(holder.excusedBtn.isChecked(), student);
                }
            });

            holder.earlyDepartureBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    toggleEarlyDeparture(holder.earlyDepartureBtn.isChecked(), student);
                }
            });

            holder.lateArrivalBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    toggleLateArrival(holder.lateArrivalBtn.isChecked(), student);
                }
            });

            holder.absentPresentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    toggleAbsentPresent(holder.absentPresentSwitch.isChecked(), student);
                }
            });

            holder.studentNameTxt.setText(student.getFullName());

            holder.topLayout.setLongClickable(true);
            holder.topLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FragmentManager fm = ((Activity)getContext()).getFragmentManager();
                    BehaviorDialog dialog = new BehaviorDialog();
                    dialog.setListener(StudentPaneAdapter.this);
                    dialog.show(fm, student.getID() + "BehaviorDialog");
                    return true;
                }
            });
            holder.swipe.setSwipeListener(new SwipeRevealLayout.SwipeListener() {
                @Override
                public void onClosed(SwipeRevealLayout view) {
                    holder.earlyDepartureBtn.setActivated(false);
                    holder.lateArrivalBtn.setActivated(false);
                    holder.excusedBtn.setActivated(false);
                }

                @Override
                public void onOpened(SwipeRevealLayout view) {
                    holder.earlyDepartureBtn.setActivated(true);
                    holder.lateArrivalBtn.setActivated(true);
                    holder.excusedBtn.setActivated(true);

                }

                @Override
                public void onSlide(SwipeRevealLayout view, float slideOffset) {
                }
            });
        }
        else throw new NullPointerException("Tried to bind non-existent student to view");

        return convertView;
    }

    private void toggleAbsentPresent(boolean isChecked, Student student) {}

    private void toggleLateArrival(boolean isChecked, Student student) {}

    private void toggleEarlyDeparture(boolean isChecked, Student student) {}

    private void toggleExcused(boolean isChecked, Student student) {}

    @Override
    public void onDialogPositiveClick(BehaviorDialog dialog) {
        Behavior[] selectedBehaviors = dialog.collectResults();
    }

    private class ViewHolder {
        LinearLayout bottomLayout;
        ToggleButton excusedBtn,
                lateArrivalBtn,
                earlyDepartureBtn;

        ConstraintLayout topLayout;
        TextView studentNameTxt;
        Switch absentPresentSwitch;

        SwipeRevealLayout swipe;
    }
}