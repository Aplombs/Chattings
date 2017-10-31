package com.chat.im.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.chat.im.R;
import com.chat.im.helper.LogHelper;

public class TestOnTouchEventActivity extends Activity implements View.OnClickListener, View.OnTouchListener {
    private String TAG = "[Activity] ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_on_touch_event);

        //findViewById(android.R.id.content).setOnTouchListener(this);
        //findViewById(R.id.touch_linear_layout).setOnTouchListener(this);
        //findViewById(R.id.touch_button).setOnTouchListener(this);

        //findViewById(android.R.id.content).setOnClickListener(this);
        //findViewById(R.id.touch_linear_layout).setOnClickListener(this);
        //findViewById(R.id.touch_button).setOnClickListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogHelper.e(TAG + "dispatchTouchEvent() action:" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogHelper.e(TAG + "onTouchEvent() action:" + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogHelper.e(TAG + "onUserInteraction()");
    }

    @Override
    public void onClick(View v) {
        LogHelper.e(TAG + "onClick()--->>" + v.getClass().getName());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        LogHelper.e(TAG + "onTouch()--->>" + v.getClass().getName() + "--->> action:" + event.getAction());
        return false;
    }
}
