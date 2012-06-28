package will.ok.MemoryManager;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ListActivity;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ControlActivity extends ListActivity  {
    /** Called when the activity is first created. */
	
	private int indexhehe;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
       
       List<Programe> list = getRunningProcess();
       ListAdapter adapter = new ListAdapter(list,this);
       getListView().setAdapter(adapter);
//ListView��item����֮���������飺
//���ȴ�������onItemLongClick������
//ִ��onItemLongClick������Ȼ�����
//onItemLongClick�ķ���ֵ�ж��ǲ���
//ִ��OnCreateContextMenuListener��
//�������onItemLongClick����false��
//������ִ��OnCreateContextMenuListener������
//���򷵻�true������OnCreateContextMenuListener������
       getListView().setOnItemLongClickListener(new OnItemLongClickListener(){
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int index, long arg3) {
			indexhehe = index;
			return false;
		}
       });
       getListView().setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			menu.setHeaderTitle("Control");       
			menu.add(0, 0, 0, "�ر�");
            menu.add(0, 1, 0, "�鿴��Ϣ");
            menu.add(0, 2, 0, "����");
            
		}
       });
       this.getListView().setOnItemClickListener(new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> adp, View v, int index,
				long id) {
			// TODO Auto-generated method stub
			Log.v("list", ""+index);
			indexhehe = index;
			ListView listView = (ListView)adp;
			Programe p = (Programe)listView.getItemAtPosition(index);
//			Log.v("listItem", p.getName());
			Log.v("package name",p.getPackageName());
//			ActivityManager actMgr = (ActivityManager)getSystemService(ACTIVITY_SERVICE); 
//			actMgr.restartPackage(p.getPackageName());
			Intent it = new Intent(ControlActivity.this, ShowInfoActivity.class);
			Bundle bundle = new Bundle();  
			bundle.putInt("uid", p.getPid());
			bundle.putString("name", p.getName());
			bundle.putString("package", p.getPackageName());
			it.putExtras(bundle);  
			//startActivity(it);
			startActivityForResult(it, 100);
		}
    	   
       });
    }
	
	@Override       
    public boolean onContextItemSelected(MenuItem aItem) {            
         ListView listview = getListView();
         Programe p = (Programe)listview.getItemAtPosition(indexhehe);
         Log.v("listview", ""+indexhehe);
         /* Switch on the ID of the item, to get what the user selected. */       
         switch (aItem.getItemId()) {       
         	case 0:       
         		Log.v("package name",p.getPackageName());
    			ActivityManager actMgr = (ActivityManager)getSystemService(ACTIVITY_SERVICE); 
    			actMgr.restartPackage(p.getPackageName());
    			List<Programe> list = getRunningProcess();
    		    ListAdapter adapter = new ListAdapter(list,this);
    		    getListView().setAdapter(adapter);
         		break;
         	case 1:
         		//ת��������ϸҳ�棬������ʾ�ڴ棬�رգ����ص�
//    			Log.v("listItem", p.getName());
    			Log.v("package name",p.getPackageName());
    			
//    			ActivityManager actMgr = (ActivityManager)getSystemService(ACTIVITY_SERVICE); 
//    			actMgr.restartPackage(p.getPackageName());
    			Intent it = new Intent(ControlActivity.this, ShowInfoActivity.class);
    			Bundle bundle = new Bundle();  
    			bundle.putInt("uid", p.getPid());
    			bundle.putString("name", p.getName());
    			bundle.putString("package", p.getPackageName());
    			it.putExtras(bundle);  
    			//startActivity(it);
    			startActivityForResult(it, 100);
         		break;
         	case 2:
         	default:
         		break;
         }       
         return super.onContextItemSelected(aItem);  
    }       
	
	//�������е�
	public List<Programe> getRunningProcess(){
		PackagesInfo pi = new PackagesInfo(this);
		
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		//��ȡ�������е�Ӧ��
		List<RunningAppProcessInfo> run = am.getRunningAppProcesses();
		//��ȡ������������������Ҫͨ��������ȡ�����ͼ��ͳ�����
		PackageManager pm =this.getPackageManager();
		List<Programe> list = new ArrayList<Programe>();	
		
		for(RunningAppProcessInfo ra : run){
			//������Ҫ�ǹ���ϵͳ��Ӧ�ú͵绰Ӧ�ã���Ȼ��Ҳ���԰���ע�͵���
			if(ra.processName.equals("system") || ra.processName.equals("com.android.phone")){
				continue;
			}
			if(pi.getInfo(ra.processName)==null) {  
			    continue;  
			}  
			Programe  pr = new Programe();
			pr.setPackageName(pi.getInfo(ra.processName).packageName);
			pr.setIcon(pi.getInfo(ra.processName).loadIcon(pm));
			pr.setName(pi.getInfo(ra.processName).loadLabel(pm).toString());
			pr.setPid(ra.pid);
			System.out.println(pi.getInfo(ra.processName).loadLabel(pm).toString());
			Log.v("old",pr.getName()+pr.getPackageName()+pr.getPid());
			list.add(pr);
		}
		return list;
	}
	
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		List<Programe> list = getRunningProcess();
	    ListAdapter adapter = new ListAdapter(list,this);
	    getListView().setAdapter(adapter);
	}  
}
