package com.fragmentUtils.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;


/**
 * fragment管理器
 *
 * @author lixuesong
 */
public abstract class ContentFragmentManager {


    public static final String KEY_BACK_BUNDLE = "back_bundle"; // 用于标识bundle回退时传入的
    public static final String KEY_SHOW_BUNDLE = "show_bundle"; // 用于标识bundle显示时传入的
    public static final String KEY_FRAGMENT_TYPE = "key_fragment_type"; // 用于在传入fragment参数bundle里保存type值
    private static final String TAG = "ContentFragmentManager2";

    protected FragmentManager mFragmentManager; // fragment管理器
    protected ArrayList<FragmentInfo> mFragmentInfoStack; // fragment管理栈
    protected FragmentInfo mCurrentFragmentInfo; // 当前显示的fragment信息
    protected IContentFragmentFactory mContentFragmentFactory; // fragment工厂

    public void setFrameID(int frameID) {
        this.frameID = frameID;
    }

    private int frameID = 0;

    public ContentFragmentManager(FragmentActivity activity) {
        mFragmentManager = activity.getSupportFragmentManager();
        mFragmentInfoStack = new ArrayList<FragmentInfo>();
        mCurrentFragmentInfo = null;
    }

    /**
     * 设置fragment工厂
     *
     * @param contentFragmentFactory fragment工厂
     */
    public void setFragmentFactory(IContentFragmentFactory contentFragmentFactory) {
        mContentFragmentFactory = contentFragmentFactory;
    }

    /**
     * 显示类型为type的fragment，并根据saveLastFragment把前一个显示的fragment入栈与否
     *
     * @param type   fragment类型
     * @param bundle 上一次显示的fragment是否入栈
     */
    public void showFragment(int type, Bundle bundle,ContentFragment ft) {
        LogUtil.i("devin", "showFragment: type=" + type+",ft="+ft.getClass().getSimpleName());

        ContentFragment fragment = null;

        if (mContentFragmentFactory != null) {
            fragment =ft;// mContentFragmentFactory.createFragment(type);
        }

        if (fragment != null) {
            Bundle parentBundle = fragment.getArguments();
            if (parentBundle == null) {
                parentBundle = new Bundle();
                parentBundle.putInt(KEY_FRAGMENT_TYPE, type);// 此时注入type值，系统自动创建时刻恢复
                fragment.setArguments(parentBundle);
            }
            LogUtil.i(TAG, "showFragment: " + fragment.getClass().getName());
            if (bundle != null) {
                parentBundle.putBundle(KEY_SHOW_BUNDLE, bundle);
            }
        }

        if (mCurrentFragmentInfo != null) {
            push(mCurrentFragmentInfo);
        }
        replaceFragment(fragment, type, false);

        mCurrentFragmentInfo = new FragmentInfo(fragment, type);
        if (mCurrentFragmentInfo.mFragment != null) {
            mCurrentFragmentInfo.mFragment.requestInitView();
        }
    }


    public void showFragment(int type, Bundle bundle, boolean back) {
        LogUtil.i("devin", "showFragment: type=" + type);

        ContentFragment fragment = null;

        if (mContentFragmentFactory != null) {
            fragment = mContentFragmentFactory.createFragment(type);
        }

        if (fragment != null) {
            Bundle parentBundle = fragment.getArguments();
            if (parentBundle == null) {
                parentBundle = new Bundle();
                parentBundle.putInt(KEY_FRAGMENT_TYPE, type);// 此时注入type值，系统自动创建时刻恢复
                fragment.setArguments(parentBundle);
            }
            LogUtil.i(TAG, "showFragment: " + fragment.getClass().getName());
            if (bundle != null) {
                parentBundle.putBundle(KEY_SHOW_BUNDLE, bundle);
            }
        }

        if (mCurrentFragmentInfo != null) {
            push(mCurrentFragmentInfo);
        }
        replaceFragment(fragment, type, back);

        mCurrentFragmentInfo = new FragmentInfo(fragment, type);
        if (mCurrentFragmentInfo.mFragment != null) {
            mCurrentFragmentInfo.mFragment.requestInitView();
        }
    }

    /**
     * 回退一个fragment
     */
    public void back(Bundle bundle) {
        FragmentInfo fragmentInfo = null;

        fragmentInfo = popFragment();
        if (fragmentInfo == null)
            return;
        if (fragmentInfo.mFragment != null) {
            /* 同一个fragment不可重复setArguments */
            Bundle parentBundle = fragmentInfo.mFragment.getArguments();
            if (parentBundle != null) {
                if (bundle != null)
                    parentBundle.putBundle(KEY_BACK_BUNDLE, bundle);
                else
                    parentBundle.remove(KEY_BACK_BUNDLE);
            }
        }

        replaceFragment(fragmentInfo.mFragment, fragmentInfo.mType, true);

        mCurrentFragmentInfo = fragmentInfo;
        if (mCurrentFragmentInfo.mFragment != null)
            mCurrentFragmentInfo.mFragment.requestRestoreView();
    }

    /**
     * 同back（null）
     */
    public void back() {
        back(null);
    }

    /**
     * 获取当前显示的fragment
     *
     * @return 返回fragment
     */
    public ContentFragment getCurrentFragment() {
        if (mCurrentFragmentInfo == null)
            return null;

        return mCurrentFragmentInfo.mFragment;
    }

    /**
     * 获取当前显示的fragment类型
     *
     * @return 返回fragment类型
     */
    public int getCurrentFragmentType() {
        if (mCurrentFragmentInfo == null)
            return 0;

        return mCurrentFragmentInfo.mType;
    }

    /**
     * 获取fragment栈的大小
     *
     * @return 返回fragment栈大小
     */
    public int getFragmentStackSize() {
        return mFragmentInfoStack.size();
    }

    /**
     * 获取栈中的第index个fragment
     *
     * @param index 指定栈中的fragment下标
     * @return 返回fragment
     */
    public ContentFragment getFragmentInStack(int index) {
        if (index >= mFragmentInfoStack.size())
            return null;

        return mFragmentInfoStack.get(index).mFragment;
    }

    /**
     * 获取栈中的第index个fragment类型
     *
     * @param index 指定栈中的fragment下标
     * @return 返回fragment类型
     */
    public int getFragmentTypeInStack(int index) {
        if (index >= mFragmentInfoStack.size())
            return -1;

        return mFragmentInfoStack.get(index).mType;
    }

    /***
     * 从Fragment栈中找到指定type的Fragment，返回其在栈中的Index
     * @param type
     * @return
     */
    public int findFragmentIndexInStack(int type) {
        for (int i = 0; i < mFragmentInfoStack.size(); i++) {
            if (mFragmentInfoStack.get(i).mType == type) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 从栈中移除第index个fragment
     *
     * @param index 指定栈中的fragment下标
     * @return 返回fragment类型
     */
    protected int removeFragmentFromStack(int index) {
        if (index >= mFragmentInfoStack.size())
            return -1;

        FragmentInfo fragmentInfo = mFragmentInfoStack.remove(index);
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.remove(fragmentInfo.mFragment);
        ft.commitAllowingStateLoss();
        return fragmentInfo.mType;
    }

    /**
     * 从页面栈中去掉所有指定类型的页面
     *
     * @param fragmentType
     */
    public void removeAllFragmentByType(int fragmentType) {
        int index = findFragmentIndexInStack(fragmentType);
        while (index >= 0) {
            removeFragmentFromStack(index);
            index = findFragmentIndexInStack(fragmentType);
        }

    }


    public void backToMainFragment(int type, Bundle bundle) {

        int size = mFragmentInfoStack.size();
        if (size <= 0) {
            return;
        }
        FragmentInfo fragmentInfo = null;
        for (int index = size - 1; index >= 0; index--) {
            //  mFragmentInfoStack.remove(i);
            fragmentInfo = mFragmentInfoStack.remove(index);
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.remove(fragmentInfo.mFragment);
            ft.commitAllowingStateLoss();
        }
        showFragment(type, bundle, true);
    }

    /**
     * 从页面栈中去掉所有页面
     */
    public void removeAllFragment() {
        int size = mFragmentInfoStack.size();
        if (size <= 0) {
            return;
        }

        for (int index = size - 1; index >= 0; index--) {
            //  mFragmentInfoStack.remove(i);
            FragmentInfo fragmentInfo = mFragmentInfoStack.remove(index);
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.remove(fragmentInfo.mFragment);
            ft.commitAllowingStateLoss();
            break;
        }


    }

    /**
     * fragment入栈
     *
     * @param fragmentInfo 需要入栈的fragment
     */
    protected void push(FragmentInfo fragmentInfo) {
        if (fragmentInfo == null)
            return;

        mFragmentInfoStack.add(fragmentInfo);
        logFragmentStack();
    }

    /**
     * fragment出栈 - 弹出离站栈顶最近的页面
     *
     * @return 返回出栈的fragment
     */
    protected FragmentInfo popFragment() {
        int size = mFragmentInfoStack.size();
        if (size <= 0)
            return null;
        FragmentInfo fragmentInfo = null;
        for (int i = size - 1; i >= 0; i--) {
            fragmentInfo = mFragmentInfoStack.remove(i);
            break;
        }

        return fragmentInfo;
    }

    /**
     * 打印fragment栈日志信息（调试用）
     */
    protected void logFragmentStack() {
        String fragmentStackStr = "fragment in stack: [";
        if (mContentFragmentFactory != null) {
            int size = mFragmentInfoStack.size();
            for (int i = 0; i < size; i++) {
                fragmentStackStr += mContentFragmentFactory.toString(mFragmentInfoStack.get(i).mType);
                if (i < mFragmentInfoStack.size() - 1)
                    fragmentStackStr += ", ";
            }
        }
        fragmentStackStr += "]";

        LogUtil.d(TAG, fragmentStackStr);
    }

    /**
     * fragment信息类 记录fragment对象，类型
     *
     * @author lixuesong
     */
    protected class FragmentInfo {
        public ContentFragment mFragment; // fragment对象
        public int mType; // 类型

        public FragmentInfo(ContentFragment fragment, int type) {
            mFragment = fragment;
            mType = type;
        }
    }


    /**
     * 替换fragment
     *
     * @param fragment 新的fragment
     */
    protected void replaceFragment(ContentFragment fragment, int type, boolean back) {
        // 允许空fragment的替换
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ContentFragment currFragment = null;
        if (mCurrentFragmentInfo != null && mCurrentFragmentInfo.mFragment != null) {
            currFragment = mCurrentFragmentInfo.mFragment;
        }

        if (back) {
            if (currFragment != null) {
                ft.remove(currFragment);
                LogUtil.i(TAG, "LXS fragment.isAdded()=" + fragment.isAdded());
                if (fragment.isAdded()) {
                    LogUtil.i(TAG, "LXS isAdded1");
                    ft.show(fragment);
                } else if (fragment.isDetached()) {
                    LogUtil.i(TAG, "LXS attach1");
                    ft.attach(fragment);
                } else {
                    if (!fragment.isAdded() && !fragment.isAddFt()) {
                        LogUtil.i(TAG, "LXS else1");
                        ft.add(frameID, fragment);
                        fragment.setAddFt(true);
                    } else {
                        LogUtil.i(TAG, "LXS else2");
                    }
                }
            }

        } else {
            LogUtil.i(TAG, "replaceFragment: currFragment="+currFragment);
            if (currFragment != null) {
                ft.detach(currFragment);
            }

            if (fragment.isAdded()) {
                ft.show(fragment);
                LogUtil.i(TAG, "replaceFragment: fragment.isAdded()");
            } else if (fragment.isDetached()) {
                ft.attach(fragment);
                LogUtil.i(TAG, "replaceFragment: fragment.isDetached()");
            } else {
                if (!fragment.isAdded() && !fragment.isAddFt()) {
                    ft.add(frameID, fragment);
                    LogUtil.i(TAG, "replaceFragment: fragment.isDetached(===========else)");
                    fragment.setAddFt(true);
                }
            }
        }
        ft.commitAllowingStateLoss();
//        ft.commit();
    }

}
