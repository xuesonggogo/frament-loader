package com.fragmentUtils.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 内容fragment基类
 *
 * @author lixuesong
 */
public abstract class ContentFragment extends BaseFragment {

    public static String TAG = "ContentFragment";

    protected View mContentView; // 内容视图

    protected boolean mNeedInitView = false;
    protected boolean mNeedRetoreView = false;

    protected boolean isResumed = false;
    protected boolean isAddFt = false;

    public BaseActivity activity;
    public boolean isDestroy = false;
    public boolean isRegister = false;

    public ContentFragment() {
        isResumed = false;
    }

    public boolean isAddFt() {
        return isAddFt;
    }

    public void setAddFt(boolean isAddFt) {
        this.isAddFt = isAddFt;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        isAddFt = false;
        Bundle bundle = getArguments();
        activity = (BaseActivity) getActivity();
        if (mContentView != null) {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
            mViewCreated = true;

        } else {
            mContentView = onCreateContentView(inflater);
        }
        if (mContentView != null) {
            mContentView.setClickable(true);
        }

        onInitFocusAreas();
        onInit();
        if (!mViewCreated) {
            onCreateOther();
        }

        mViewCreated = true;
        return mContentView;
    }

    public abstract void onCreateOther();

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }


    protected void onInitFocusAreas() {

    }


    @Override
    public void onDestroyView() {
        mViewCreated = false;
        mNeedInitView = false;
        mNeedRetoreView = false;
        super.onDestroyView();
    }

    /**
     * 要求初始化视图，若视图已生成，则立刻初始化，否则延迟到onActivityCreated中初始化
     */
    public void requestInitView() {
        if (mViewCreated) {
            onInitView();
        } else {
            mNeedInitView = true;
        }

    }

    /**
     * 回退时处理页面数据
     */
    public void requestRestoreView() {

    }

    /**
     * 初始化函数
     */
    protected void onInit() {
        /* 初始化视图，可以包含地图 */
        if (mNeedInitView) {
            onInitView();
            mNeedInitView = false;
        }
        if (mNeedRetoreView) {

            mNeedRetoreView = false;
        }
    }

    /**
     * 生成内容fragment视图
     *
     * @param inflater 布局展开对象
     * @return 返回内容fragment视图
     */
    protected abstract View onCreateContentView(LayoutInflater inflater);

    /**
     * 获取动画的总共时间
     *
     * @param animList 动画列表
     * @return 返回动画的总共时间
     */
    protected long getAnimationTotalDuration(Collection<Animation> animList) {

        long duration = 0;
        if (animList != null) {
            for (Animation anim : animList) {
                duration = Math.max(anim.getStartOffset() + anim.getDuration(), duration);
            }
        }

        return duration;
    }


    /**
     * 开始animMap中的所有动画
     *
     * @param animMap 动画映射表
     */
    protected void startAnimation(Map<View, Animation> animMap) {
        if (animMap == null) {
            return;
        }

        Set<View> viewList = animMap.keySet();
        Animation anim = null;
        for (View view : viewList) {
            anim = animMap.get(view);
            if (anim != null && view != null) {
                view.startAnimation(anim);
            }
        }
    }

    /**
     * 初始化视图
     */
    protected abstract void onInitView();


    protected void onRightClick() {

    }

    protected void onLeftClick() {
        mNaviFragmentManager.back();

    }

    protected void onLeftClick(Bundle bundle) {

        mNaviFragmentManager.back(bundle);

    }

    protected void toMain() {
        //   mNaviFragmentManager.backToMainFragment(CarFragmentManager.TYPE_CAR_INFO, null);
    }

    @Override
    public void onResume() {
        super.onResume();

        isDestroy = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getNeedRegisterregister() && isRegister) {
            isRegister = false;
        }
    }

    public boolean getNeedRegisterregister() {
        return false;
    }


}
