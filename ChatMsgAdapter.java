package com.example.bine;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatMsgAdapter extends BaseAdapter{
	private static final String TAG = ChatMsgAdapter.class.getSimpleName();
	private  ArrayList<chatMsg>  list;  
	private  Context  context;  
	
	public ChatMsgAdapter(ArrayList<chatMsg> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public int getItemViewType(int position) {  
        return position;  
    }  

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		chatMsg msg = list.get(position);  
        int itemlayout = msg.getLayoutID();  
        LinearLayout   layout = new LinearLayout(context);  
        LayoutInflater  vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        vi.inflate(itemlayout, layout,true);  
        TextView  tvName ;  
        TextView  tvText ;  
        
        ImageView lvpic;
        if (msg.thismsg.from.contentEquals("me"))
        {
        	lvpic=(ImageView) layout.findViewById(R.id.chatlist_image_me);
        	tvText  =(TextView) layout.findViewById(R.id.chatlist_text_me);
        	tvName = (TextView) layout.findViewById(R.id.chatlist_identify_me);
        	tvText.setText(msg.thismsg.contextinfo);  
        	tvName.setText(msg.thismsg.from); 
        }
        else
        {
        	lvpic=(ImageView) layout.findViewById(R.id.chatlist_image_other);
        	tvText  =(TextView) layout.findViewById(R.id.chatlist_text_other);
        	tvName = (TextView) layout.findViewById(R.id.chatlist_identify_other);
        	tvText.setText(msg.thismsg.contextinfo);  
        	tvName.setText(msg.thismsg.from); 
        }
        tvText.setTextColor(Color.BLACK);
        tvText.setTextScaleX(1);
        lvpic.setBackgroundResource(msg.picid);
        tvText.setBackgroundResource(R.drawable.ballon);
        return layout;  
	}
	
	public int getViewTypeCount() {  
        return list.size();  
    }
	
}
