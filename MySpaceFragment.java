package com.example.bine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import com.threed.jpct.Object3D;

import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MySpaceFragment extends Fragment {  
	private static MyGLSurfaceView glView;  
	private MyRenderer mr;  
	private float mPreviousX;
    private float mPreviousY;
    private float mAngleX,mAngleY,mzoom;
    private boolean isZoom = false;
    private float oldDist;    
	private final float TOUCH_SCALE_FACTOR = 180.0f / (32000);
	public static Handler mHandler;  
	EditText inputtext;
	Button inputbutton;
	static MiniData md=new MiniData();
	ListView listView;
	RelativeLayout inputlayout;
	private static CarObj chosenPart=null;
	ChatMsgAdapter mycma;
	ArrayList<chatMsg> dataset;

    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  

    	final RelativeLayout layout = new RelativeLayout(getActivity());  
        //layout.setOrientation(LinearLayout.VERTICAL);;  
        //LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        final LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, 400);
		
        
		new LoadAssets(getResources());
		
		glView=new MyGLSurfaceView(getActivity());

		
        glView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {  
            public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {  
                // Ensure that we get a 16bit framebuffer. Otherwise, we'll fall  
                // back to Pixelflinger on some device (read: Samsung I7500)  
                int[] attributes = new int[] { EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE };  
                EGLConfig[] configs = new EGLConfig[1];  
                int[] result = new int[1];  
                egl.eglChooseConfig(display, attributes, configs, 1, result);  
                return configs[0];  
            }			
        });  
        mr = new MyRenderer();  
        glView.setRenderer(mr);  

        mHandler=new Handler(){
         	 public void handleMessage(Message msg){
         		 switch (msg.what){
	         		 case 123:         	        
	         			layout.addView(glView,params); 		
	         			break;
	         		 case 234:
	         			 if (chosenPart!=null)
	         				 inputtext.setHint(chosenPart.name);
	         			 break;
	         		 case 789:
	         			md.addinfo(MainActivity.receivevalue);
	         			dataset.clear();
						dataset=translateChatMsg(md);
						mycma=new ChatMsgAdapter(dataset,getActivity().getApplicationContext());
					    listView.setAdapter(mycma);
						mycma.notifyDataSetChanged();
	         			break;
	         	     default:
	         	    	 break;
         		 }
         	 }
         };
         
         new Thread() {
             public void run() {
            	LoadImage.loadi(getResources()); 
                mr.load("testlow.3DS",1);
           	 
   				Message message = new Message();  
   	          	message.what=123;// TODO
   	          	mHandler.sendMessage(message);
             }
         }.start();//
         new Thread(){
        	 public void run() {
        		 while(true)
        		 {
	             	if (MainActivity.receivedata==true)
	             	{
	             		Message message = new Message();  
	       	          	message.what=789;// TODO
	       	          	mHandler.sendMessage(message);
	       	          	MainActivity.receivedata=false;
	             	}
        		 }
              }
         }.start();
         
	     inputlayout = new RelativeLayout(getActivity());  
         RelativeLayout.LayoutParams inputparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
         inputparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
         //inputparams.addRule(RelativeLayout.BELOW,listView.getId());
         DisplayMetrics  dm = new DisplayMetrics();    
         getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);    
         int screenWidth = dm.widthPixels;    
         int screenHeight = dm.heightPixels;
         
         
         
         //listview
         
	     listView = new ListView(getActivity());
	     listView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(glView.getVisibility()==View.VISIBLE)
					glView.setVisibility(View.INVISIBLE);
				else 
					glView.setVisibility(View.VISIBLE);
				return false;
			}
	    	 
	     });
	     dataset=translateChatMsg(md);
	     mycma=new ChatMsgAdapter(dataset,getActivity().getApplicationContext());
	     listView.setAdapter(mycma);
	     
         RelativeLayout.LayoutParams lvparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, screenHeight*7/10);
         lvparams.addRule(RelativeLayout.BELOW,glView.getId());
         lvparams.addRule(RelativeLayout.ABOVE,inputlayout.getId());
	     layout.addView(listView,lvparams);
	     inputtext = new EditText(getActivity());
         RelativeLayout.LayoutParams textparams = new RelativeLayout.LayoutParams(screenWidth*4/5, LayoutParams.WRAP_CONTENT);
         textparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
         textparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
         
         inputbutton = new Button(getActivity());
         RelativeLayout.LayoutParams btparams = new RelativeLayout.LayoutParams(screenWidth/5, LayoutParams.WRAP_CONTENT);
         btparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
         btparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
         btparams.addRule(RelativeLayout.RIGHT_OF, inputtext.getId());
         btparams.addRule(RelativeLayout.ALIGN_BOTTOM,inputtext.getId());
         btparams.addRule(RelativeLayout.ALIGN_TOP,inputtext.getId());
         inputbutton.setText("Send");;
         inputbutton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (chosenPart!=null&&inputbutton.getText().toString()!=null)
				{
					totalinfo temp= new totalinfo("me","me",chosenPart,"   "+inputtext.getText().toString(),System.currentTimeMillis());
					md.addinfo(temp);
					dataset.clear();
					dataset=translateChatMsg(md);
					mycma=new ChatMsgAdapter(dataset,getActivity().getApplicationContext());
				    listView.setAdapter(mycma);
					mycma.notifyDataSetChanged();
				}
				else if (inputbutton.getText().toString()!=null)
				{

					totalinfo temp= new totalinfo("me","me",chosenPart,"   "+inputtext.getText().toString(),System.currentTimeMillis());
					md.addinfo(temp);
					dataset.clear();
					dataset=translateChatMsg(md);
					mycma=new ChatMsgAdapter(dataset,getActivity().getApplicationContext());
				    listView.setAdapter(mycma);
					mycma.notifyDataSetChanged();
				}
				inputtext.setText("");
				
			}
        	 
         });
         inputlayout.addView(inputtext,textparams);
         inputlayout.addView(inputbutton,btparams);
         
         layout.addView(inputlayout,inputparams);
         return layout;  
    }  
    private ArrayList<chatMsg> translateChatMsg(MiniData md2) {
		// TODO Auto-generated method stub
    	if (md2==null) return null;
    	ArrayList<chatMsg> rtnval=new ArrayList<chatMsg>();
    	for(totalinfo i:md2.alldata)
    	{
    		if (i.to.contentEquals("me"))
    		{
    			int rID;
    			if (i.from.contentEquals("me"))
    				rID=R.layout.i_send;
    			else rID=R.layout.send_to_i;
    			chatMsg temp=new chatMsg(i,rID);
    			rtnval.add(temp);
    		}
    		
    	}
		return rtnval;
	}
    

    
	class MyGLSurfaceView extends GLSurfaceView{

		public MyGLSurfaceView(Context mySpaceFragment) {
			super(mySpaceFragment);
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
		 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
		 */
		@Override
		public boolean onTouchEvent(MotionEvent e) {
			mr.setcount(1);
			float newDist=0;
	    	float x = e.getX();
	        float y = e.getY();
	        String objname=null;
			  switch (e.getAction() & MotionEvent.ACTION_MASK) {  
			  
			    case MotionEvent.ACTION_CANCEL:
			    	mr.setunmuber(-1);
			    	break;
		        
		        case MotionEvent.ACTION_DOWN:   
		        	if (mr.JudgeObjects(e.getX(), e.getY()).length>=2){
		        		Object3D thisobj=(Object3D)(mr.JudgeObjects(e.getX(), e.getY()))[1];
		        		if (thisobj!=null)
		        			objname=thisobj.getName();
		        		else objname=null;
		        	}
		        	if (objname!=null)
		        	{
		        		Log.e("this is ", objname);
		        		chosenPart=new CarObj(objname,getCarPart(objname));
		        		/*if (objname.contains("HDM_01_03_")) Toast.makeText(getContext(), "body!", Toast.LENGTH_SHORT).show();
		        		else if (objname.contains("HDM_01_032")) Toast.makeText(getContext(), "underbody!", Toast.LENGTH_SHORT).show();
		        		else if (objname.contains("HDM_01_039")||objname.contains("HDM_01_012")) Toast.makeText(getContext(), "glass!", Toast.LENGTH_SHORT).show();
		        		else if (objname.contains("HDM_01_028")) Toast.makeText(getContext(), "mirror!", Toast.LENGTH_SHORT).show();
		        		else if (objname.contains("HDM_01_031")) Toast.makeText(getContext(), "hood!", Toast.LENGTH_SHORT).show();
		        		else if (objname.contains("HDM_01_033")) Toast.makeText(getContext(), "headlight!", Toast.LENGTH_SHORT).show();
		        		else if (objname.contains("HDM_01_067")||objname.contains("HDM_01_062")||objname.contains("HDM_01_022")||objname.contains("HDM_01_064")||objname.contains("HDM_01_058")
		        				||objname.contains("HDM_01_066")||objname.contains("HDM_01_043")||objname.contains("HDM_01_065")) 
		        			Toast.makeText(getContext(), "tire!", Toast.LENGTH_SHORT).show();
		        		else if (objname.contains("HDM_01_014")||objname.contains("HDM_01_074")) Toast.makeText(getContext(), "backlight!", Toast.LENGTH_SHORT).show();
		        		else if (objname.contains("HDM_01_073")) Toast.makeText(getContext(), "backregister!", Toast.LENGTH_SHORT).show();
		        		else Toast.makeText(getContext(), "unknown!", Toast.LENGTH_SHORT).show();*/
		        		totalinfo temp=md.findinfo("me", chosenPart, System.currentTimeMillis());
		        		if (temp!=null);
		        		{
		        			//Log.e("totoalinfo", temp.toString());
		        			Toast.makeText(getContext(), temp.contextinfo, Toast.LENGTH_LONG).show();
		        		}
		        	}
		        	else 
		        	{
		        		
		        		chosenPart=null;
		        	}
		        	
		        	Message message = new Message();  
	   	          	message.what=234;
	   	          	mHandler.sendMessage(message);
		            break;  
		            
		        case MotionEvent.ACTION_POINTER_UP:  
		            isZoom = false;  
		            break;  
		            
		        //指非第一个点按下 
		        case MotionEvent.ACTION_POINTER_DOWN:  
		            oldDist = spacing(e);  
		            
		            isZoom = true;  
		            break;  
		            
		        case MotionEvent.ACTION_MOVE:  
	        		float dx = x - mPreviousX;
		            float dy = y - mPreviousY;
		            if (isZoom) {  
		            	if (e.getPointerCount()==2){
		            		mr.setmode(1);
		            		newDist = spacing(e);                   
		            		if (oldDist>10)
		            			mzoom=newDist/oldDist;
		            		else mzoom=1;
		            		mr.settranslate(mzoom, 0, 1);
		            		}
		            	else if (e.getPointerCount()==3){
		            		mr.setmode(2);
	        
		  	              	mAngleX = dx * TOUCH_SCALE_FACTOR;
		  	              	mAngleY = dy * TOUCH_SCALE_FACTOR;
		  	              	mr.settranslate(mAngleX, mAngleY, 2);
		            	}
						
		            }   
		            else{
		            	if(e.getPointerCount()==1)
		            		mr.setmode(0);              
		              mAngleX = dx * TOUCH_SCALE_FACTOR;
		              mAngleY = dy * TOUCH_SCALE_FACTOR;
		              mr.settranslate(mAngleX, mAngleY, 2);
		            }
		              
		  
		            break;  
		        }  
			  	oldDist=newDist;
				mPreviousX = x;
	          	mPreviousY = y;
	          	
	          	try {
	    			Thread.sleep(15);
	    		} catch (Exception ex) {
	    			// Doesn't matter here...
	    		}
	          	
			return true;
		}
		   private float spacing(MotionEvent event) {  
		        float x = event.getX(0) - event.getX(1);  
		        float y = event.getY(0) - event.getY(1);  
		        return (float) Math.sqrt(x * x + y * y);  
		    } 
		
	}
    
    private String getCarPart(String objname)
    {
    	Log.e("enter","!!");
    	if (objname.contains("HDM_01_03_")) return "body";
		else if (objname.contains("HDM_01_032")) return "underbody";
		else if (objname.contains("HDM_01_039")||objname.contains("HDM_01_012")) return "glass";
		else if (objname.contains("HDM_01_028")) return "mirror";
		else if (objname.contains("HDM_01_031")) return "hood";
		else if (objname.contains("HDM_01_033")) return "headlight";
		else if (objname.contains("HDM_01_067")||objname.contains("HDM_01_062")||objname.contains("HDM_01_022")||objname.contains("HDM_01_064")||objname.contains("HDM_01_058")
				||objname.contains("HDM_01_066")||objname.contains("HDM_01_043")||objname.contains("HDM_01_065")) 
			return "tyre";
		else if (objname.contains("HDM_01_014")||objname.contains("HDM_01_074")) return "backlight";
		else if (objname.contains("HDM_01_073")) return "backregister";
		else if (objname.contains("HDM_01_013")||objname.contains("HDM_01_017")) return "trunk";
		else return "unknown";
    	
    }
    
}  
class LoadAssets {  
	  
public static Resources res;  

public LoadAssets(Resources resources) {  

  res = resources;  

}  

public static  InputStream loadf(String fileName) {  
	
  AssetManager am = LoadAssets.res.getAssets();  

  try {   
  	
      return am.open(fileName, AssetManager.ACCESS_UNKNOWN);  

  } catch (IOException e) {  
  	e.printStackTrace();
  	
  	
  	try {
			return am.open("ccddee.3DS", AssetManager.ACCESS_UNKNOWN);
	} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} 

  }  

}  
}

class LoadImage {  
	  
    public static  Bitmap background;

  
    public static  void loadi(Resources res) {  
  
    	background = BitmapFactory.decodeResource(res, R.drawable.zzz);  

       
    }  
}

class CarObj
{
	String obj;
	String name;
	public CarObj() {
		super();

	}
	
	public CarObj(String obj, String name) {
		super();
		this.obj = obj;
		this.name = name;
	}
	
}

class chatMsg {
	totalinfo thismsg;
	int layoutID;
	int picid=R.drawable.sky;
	public chatMsg(totalinfo thismsg, int layoutID,int picid) {
		super();
		this.thismsg = thismsg;
		this.layoutID = layoutID;

	}
	public chatMsg(totalinfo thismsg, int layoutID) {
		super();
		this.thismsg = thismsg;
		this.layoutID = layoutID;
		if (thismsg.from.contentEquals("ella"))
			this.picid=R.drawable.yellowcar;
		else if (thismsg.from.contentEquals("frank"))
			this.picid=R.drawable.whitecar;
		else if (thismsg.from.contentEquals("me"))
			this.picid=R.drawable.mepic;
	}
	/**
	 * @return the thismsg
	 */
	public totalinfo getThismsg() {
		return thismsg;
	}
	/**
	 * @param thismsg the thismsg to set
	 */
	public void setThismsg(totalinfo thismsg) {
		this.thismsg = thismsg;
	}
	/**
	 * @return the layoutID
	 */
	public int getLayoutID() {
		return layoutID;
	}
	/**
	 * @param layoutID the layoutID to set
	 */
	public void setLayoutID(int layoutID) {
		this.layoutID = layoutID;
	}
	
}
