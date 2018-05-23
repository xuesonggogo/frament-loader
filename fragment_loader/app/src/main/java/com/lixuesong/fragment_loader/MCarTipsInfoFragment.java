package com.lixuesong.fragment_loader;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentUtils.fragment.ContentFragment;

/**
 * Created by lixuesong on 2018/5/23.
 */

public class MCarTipsInfoFragment extends ContentFragment {

    private String TAG = MCarTipsInfoFragment.class.getSimpleName();
    private ViewGroup mRootView;

    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_tips_info, null);
        return mRootView;
    }


    @Override
    protected void onInitView() {


    }

    @Override
    public void onCreateOther() {

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
