package com.android;

import android.graphics.drawable.Drawable;

public class Process implements Comparable<Process>{
	private String title;
	private String processName;
	private String packageName;
	private int pid;
	private int uid;
	private int memSize;
	private Drawable icon;
	
	public Process(){	
	}
	
	public Process(String ttitle,String pN,String pkN, int pId,int uId, int mS, Drawable ticon){
		title = ttitle;
		processName = pN;
		packageName = pkN;
		pid = pId;
		uid = uId;
		memSize = mS;
		icon = ticon;
	}
	
	public String getTitle(){
		return title;
	}
	public String getProcessName(){
		return processName;
	}
	
	public String getPackageName(){
		return packageName;
	}
	
	public int getPid(){
		return pid;
	}
	
	public int getUid(){
		return uid;
	}
	
	public int getMemSize(){
		return memSize;
	}
	
	public Drawable getIcon(){
		return icon;
	}
	
	public void setTitle(String ttitle){
		title = ttitle;
	}
	public void setProcessName(String tprocessName){
		processName = tprocessName;
	}
	
	public void setPackageName(String tpackageName){
		packageName = tpackageName;
	}
	public void setPid(int tpid){
		pid = tpid;
	}
	
	public void setUid(int tuid){
		uid = tuid;
	}
	
	public void setMemSize(int tmemSize){
		memSize = tmemSize;
	}
	
	public void setIcon(Drawable ticon){
		icon = ticon;
	}
	
	public int compareTo(Process x){
		return this.title.compareTo(x.title);
	}
}
