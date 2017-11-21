package com.chat.im.test.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chat.im.R;
import com.chat.im.helper.LogHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestFragment extends Fragment {

    @BindView(R.id.test_button)
    Button testButton;
    @BindView(R.id.test_listview)
    ListView mListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        final BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LogHelper.e("======getView()======");
                TextView textView = new TextView(getContext());
                textView.setText("dddddd");
                return textView;
            }
        };
        mListView.setAdapter(baseAdapter);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseAdapter.notifyAll();
            }
        });
    }
}
