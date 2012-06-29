package helloword.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;


public class Android_project_twoActivity extends Activity {
    /** Called when the activity is first created. */
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        //TextView selection = (TextView)findViewById(R.id.selection);
        //registerForContextMenu(selection);
        /*ImageView helloWorldImageView = new ImageView(this);
        helloWorldImageView.setImageResource(R.drawable.helloworld);
        setContentView(helloWorldImageView);*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(Menu.NONE,0,Menu.NONE, "Auto Complete").setIcon(R.drawable.helloworld);
    	menu.add(Menu.NONE,1,Menu.NONE,"Button");
    	menu.add(Menu.NONE,2,Menu.NONE,"CheckBox").setIcon(R.drawable.ic_launcher);
    	menu.add(Menu.NONE,3,Menu.NONE,"EditText");
    	menu.add(Menu.NONE,4,Menu.NONE,"RadioGroup");
    	menu.add(Menu.NONE,5,Menu.NONE,"Spiner");
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case 0:
    		showAutoComplete();
    		return true;
    	case 1:
    		showTestButton();
    		return true;
    	case 2:
    		return true;
    	case 3:
    		return true;
    	case 4:
    		return true;
    	case 5:
    		return true;
    	}
    	return true;
    }
	private void showTestButton() {
		// TODO Auto-generated method stub
		Intent testButton = new Intent(this,TestButton.class);
		startActivity(testButton);
	}
	private void showAutoComplete() {
		// TODO Auto-generated method stub
		Intent autocomplete = new Intent(this,AutoComplete.class);
		startActivity(autocomplete);
	}
    
}