package com.chat.im.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 监听输入法软键盘是否显示的自定义RelativeLayout
 */
public class ResizeRelativeLayout extends RelativeLayout {

    public ResizeRelativeLayout(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
    }

    public ResizeRelativeLayout(Context context) {
        super(context);
    }

    public ResizeRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWith, int oldHeight) {
        super.onSizeChanged(w, h, oldWith, oldHeight);
        if (mListener != null) {
            mListener.onResizeRelative(w, h, oldWith, oldHeight);
        }
    }

    /**
     * 监听软键盘是否显示接口
     */
    private OnResizeRelativeListener mListener;

    public interface OnResizeRelativeListener {

        /**
         * 控件宽高改变
         *
         * @param w         Current width of this view.
         * @param h         Current height of this view.
         * @param oldWith   Old width of this view.
         * @param oldHeight Old height of this view.
         */
        void onResizeRelative(int w, int h, int oldWith, int oldHeight);
    }

    /**
     * 设置软键盘是否显示的监听器
     *
     * @param listener 监听器
     */
    public void setOnResizeRelativeListener(OnResizeRelativeListener listener) {
        mListener = listener;
    }

}
