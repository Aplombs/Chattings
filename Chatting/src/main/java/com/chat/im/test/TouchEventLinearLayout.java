package com.chat.im.test;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public class TouchEventLinearLayout extends LinearLayout {

    private String TAG = "[LinearLayout] ";

    public TouchEventLinearLayout(Context context) {
        super(context);
    }

    public TouchEventLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchEventLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TouchEventLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        LogHelper.e(TAG + "dispatchTouchEvent() action:" + ev.getAction());
//        return super.dispatchTouchEvent(ev);
//    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        LogHelper.e(TAG + "onInterceptTouchEvent() action:" + ev.getAction());
//        return super.onInterceptTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        LogHelper.e(TAG + "onTouchEvent() action:" + event.getAction());
//        return super.onTouchEvent(event);
//    }
}