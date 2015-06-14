package com.example.bine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.threed.jpct.Camera;
import com.threed.jpct.Config;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;
import com.threed.jpct.util.Overlay;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class MyRenderer implements Renderer{
	//private TextureManager tm=TextureManager.getInstance();
	private static Texture t;
	private int countsss;
	static Bitmap temp=Bitmap.createBitmap(512,512,Bitmap.Config.ARGB_4444);
	private float mAngleX;
	private float mAngleY;
	private float mzoom;
	private int count,mode,number=-1;
	static private Object3D[] thing;  
    private Overlay overlay;
    private World world;  
    private Object3D o3d=null; 
    // FrameBuffer对象  
    private FrameBuffer fb;  
    private int objnumber=0;
    private SimpleVector midcenter=SimpleVector.ORIGIN;
    private SimpleVector temppos=new SimpleVector(0,0,10);
    private Light sun;
    private double awydis=10;
    public void setmode(int _mode){
    	mode=_mode;
    }
    public void setunmuber(int _number){
    	number=_number;
    	Log.e("get touched", number+"");
    }
    public void setcount(int _count){
    	count=_count;
    }
    public void settranslate(float arg0,float arg1,int ismzoomornot){
    	switch (ismzoomornot){
    	case 1:
    		mzoom=arg0;
    		break;
    	case 2:
    		mAngleX=arg0;
    		mAngleY=arg1;
    		break;
    	}
    }
    
	public MyRenderer() {
		Config.maxPolysVisible = 500;  
        Config.farPlane = 1500;  
        Config.glTransparencyMul = 0.1f;  
        Config.glTransparencyOffset = 0.1f;  
        Config.useVBO=true;  
          
        Texture.defaultToMipmapping(true);  
        Texture.defaultTo4bpp(true);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
        if (fb != null) {  
            fb.dispose();  
        }  

        fb = new FrameBuffer(gl, width, height);  
        //TextureManager tm = TextureManager.getInstance();
       
        //TextureManager.getInstance().addTexture("background", t);  

	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
        if (overlay==null){
        overlay=new Overlay(world,fb,"background");
        overlay.setDepth(20);
        overlay.setTransparency(-1);}
		if (count==1){
		switch (mode){
		case 0: 
			/*SimpleVector camera_position=world.getCamera().getPosition();
			Double anglex,angley;
			if (camera_position.x<0)
				anglex=-Math.acos(camera_position.z/10)+6.2831;
			else anglex=Math.acos(camera_position.z/10);
			anglex+=mAngleX;

			Double temp=Math.atan2(camera_position.y,camera_position.x);
			
			if (camera_position.x==0&&camera_position.y==0)
			{
			 	angley
			}
			if (temp>0)
				angley=temp;
			else angley=-temp+6.2831;


			Log.i("camera_position", anglex+" add "+mAngleX+" : "+angley+" add "+mAngleY);
			angley+=-mAngleY;
			if (anglex<0) anglex+=2*3.14159;
			if (angley<0) angley+=2*3.14159;
			Log.i("camera_position", anglex+" add "+mAngleX+" : "+angley+" add "+mAngleY);
			camera_position.x=(float) (10*Math.sin(anglex)*Math.cos(angley));
			
			camera_position.y=(float) (10*Math.sin(anglex)*Math.sin(angley));
						
			camera_position.z=(float) (10*Math.cos(anglex));
			Log.i("final value: ",camera_position.toString());
			
			world.getCamera().setPosition(camera_position);*/
			
			SimpleVector camera_position=temppos;
			//camera_position.rotateZ(mAngleY);
			camera_position.rotateY(-mAngleX);
			temppos=camera_position;
			if (midcenter.y+mAngleY*5<5||midcenter.y+mAngleY*5>-5)
			{
				//for(int i=0;i<objnumber;i++)
				
				//{
					thing[0].translate(0, mAngleY*5, 0);
				//}
			
				midcenter.add(new SimpleVector(0,mAngleY*5,0));
			}
			SimpleVector dis=camera_position.calcSub(midcenter);
			dis.scalarMul((float) (awydis/dis.length()));
			//dis.add(midcenter);
			world.getCamera().setPosition(dis.calcAdd(midcenter));
			//world.getCamera().setPosition(camera_position);
			world.getCamera().lookAt(SimpleVector.ORIGIN);
			mAngleX=0;mAngleY=0;
			break;
	
		case 1:
			awydis=(awydis*((-mzoom+1)/5+1))>6?awydis*((-mzoom+1)/5+1):6;
			mzoom=1;
			/*for(int i=0;i<thing.length;i++)
			thing[i].setScale(thing[i].getScale()*((mzoom-1)/5+1));
			mzoom=1;*/
			break;
		
		case 2:
			for(int i=0;i<thing.length;i++)
			{
				thing[i].translate(mAngleX/5, mAngleY/5, 0);
			}
			//float x=world.getCamera().getPosition().x+mAngleX/10;
			//float y=world.getCamera().getPosition().y+mAngleY/10;
			//float z=world.getCamera().getPosition().z;
			//world.getCamera().setPosition(x, y, z);
			mAngleX=0;mAngleY=0;
			break;

		}
		count-=1;
		}
		
        fb.clear(RGBColor.BLACK);  
        world.renderScene(fb);  
        sun.setPosition( world.getCamera().getPosition());

        

        /*Paint paint=new Paint();
        paint.setColor(Color.GREEN);  
        paint.setTextSize(16);  //设置字体大小  
        Canvas tempc=new Canvas(temp);
        tempc.drawText("我是一个小小小小鸟啊啊啊啊啊啊！",0, 11,0, 20, paint);
        t=new Texture(temp);
        t.compress();
        fb.blit(t,0,0,10,20,t.getWidth(),40,FrameBuffer.TRANSPARENT_BLITTING);  */
        
        ////fb.blit(TextureManager.getInstance().getTexture("context"),0,0,10,200,TextureManager.getInstance().getTexture("context").getWidth(),TextureManager.getInstance().getTexture("context").getHeight(),FrameBuffer.TRANSPARENT_BLITTING);  
        world.draw(fb);
        fb.display();  

        //MemoryHelper.compact();

	}
	
	public Object[] JudgeObjects(float x,float y){
		SimpleVector dir=Interact2D.reproject2D3DWS(world.getCamera(), fb, (int)x, (int)y).normalize();
		
		Object[] res=world.calcMinDistanceAndObject3D(world.getCamera().getPosition(), dir, 10000 /*or whatever*/);

		return res;
 
    }
	
	 public void load(String data,int _transmode)
	    {

	    	 world = new World();  
	    	 // 设置环境光  
	         world.setAmbientLight(80,80,80);  
	         TextureManager.getInstance().flush();
	         // 循环将已存在Texture以新的名字存入TextureManager中  
	         loadModel(data,1);


	         thing[0].build();
	         thing[0].setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
	         world.addObject(thing[0]);  
	        	 for(int i=1;i<objnumber;i++)
	        	 { 
	       // 渲染绘制前进行的操作  
	        		 
	        		 thing[i].build();  
	        		 thing[i].setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
	        		 thing[0].addChild(thing[i]);
	         // 将thing添加Object3D对象中          
	        		 world.addObject(thing[i]);  
	        	 }
	        	 
	         Texture texture=new Texture(BitmapHelper.rescale(LoadImage.background,64, 64));
	         TextureManager.getInstance().addTexture("background", texture);  
	         // 调整坐标系  
	         world.getCamera().setPosition(0, 0, 10);  
	         thing[0].align(world.getCamera());
	         // 在world中应用刚设置好了的坐标系  
	         world.getCamera().lookAt(SimpleVector.ORIGIN);
	         //thing[1].getTransformedCenter()
	         sun=new Light(world);
	         sun.setIntensity(160, 160, 160);
	         sun.setPosition( world.getCamera().getPosition());
	         Loader.clearCache();
	         MemoryHelper.compact(); 

	    }
	
	 public void loadModel(String filename, float scale) {  
	        // 将载入的3ds文件保存到model数组中  
		 
	        thing = Loader.load3DS(LoadAssets.loadf(filename), scale); 
	        objnumber=thing.length;
	        
	        /*o3d=new Object3D(0);
         	for (int i = 0; i < thing.length; i++) {  
         		o3d = Object3D.mergeObjects(o3d, thing[i]);  
         		o3d.compile();
            }*/
	        return;
	         
	    }  
	 
	 

}
