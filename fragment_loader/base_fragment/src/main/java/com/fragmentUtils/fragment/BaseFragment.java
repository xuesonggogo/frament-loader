package com.fragmentUtils.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragment基类
 *
 * @author lixuesong
 */
public abstract class BaseFragment extends Fragment {

    private static String TAG = "BaseFragment";
    protected static BaseActivity mActivity; // 依附的activity
    protected static Context mContext; // 上下文
    protected static CarFragmentManager mNaviFragmentManager; // fragment管理器
    protected boolean mViewCreated = false; // 视图生成标志

    public static void initBeforeAll(BaseActivity activity) {
        mActivity = activity;
        mNaviFragmentManager = mActivity.getNaviFragmentManager();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getClass().getSimpleName();
        // LogUtil.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LogUtil.d(TAG, "onCreate");
        mActivity = (BaseActivity) getActivity();
        mNaviFragmentManager = mActivity.getNaviFragmentManager();
        mContext = mActivity.getApplication().getApplicationContext();
        setHasOptionsMenu(true); // 允许fragment修改menu
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // LogUtil.d(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // LogUtil.d(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        //   LogUtil.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();

        //LogUtil.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        // LogUtil.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        // LogUtil.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // LogUtil.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // LogUtil.d(TAG, "onDestroy");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        // LogUtil.d(TAG, "onDetach");
    }

    public static CarFragmentManager getNaviFragmentManager() {
        return mNaviFragmentManager;
    }


    protected int fragmentType;

    public int getType() {
        return fragmentType;
    }

    public void setType(int fragmentType) {
        this.fragmentType = fragmentType;
    }

    public Context getContext() {
        return mContext;
    }


    public boolean isRegisterEventBus() {
        return false;
    }


}
