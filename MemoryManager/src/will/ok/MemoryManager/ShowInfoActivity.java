package will.ok.MemoryManager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ShowInfoActivity extends Activity{
	private int uid;
	private String name;
	private String packagename;
	private TextView text;
	public Button back;
	public Button close;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showinfo);
		close = (Button)findViewById(R.id.close);
		Intent intent = this.getIntent();
		uid = intent.getIntExtra("uid", -1);
		name = intent.getStringExtra("name");
		packagename = intent.getStringExtra("package");
		Log.v("new", name+" "+uid+" "+packagename);
		
		
		back = (Button)findViewById(R.id.button1);
		text = (TextView)findViewById(R.id.text1);

		ActivityManager actMgr = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		int[] hehe = new int[]{uid};
		actMgr.getProcessMemoryInfo(hehe);
		Debug.MemoryInfo[] memoryInfo = actMgr.getProcessMemoryInfo(hehe);  
		// 获取进程占内存用信息 kb单位   
		int memSize = memoryInfo[0].dalvikPrivateDirty; 
		
		String out = "name:    "+name+"\n" +
					 "package: "+packagename+"\n" +
					 "memory:  "+memSize+"kb\n" +
					 "uid:     "+uid+"\n";
		SpannableStringBuilder style=new SpannableStringBuilder(out);
		text.setText(style);

		//close.setOnClickListener((OnClickListener)this);
		//back.setOnClickListener((OnClickListener)this);
		close.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v) {
				ActivityManager act = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
				act.restartPackage(packagename);
				finish();
			}
		});
		back.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v) {
				finish();
			}
		});
		
	}
//	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.button1:
//			finish(); 
//			break;
//		case R.id.close: 
//			ActivityManager act = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
//			act.restartPackage(packagename);
//			finish();
//			break;
//		}
//	}
}
