package com.chat.im.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chat.im.R;

/**
 * 钱包
 */

public class WalletFragment extends Fragment {

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView(inflater);
        return mView;
    }

    private void initView(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_wallet, null);
    }
}
