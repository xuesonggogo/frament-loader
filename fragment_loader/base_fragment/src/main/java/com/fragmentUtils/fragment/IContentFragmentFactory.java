package com.fragmentUtils.fragment;


/**
 * 内容fragment工厂接口
 * @author lixuesong
 * 
 */
public interface IContentFragmentFactory {

	ContentFragment createFragment(int type);

	String toString(int type);

}
