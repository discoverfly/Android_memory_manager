package helloword.test;

import android.app.Activity;
import android.os.Bundle;
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
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Button callButton = (Button) findViewById(R.id.callButton);
        final EditText phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        
        callButton.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        	 Intent dailIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+ phoneNumber.getText()));
             dailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(dailIntent);
        	}
        });
       
        
        /*ImageView helloWorldImageView = new ImageView(this);
        helloWorldImageView.setImageResource(R.drawable.helloworld);
        setContentView(helloWorldImageView);*/
    }
}