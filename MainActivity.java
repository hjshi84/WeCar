package com.example.bine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

















import com.threed.jpct.Object3D;

import android.R.string;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	public static int number=00001;
	private final String SERVER_HOST_IP = "192.168.8.86";
	private final int SERVER_HOST_PORT = 11222;
	public Socket socket;
	public PrintStream output;
	public static boolean receivedata=false;
	public static totalinfo receivevalue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		    
		setContentView(R.layout.activity_main);
		android.app.ActionBar actionBar = getActionBar();  
	    actionBar.setDisplayHomeAsUpEnabled(true); 
	    //setOverflowShowingAlways();  
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);  
	    android.app.ActionBar.Tab tab = actionBar  
	            .newTab()  
	            .setText(R.string.Scan)  
	            .setTabListener(new TabListener<ScanFragment>(this, "Scan", ScanFragment.class));  
	    actionBar.addTab(tab);  
	    tab = actionBar 
	            .newTab()  
	            .setText(R.string.MySpace)  
	            .setTabListener(new TabListener<MySpaceFragment>(this, "MySpace", MySpaceFragment.class));  
	    actionBar.addTab(tab);
	    tab = actionBar 
	            .newTab()  
	            .setText(R.string.Friendslist)  
	            .setTabListener(new TabListener<FriendslistFragment>(this, "Friendslist",FriendslistFragment.class));  
	    actionBar.addTab(tab);
	    //
	    
	    new Thread() {
            public void run() {
            	initClientSocket();
            	while(true)
            	{
            		try{
            			
	            			DataOutputStream out=new DataOutputStream(socket.getOutputStream());
	            	        DataInputStream in=new DataInputStream(socket.getInputStream());
	            	        byte[] recBuf = new byte[1024];
	            	        in.read(recBuf);
	            	        if (recBuf.toString().contains("msg&"))
	            	        {
	            	        	Message message = new Message();  
	               	          	message.what=789;// TODO
	               	          	String[] res=recBuf.toString().split("\\&");
	               	          	
	               	          	Bundle b = new Bundle();
	               	          	b.putString("from", res[1]);
	               	          	b.putString("to", res[2]);
	               	          	b.putString("content", res[3]);
	               	          	b.putLong("when", Long.parseLong(res[4]));
	               	          	message.setData(b);
	               	          	MySpaceFragment.mHandler.sendMessage(message);
	            	        }
            			
            		}catch(Exception e){
            			
            		}
            		
            	}

            }
        }.start();
	}
	
	 public void closeSocket()
	  {
	    try
	    {
	      output.close();
	      socket.close();
		}
	    catch (IOException e)
	    {
	      handleException(e, "close exception: ");
		}
	  }

	 private void initClientSocket()
	  {
	    try
	    {
	      /* 连接服务�?*/
	      socket = new Socket(SERVER_HOST_IP, SERVER_HOST_PORT);

	      /* 获取输出�?*/
	      output = new PrintStream(socket.getOutputStream(), true, "utf-8");
	      
	    }
	    catch (UnknownHostException e)
	    {
	      handleException(e, "unknown host exception: " + e.toString());
	    }
	    catch (IOException e)
	    {
	      handleException(e, "io exception: " + e.toString());
	    }

	  }
	  public void handleException(Exception e, String prefix)
	  {
	    e.printStackTrace();
	    
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}



