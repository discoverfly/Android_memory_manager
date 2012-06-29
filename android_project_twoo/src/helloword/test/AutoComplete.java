package helloword.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class AutoComplete extends Activity {
	static final String[] months = new String[]{
		"Jan", "Feb", "Mar", "Apr", "May","Jun","Jul","Aug",
		"Sep","Oct","Nov","Dec"};
	
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.autocomplete);
		ArrayAdapter<String> monthArray = new
				ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,months);
		
		final AutoCompleteTextView textView = (AutoCompleteTextView)
				findViewById(R.id.testAutoComplete);
		
		textView.setAdapter(monthArray);
		
		final Button changeButton = (Button)
				findViewById(R.id.autoCompleteButton);
		
		changeButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				changeOption(textView);
			}

			private void changeOption(AutoCompleteTextView text) {
				// TODO Auto-generated method stub
				if (text.getHeight() == 100){
					text.setHeight(30);
				} else {
					text.setHeight(100);
				}
			}
		});
		
		final Button changeButton2 = (Button)
				findViewById(R.id.textColorButton);
		
		changeButton2.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				changeOption2(textView);
			}

			private void changeOption2(AutoCompleteTextView textView) {
				// TODO Auto-generated method stub
				textView.setTextColor(Color.RED);
			}
		});
	}
}
