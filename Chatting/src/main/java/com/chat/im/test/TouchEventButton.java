package com.chat.im.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class TouchEventButton extends View {

    private String TAG = "[Button] ";

    public TouchEventButton(Context context) {
        super(context);
    }

    public TouchEventButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchEventButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TouchEventButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        LogHelper.e(TAG + "dispatchTouchEvent() action:" + ev.getAction());
//        return super.dispatchTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        LogHelper.e(TAG + "onTouchEvent() action:" + event.getAction());
//        return super.onTouchEvent(event);
//    }
}