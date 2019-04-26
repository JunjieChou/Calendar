package com.olituc.standard.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.timber.standard.R;
import com.timber.standard.listener.SignCalendarListener;

import java.util.Calendar;

/**
 * @author olituc
 * @version 1.1
 * @date 2019.04.26
 */

public class SignCalendar extends ViewFlipper implements TextView.OnClickListener, SignCalendarListener.onSignCalendarListener {
    //the font color of every single day in calendar is white by default
    public static final int DEFAULT_FONT_COLOR=Color.parseColor("#000000");
    //the font color of every single day in calendar is black by clicked or checked
    public static final int CHECKED_FONT_COLOR=Color.parseColor("#ffffff");
    //the back color of every single day in calendar is white by default
    public static final int DEFAULT_BACK_COLOR=Color.parseColor("#ffffff");
    //the back color of every single day in calendar is black by clicked or checked
    public static final int CHECKED_BACK_COLOR=Color.parseColor("#87ceeb");
    //the back color of week title
    public static final int DEFAULT_TITLE_BACK_COLOR=Color.parseColor("#87ceeb");
    //these variables represent for current year, month and day
    private int curYear;
    private int curMonth;
    private int curDay;
    //the position that currently should show the color of CHECKED;
    private int curCheckedDay;
    //this variable is to set current LinearLayout
    private LinearLayout curLayout;
    //an interface showSignCalendarListener to be called in getLastMonth() or getNextMonth()
    private GestureDetector mGestureDetector=null;
    //an inner public interface to listen the click issues
    private SingleDateClickListener mSingleDateClickListener=null;

    /**
     * Constructor with one argument Context, actually calling another constructor inside.
     * @param context assigned when initialized
     */
    public SignCalendar(Context context) {
        this(context,(AttributeSet) null);
    }

    /**
     * Constructor with two arguments, Context and AttributeSet, calling super constructor.
     * @param context assigned when initialized
     * @param attrs assigned when initialized
     */
    public SignCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitializeListener();
        curYear=Calendar.getInstance().get(Calendar.YEAR);
        curMonth=Calendar.getInstance().get(Calendar.MONTH);
        curCheckedDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        curDay=curCheckedDay;
        curLayout=getCalendarView();
        addView(curLayout);
        setBackColor4SingleDay(curCheckedDay, CHECKED_BACK_COLOR);
    }

    /**
     * This function set the listener for mGestureDetector, afterwards onInterceptTouchEvent function
     * will use mGestureDetector.
     */
    private void InitializeListener() {
        SignCalendarListener listener = new SignCalendarListener();
        listener.setmOnSignCalendarListener(this);
        mGestureDetector=new GestureDetector(getContext(),listener);
    }

    /**
     * This function intercepts Touch Event and deal it with the GestureDetector that has been declared
     * in this class.
     * @param ev motion event inspected by Android system
     * @return the outcome of process, false meaning nothing happened
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mGestureDetector!=null)mGestureDetector.onTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * This function is divided into 5 parts, first one drawing the frame for the whole background,
     * second one drawing the title layout and setting it, third one drawing the frame for content,
     * forth one drawing every row layouts as well as the textView in it, fifth one setting the number
     * of date for each textView and setting background color if current calendar containing today.
     * Notice:1.months range from 0 to 11;
     *        2.weekDays range from 1 to 7;
     *        3.days of months starts from 1.
     */
    private LinearLayout getCalendarView() {
        //1.the general background LinearLayout that fill the SignCalendar
        LinearLayout curLayout=new LinearLayout(getContext());
        curLayout.setOrientation(LinearLayout.VERTICAL);
        curLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        curLayout.setWeightSum(7.5f);
        curLayout.setClickable(true);

        //2.the title LinearLayout that lies on the first row with names of weekdays and weekends
        LinearLayout title=new LinearLayout(getContext());
        curLayout.addView(title);
        title.setOrientation(LinearLayout.HORIZONTAL);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                0,0.5f));
        title.setWeightSum(7);
        String[] weekArray={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        for(int i=0;i<7;i++){
            TextView weekTitle = new TextView(getContext());
            weekTitle.setGravity(Gravity.CENTER);
            weekTitle.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT,1f));
            weekTitle.setText(weekArray[i]);
            weekTitle.setTextColor(DEFAULT_FONT_COLOR);
            weekTitle.setBackgroundColor(DEFAULT_TITLE_BACK_COLOR);
            weekTitle.getPaint().setFakeBoldText(true);
            weekTitle.setClickable(false);
            title.addView(weekTitle);
        }

        //3.the content LinearLayout that lies on the other area, aligning the below of title, hosts
        //specific days of each month
        LinearLayout content=new LinearLayout(getContext());
        curLayout.addView(content);
        content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                0,7f));
        content.setOrientation(LinearLayout.VERTICAL);
        content.setWeightSum(6f);
        content.setClickable(true);

        //4.the LinearLayout[] of every rows of content layout which has 6 rows and 7 columns. Only
        //set the clickable attribute for TextView.
        LinearLayout[] rowLayouts=new LinearLayout[6];
        for(int i=0;i<6;i++){
            rowLayouts[i]=new LinearLayout(getContext());
            content.addView(rowLayouts[i]);
            rowLayouts[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    0,1f));
            rowLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
            rowLayouts[i].setWeightSum(7f);
            rowLayouts[i].setClickable(true);
            TextView[] singleDates=new TextView[7];
            for(int j=0;j<7;j++){
                singleDates[j]=new TextView(getContext());
                rowLayouts[i].addView(singleDates[j]);
                singleDates[j].setGravity(Gravity.CENTER);
                singleDates[j].setLayoutParams(new LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.MATCH_PARENT,1f));
                singleDates[j].setTextColor(DEFAULT_FONT_COLOR);
                singleDates[j].setBackgroundColor(DEFAULT_BACK_COLOR);
                singleDates[j].setClickable(true);
                singleDates[j].setOnClickListener(this);
            }
        }

        //5.get current month and year and set the date for the calendar
        //something to be noticed:1.month ranges from 0 to 11; 2.week starts from Sunday(1) while in
        //the following scope firstDay ranges from 0 to 6; 3.days of month starts from 1
        Calendar calendar = Calendar.getInstance();
        calendar.set(curYear,curMonth,1);
        int firstDay=calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int totalDay=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int i=1;
        while(totalDay-->0){
            ((TextView)(rowLayouts[firstDay/7].getChildAt(firstDay%7))).setText(i+++"");
            firstDay++;
        }
        return curLayout;
    }

    /**
     * This function is for setting background color for a single day which is a single TextView. It
     * literally calls setBackColor4SingleDay with an array, having one only integer, and a color.
     * @param date the date from 1 to max
     * @param color the background color need to be set
     */
    public void setBackColor4SingleDay(int date,int color){
        int[] dates={date};
        setBackColor4Days(dates,color);
    }

    /**
     * This function is for setting background color for dates which are TextViews, inspecting
     * the value of date with the maximum date of this month from a calendar instance.Notice that
     * firstDay which is also the first day of this month in this scope starts from 1.
     * @param dates
     * @param color
     */
    public void setBackColor4Days(@NonNull int[] dates,int color){
        Calendar calendar=Calendar.getInstance();
        calendar.set(curYear,curMonth,1);
        int maximum=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay=calendar.get(Calendar.DAY_OF_WEEK);
        for (int date : dates) {
            if (date <= 0 || date > maximum) continue;
            (((LinearLayout)(((LinearLayout)curLayout.getChildAt(1)).getChildAt((date+firstDay-2)/7)))
                    .getChildAt((date+firstDay-2)%7)).setBackgroundColor(color);
        }
    }

    /**
     * This function is for removing background color for dates in current calendar. It literally calls
     * setBackColor4Days with two arguments, one is the array saving the date, another is background
     * color by default which is white.
     * @param date the date need to be set WHITE
     */
    public void removeBackColor4SingleDay(int date){
        int dates[]={date};
        setBackColor4Days(dates,DEFAULT_BACK_COLOR);
    }

    /**
     * This function is for removing background color for dates in current calendar. It literally calls
     * setBackColor4Days with two arguments, one is the array caller put in, another is background
     * color by default which is white.
     * @param dates the array saving the dates caller carries
     */
    public void removeBackColor4Days(int[] dates){
        setBackColor4Days(dates,DEFAULT_BACK_COLOR);
    }


    /***********************************************************************************************/
    /**********************************View.OnClickListener*****************************************/
    /***********************************************************************************************/

    /**
     * This override function is for listening the click events and handle them.
     * Notice that firstDay which is also the first day of this month in this scope starts from 1.
     * @param view the TextView be clicked
     */
    @Override
    public void onClick(View view) {
        if(mSingleDateClickListener==null)return;
        String text=String.valueOf(((TextView)view).getText());
        removeBackColor4SingleDay(curCheckedDay);
        if(!text.equals("")) {
            curCheckedDay=Integer.parseInt(text);
            mSingleDateClickListener.onDateClick(curYear,curMonth+1,curCheckedDay);
        }
        setBackColor4SingleDay(curCheckedDay,CHECKED_BACK_COLOR);
    }

    /**
     * interface that need to be realized in the place where it is called
     */
    public interface SingleDateClickListener{
        void onDateClick(int year,int month,int date);
    }

    /**
     * This method needs to be called when SignCalendar is creating an instance to execute onClick()
     * without any barriers.
     * @param mSingleDateClickListener inner anonymous class or a class implementing this interface
     */
    public void setOnDateClickListener(SingleDateClickListener mSingleDateClickListener){
        this.mSingleDateClickListener=mSingleDateClickListener;
    }


    /***********************************************************************************************/
    /************************SignCalendarListener.onSignCalendarListener****************************/
    /***********************************************************************************************/

    /**
     * This synchronized method realizes getLastMonth() from SignCalendarListener where onFling() and
     * onScroll() is overrode.
     */
    @Override
    public synchronized void getLastMonth() {
        if(--curMonth<0){curYear--;curMonth=11;}else {}
        if(curYear==Calendar.getInstance().get(Calendar.YEAR)
                &&curMonth==Calendar.getInstance().get(Calendar.MONTH))curCheckedDay=curDay;
        else curCheckedDay=1;
        curLayout=getCalendarView();
        setBackColor4SingleDay(curCheckedDay,CHECKED_BACK_COLOR);
        addView(curLayout,0);
        setInAnimation(getContext(), R.anim.push_left_in);
        setOutAnimation(getContext(),R.anim.push_right_out);
        showPrevious();
        removeViewAt(1);
    }

    /**
     * This synchronized method realizes getNextMonth() from SignCalendarListener where onFling() and
     * onScroll() is overrode.
     */
    @Override
    public synchronized void getNextMonth() {
        if(++curMonth>11){curYear++;curMonth=0;}else {}
        if(curYear==Calendar.getInstance().get(Calendar.YEAR)
                &&curMonth==Calendar.getInstance().get(Calendar.MONTH))curCheckedDay=curDay;
        else curCheckedDay=1;
        curLayout=getCalendarView();
        setBackColor4SingleDay(curCheckedDay,CHECKED_BACK_COLOR);
        addView(curLayout,1);
        setInAnimation(getContext(),R.anim.push_right_in);
        setOutAnimation(getContext(),R.anim.push_left_out);
        showNext();
        removeViewAt(0);
    }
}
