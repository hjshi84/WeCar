package com.example.bine;

import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FriendslistFragment extends Fragment {  
	private ExpandableListView Elv;
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
    	 View view =inflater.inflate(R.layout.expandablelistview, null);
         Elv = (ExpandableListView) view.findViewById(R.id.expandableListView1);
         Elv.setAdapter(new MyExpandablelistViewAdapter());
        return view;  
    }  
    
    class MyExpandablelistViewAdapter extends BaseExpandableListAdapter{

    	private String[] groups = {"家人","公司","大陆"};
    	private String[][] childs = {{"爸爸","妈妈"},{"施黄骏","王晓晨","余征毅"},{"Loo","boss","Killer"}};

    	
    	@Override
    	public Object getChild(int arg0, int arg1) {
    		// TODO Auto-generated method stub
    		return childs[arg0][arg1];
    	}

    	@Override
    	public long getChildId(int arg0, int arg1) {
    		// TODO Auto-generated method stub
    		return arg1;
    	}

    	@Override
    	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
    			ViewGroup arg4) {
    		// TODO Auto-generated method stub
    		
            if(arg3 == null){
            	arg3 = getActivity().getLayoutInflater().inflate(R.layout.child, null);
            }
    		ImageView iv = (ImageView) arg3.findViewById(R.id.imageView1_child);
    		TextView tv = (TextView) arg3.findViewById(R.id.childTo);
//    		iv.setImageResource(resId);
    		tv.setText(childs[arg0][arg1]);
    		//20141211字体
    		tv.setTextSize(24);
    		
    		//20141211字体
    		//TextView groupText=new TextView(getActivity());   
    		//groupText.setText(groups[arg0]);//起始值是0   
    		//groupText.setTextSize(24);
            
    		return arg3;
    	}

    	@Override
    	public int getChildrenCount(int arg0) {
    		// TODO Auto-generated method stub
    		return childs[arg0].length;
    	}

    	@Override
    	public Object getGroup(int arg0) {
    		// TODO Auto-generated method stub
    		return groups[arg0];
    	}

    	@Override
    	public int getGroupCount() {
    		// TODO Auto-generated method stub
    		return groups.length;
    	}

    	@Override
    	public long getGroupId(int arg0) {
    		// TODO Auto-generated method stub
    		return arg0;
    	}

    	@Override
    	public View getGroupView(int arg0, boolean arg1, View arg2,
    			ViewGroup arg3) {
    		// TODO Auto-generated method stub
    		
    	     if(arg2 == null){
    					arg2 =  getActivity().getLayoutInflater().inflate(R.layout.group,null);
    				}
    				ImageView iv = (ImageView) arg2.findViewById(R.id.imageView1_group);
    				TextView tv =(TextView) arg2.findViewById(R.id.textview1_group);
    				tv.setText(groups[arg0]);
    				//20141211字体
    				tv.setTextSize(30);
//    				iv.setImageResource(resId);
    				
    				//20141211字体
    				//TextView groupText=new TextView(getActivity());   
    				//groupText.setText(groups[arg0]);//起始值是0   
    				//groupText.setTextSize(24);
    				
    				return arg2;
    	}



    	@Override
    	public boolean hasStableIds() {
    		// TODO Auto-generated method stub
    		return true;
    	}

    	@Override
    	public boolean isChildSelectable(int arg0, int arg1) {
    		// TODO Auto-generated method stub
    		return true;
    	}
    	
    }
} 

