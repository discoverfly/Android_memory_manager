package will.ok.MemoryManager;

import android.graphics.drawable.Drawable;

public class Programe {
	//Í¼±ê
	private Drawable icon;	
	//³ÌÐòÃû
	private String name;
	
	private String packageName;
	
	private int pid;
	
	public void setPid(int id){
		this.pid = id;
	}
	
	public int getPid(){
		return this.pid;
	}
	
	public void setPackageName(String PName){
		this.packageName = PName;
	}
	
	public String getPackageName(){
		return packageName;
	}
	
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}