package com.fragmentUtils.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;


/**
 * Created by lixuesong on 2018/5/23.
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
    public static String TAG;
    public CarFragmentManager mNaviFragmentManager; // fragment管理器 --tt
    public int frameID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityStack.addActivity(this);
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        setFrameID();
        mNaviFragmentManager = new CarFragmentManager(this, getView());
        BaseFragment.initBeforeAll(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.removeActivity(this);
    }

    @Override
    public void onBackPressed() {
        ContentFragment fragment = mNaviFragmentManager.getCurrentFragment();
        if (fragment != null) {
            return;
        }
        if (mNaviFragmentManager.getFragmentStackSize() > 0) {
            mNaviFragmentManager.back(null);
        }
    }

    /**
     * 获取fragment管理器
     *
     * @return fragment管理器
     */
    public CarFragmentManager getNaviFragmentManager() {
        return mNaviFragmentManager;
    }

    protected abstract void setFrameID();

    public int getView() {
        LogUtil.i(TAG, "getView frameID=" + frameID);
        return frameID;
    }

    public ContentFragment getFragment() {
        return null;
    }
}
