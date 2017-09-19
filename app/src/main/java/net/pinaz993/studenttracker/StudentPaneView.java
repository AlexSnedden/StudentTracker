package net.pinaz993.studenttracker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

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
 */
public class StudentPaneView extends View {

    public StudentPaneView(Context context, Student student) {
        super(context);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}