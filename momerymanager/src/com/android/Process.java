package com.android;

import android.graphics.drawable.Drawable;

public class Process implements Comparable<Process>{
	private String title;
	private String processName;
	private int pid;
	private int memSize;
	private Drawable icon;
	
	public Process(){	
	}
	
	public Process(String ttitle,String pN, int pId, int mS, Drawable ticon){
		title = ttitle;
		processName = pN;
		pid = pId;
		memSize = mS;
		icon = ticon;
	}
	
	public String getTitle(){
		return title;
	}
	public String getProcessName(){
		return processName;
	}
	
	public int getPid(){
		return pid;
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
	
	public void setPid(int tpid){
		pid = tpid;
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
