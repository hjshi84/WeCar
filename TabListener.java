package com.example.bine;

import android.app.Activity;
import android.app.Fragment;


public class TabListener<T extends Fragment> implements android.app.ActionBar.TabListener {  
    private Fragment mFragment;  
    private final Activity mActivity;  
    private final String mTag;  
    private final Class<T> mClass;  
  
    public TabListener(Activity activity, String tag, Class<T> clz) {  
        mActivity = activity;  
        mTag = tag;  
        mClass = clz;  
    }  
  
	@Override
	public void onTabReselected(android.app.ActionBar.Tab arg0,
			android.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
        if (mFragment == null) {  
            mFragment = Fragment.instantiate(mActivity, mClass.getName());  
            arg1.add(android.R.id.content, mFragment, mTag);  
        } else {  
            arg1.attach(mFragment);  
        }  
	}

	@Override
	public void onTabSelected(android.app.ActionBar.Tab arg0,
			android.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
	      if (mFragment == null) {  
	            mFragment = Fragment.instantiate(mActivity, mClass.getName());  
	            arg1.add(android.R.id.content, mFragment, mTag);  
	        } else {  
	            arg1.attach(mFragment);  
	        } 
	}

	@Override
	public void onTabUnselected(android.app.ActionBar.Tab arg0,
			android.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
        if (mFragment != null) {  
            arg1.detach(mFragment);  
        }  
		
	}  
}