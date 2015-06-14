package com.example.bine;

import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScanFragment extends Fragment implements Callback{  
	 
	private android.hardware.Camera aCamera; //Camera part
	private PreviewCallback mJpegPreviewCallback;
	private SurfaceView mSurfaceview;
	private SurfaceHolder mSurfaceHolder;
	private boolean previewrun;
	Bitmap vBitmap;
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        /*TextView textView = new TextView(getActivity());  
        textView.setText("Scan");  
        textView.setGravity(Gravity.CENTER_VERTICAL);  */
    	FrameLayout layout = new FrameLayout(getActivity());  
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);  
        mSurfaceview=new SurfaceView(getActivity());
        mSurfaceHolder = mSurfaceview.getHolder();
        
        mSurfaceHolder.addCallback(this);
        layout.addView(mSurfaceview);
        
        SurfaceView coverSurface=new SurfaceView(getActivity());
        LayoutParams cvparams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        coverSurface.setBackgroundResource(R.drawable.a);
        layout.addView(coverSurface,cvparams);
        
        /*LayoutParams cvparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);  
        layout.addView(coverSurface,cvparams);
        SurfaceHolder coverholder=coverSurface.getHolder();
        coverSurface.setZOrderOnTop(true);
        coverSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);*/

        /*Canvas coverimg=coverholder.lockCanvas();
        Paint temp=new Paint();
        temp.setColor(Color.RED);
        coverimg.drawCircle(100, 100, 50, temp);
        coverholder.unlockCanvasAndPost(coverimg);*/
        
        return layout;  
    }
    
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		 if(previewrun){
			 aCamera.setPreviewCallback(null);
			 aCamera.stopPreview();
			 previewrun=false;
			 aCamera.release();
			 aCamera=null;
		 }
		 initcamera(holder);

		 
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		initcamera(holder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		 if(aCamera != null) {
			 aCamera.setPreviewCallback(null);
			 aCamera.stopPreview();
			 previewrun=false;
			 aCamera.release();
			 aCamera=null;
		 }
	}  
  
	public void initcamera(SurfaceHolder holder){
		try{
			   aCamera=android.hardware.Camera.open();
			   aCamera.setPreviewDisplay(holder);
			  
			   aCamera.setPreviewCallback(null);
		//	   aCamera.setPreviewCallback(mJpegPreviewCallback);
		       Parameters params = aCamera.getParameters();  
		//     params.setFlashMode(Parameters.FLASH_MODE_TORCH);
		       params.setPreviewSize(640,480);  
		      
		       /*if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {  
                   params.setRotation(90);
               }  
               if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {  
                   params.set("orientation", "landscape");  
                   params.set("rotation", 90);  
               } */ 
		       aCamera.setDisplayOrientation(90);
		       aCamera.startPreview();  
		       previewrun=true;
			   
			}catch(Exception e){
				Log.e("name",e.toString());
			}
	}
}  
