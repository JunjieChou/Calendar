package com.olituc.standard.listener;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SignCalendarListener extends GestureDetector.SimpleOnGestureListener {
    private onSignCalendarListener mOnSignCalendarListener=null;

    public void setmOnSignCalendarListener(onSignCalendarListener mOnSignCalendarListener) {
        this.mOnSignCalendarListener = mOnSignCalendarListener;
    }

    /**
     * This override function is to switch the calendar which is actually a ViewFlipper to last page
     * or next page regarding to the distanceX of onScroll Gesture. If the distanceX is bigger than
     * 200dp, this function will call getNextMonth() of interface onSignCalendarListener and return
     * true; If the distanceX is smaller than -200dp, this function will call getLastMonth() and
     * return true; Otherwise it will return false, which means no function will be executed.
     * @param e1 the onDown status
     * @param e2 the latest onUp status
     * @param distanceX e1.getX()-e2.getX()
     * @param distanceY e1.getY()-e2.getY()
     * @return true or false depending on the process
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(mOnSignCalendarListener==null) return super.onScroll(e1,e2,distanceX,distanceY);
    }

    /**
     * This override function is to switch the calendar which is actually a ViewFlipper to last page
     * or next page regarding to the velocityX of onFling Gesture. If the velocityX is bigger than
     * 100(px/s), this function will call getNextMonth() of interface onSignCalendarListener and return
     * true; If the velocityX is smaller than -100, this function will call getLastMonth() and return
     * true; Otherwise it will return false, which means no function will be executed.
     * @param e1 the onDown status
     * @param e2 the latest onUp status
     * @param velocityX the X speed of fling
     * @param velocityY the Y speed of fling
     * @return true or false depending on the process
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(mOnSignCalendarListener==null)return super.onFling(e1, e2, velocityX, velocityY);
        if((int)velocityX>150){
            mOnSignCalendarListener.getNextMonth();
            return true;
        }
        if((int)velocityX<(-150)){
            mOnSignCalendarListener.getLastMonth();
            return true;
        }
        return false;
    }

    /**
     * This interface will be realized in the class SignCalendar
     */
    public interface onSignCalendarListener{
        void getLastMonth();
        void getNextMonth();
    }
}
