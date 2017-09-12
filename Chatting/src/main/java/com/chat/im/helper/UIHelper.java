package com.chat.im.helper;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.chat.im.R;


/**
 * 界面弹框或Toast提示工具类
 */

public class UIHelper {

    private static Toast mToast;
    private static UIHelper uiTipHelper;

    private UIHelper() {
    }

    public static UIHelper getInstance() {
        if (null == uiTipHelper) {
            synchronized (UIHelper.class) {
                if (null == uiTipHelper) {
                    uiTipHelper = new UIHelper();
                }
            }
        }
        return uiTipHelper;
    }

    public static void clearCache() {
        uiTipHelper = null;
        mToast = null;
    }

    public void toast(String content) {
        if (ContextHelper.getContext() == null) {
            throw new RuntimeException("context is null,use context must to invoke init() in your application!");
        }

        if (mToast != null) {
            mToast.setText(content);
        } else {
            mToast = Toast.makeText(ContextHelper.getContext(), content, Toast.LENGTH_SHORT);
        }

        mToast.show();
    }

    public boolean checkNetwork() {
        if (!UtilsHelper.getInstance().isNetworkConnected()) {
            UIHelper.getInstance().toast("当前网络不可用");
            return false;
        }
        return true;
    }

    public Dialog createLoadingDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setContentView(R.layout.dialog_lodding);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams dialogLayoutParams = window.getAttributes();
            dialogLayoutParams.dimAmount = 0;
            dialogLayoutParams.width = UtilsHelper.getInstance().dp2px(context, 120);
            dialogLayoutParams.height = UtilsHelper.getInstance().dp2px(context, 120);
            window.setAttributes(dialogLayoutParams);
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void hideSoftInput(EditText editText) {
        if (editText == null)
            return;

        editText.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) ContextHelper.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public void showSoftInput(EditText editText) {
        if (editText == null)
            return;

        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) ContextHelper.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(editText, 0);
        }
    }
}
