package will.ok.MemoryManager;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class PackagesInfo {
	private List<ApplicationInfo> appList;
	
	public PackagesInfo(Context context){
		//ͨ�����������������е�Ӧ�ó�������ж�صģ�������Ŀ¼
		PackageManager pm = context.getApplicationContext().getPackageManager();
		appList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
	}
	
	
	
	/**
	 * ͨ��һ�����������ظó����һ��Application����
	 * @param name	������
	 * @return	ApplicationInfo 
	 */
	
	public ApplicationInfo getInfo(String name){
		if(name == null){
			return null;
		}
		for(ApplicationInfo appinfo : appList){
			if(name.equals(appinfo.processName)){
				return appinfo;
			}
		}
		return null;
	}
	
}
