package helloword.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TestButton extends Activity {
	@Override
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.button);
		final Button testButton = (Button) findViewById(R.id.testButton);
		final Button changeButton = (Button) findViewById(R.id.layoutButton);
		changeButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				changeOption(testButton);
				
			}

			private void changeOption(Button testButton) {
				// TODO Auto-generated method stub
				if (testButton.getHeight() == 100){
					testButton.setHeight(30);
				} else testButton.setHeight(100);
			}
		});
		
		final Button textButton = (Button) findViewById(R.id.textColorButton);
		textButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				changeOption(testButton);
				
			}

			private void changeOption(Button testButton) {
				// TODO Auto-generated method stub
			
				testButton.setTextColor(Color.RED);
			}
		});
	}

}
