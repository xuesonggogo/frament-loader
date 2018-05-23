package com.fragmentUtils.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


/**
 * Created by lixuesong on 2018/5/23.
 */
public class CarFragmentManager extends ContentFragmentManager implements IContentFragmentFactory {

    public final static int TYPE_NONE = 0x0000;

    //首页

    /**
     * 前一个fragment 类型 ，即从哪个fragment跳转过来的
     */
    private int mPreviousFragmentType = TYPE_NONE;


    public CarFragmentManager(FragmentActivity activity, int id) {
        super(activity);
        setFrameID(id);
        setFragmentFactory(this);
    }

    @Override
    public ContentFragment createFragment(int type) {

        CommonFragment fragment = null;
        int index = findFragmentIndexInStack(type);
        if (index > -1) {
            removeFragmentFromStack(index);
        }
        Log.i("jl", "createFragment: type=" + type);
        switch (type) {
          //  fragment = new CommonFragment();

            default:
                if (fragment == null) {
                    type = TYPE_NONE;
                }
                break;
        }

        if (fragment != null) {
            fragment.setType(type);
        }
        return fragment;
    }

    @Override
    public String toString(int type) {
        String str = "fragment";
        return str;
    }

    public int getCurrentFragmentType() {
        if (null != mCurrentFragmentInfo && null != mCurrentFragmentInfo.mFragment) {
            return mCurrentFragmentInfo.mFragment.getType();
        }
        return TYPE_NONE;
    }

    /**
     * 获取前一个fragment类型，即从哪个fragment跳转过来的
     */
    public int getPreviousFragmentType() {
        return mPreviousFragmentType;
    }

    /**
     * 回退到type类型的fragment
     */
    public void backTo(int type, Bundle bundle) {

        int index = -1;
        for (int j = mFragmentInfoStack.size() - 1; j >= 0; j--) {
            if (mFragmentInfoStack.get(j).mType == type) {
                index = j;
            }
        }
        if (index == -1) {
            return;
        }

        for (int i = mFragmentInfoStack.size() - 1; i >= 0; i--) {
            if (mFragmentInfoStack.get(i).mType == type && i == index) {
                back(bundle);
                LogUtil.d("NaviFragmentManager2", "remove back " + i);
                break;

            } else if (mFragmentInfoStack.get(i).mType == type) {
                mFragmentInfoStack.remove(i);
                LogUtil.d("NaviFragmentManager2", "remove " + i);
            } else {
                removeFragmentFromStack(i);
                LogUtil.d("NaviFragmentManager2", "remove stack" + i);
            }
        }

    }


    /**
     * 清理HOME页之后压栈的所有fragment
     *
     * @author lixuesong
     */
    public void removeFragmentTo(int type) {
        int index = getIndexFromLast(type);
        if (index >= 0) {
            int stackSize = mFragmentInfoStack.size();
            for (int i = stackSize - 1; i > index; i--) {
                removeFragmentFromStack(i);
            }
        }
    }

    @Override
    public void showFragment(int type, Bundle bundle,ContentFragment ft) {


        if (bundle == null) {
            bundle = new Bundle();
        }


        super.showFragment(type, bundle, ft);

    }

    //返回到初始fragment，同时清理所有的fragment
    public void backToMainFragment(int type, Bundle bundle) {
        super.backToMainFragment(type, bundle);
    }

    /**
     * 从后往前，获取第一个类型是type的fragment在栈中的下标
     *
     * @param type fragment类型
     * @return 下标
     */
    private int getIndexFromLast(int type) {
        int i = mFragmentInfoStack.size() - 1;
        for (; i >= 0; i--) {
            if (mFragmentInfoStack.get(i).mType == type) {
                break;
            }
        }

        return i;
    }

    public boolean isNaviStart() {
        boolean isStart = false;
        for (FragmentInfo fragmentInfo : mFragmentInfoStack) {

        }

        return isStart;
    }
}
