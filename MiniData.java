package com.example.bine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MiniData {
	
	static ArrayList<totalinfo> alldata=new ArrayList<totalinfo>();
		
	MiniData()
	{
		totalinfo tempinfo=new totalinfo("ella","me",new CarObj("HDM_01_067","tyre"),"   Love How Much!!",System.currentTimeMillis());
		totalinfo tempinfo1=new totalinfo("frank","me",new CarObj("HDM_01_03_","body"),"   nice body!!",System.currentTimeMillis());
		totalinfo tempinfo2=new totalinfo("ella","me",new CarObj("HDM_01_033","headlight"),"   nice headlight!!",System.currentTimeMillis());
		totalinfo tempinfo3=new totalinfo("frank","me",new CarObj("HDM_01_031","hood"),"   :)",System.currentTimeMillis());
		totalinfo tempinfo4=new totalinfo("ella","me",new CarObj("HDM_01_032","underbody"),"   wonderful!!",System.currentTimeMillis());
		alldata.add(tempinfo);
		alldata.add(tempinfo1);
		alldata.add(tempinfo2);
		alldata.add(tempinfo3);
		alldata.add(tempinfo4);
		Collections.sort(alldata,new SortTotalinfo());     
	}
	
	public void addinfo(totalinfo temp){
		
		alldata.add(temp);
		Collections.sort(alldata,new SortTotalinfo());     
	}
	
	public void deleteinfo(totalinfo temp){
		
		alldata.remove(temp);
	
	}
	
	public totalinfo findinfo(String to,CarObj carobj,Long time)
	{
		int length=alldata.size();
		long lastestime=Long.MIN_VALUE;
		int selectnum=-1;
		for(int i=0;i<length;i++)
		{
			if (alldata.get(i).caiobject!=null)
			{
				if ((alldata.get(i).to.contains(to))&&(carobj.obj.contains(alldata.get(i).caiobject.obj)))
				{
					if (alldata.get(i).when>lastestime) 
					{	
						lastestime=alldata.get(i).when;
						selectnum=i;
					}
				}
			}
		}
		if (selectnum>=0)
			return alldata.get(selectnum);
		else 
			return new totalinfo("","",new CarObj(),"not found!!",0l);
	}

}
	class totalinfo{
		String from;
		String to;
		CarObj caiobject;
		String contextinfo;
		Long when;
		public totalinfo(String from, String to, CarObj caiobject,
				String contextinfo, Long when) {
			super();
			this.from = from;
			this.to = to;
			this.caiobject = caiobject;
			this.contextinfo = contextinfo;
			this.when = when;
		}
		
	}
	
	class SortTotalinfo implements Comparator<totalinfo>{     
		public int compare(totalinfo obj1,totalinfo obj2){     
		
			if(obj1.when<obj2.when)     
				return 1;     
			else    
				return 0;     
		}     
	}     