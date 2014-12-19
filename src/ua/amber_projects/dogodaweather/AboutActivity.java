package ua.amber_projects.dogodaweather;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AboutActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.about_activity);
	}
	
	
	
	public void onPoweredByClick(View view) {
		
		String url = "http://openweathermap.org/";
		Intent intent = new Intent(Intent.ACTION_VIEW);	
		
		intent.setData(Uri.parse(url));		
		
		startActivity(intent);		
		
		
	}

}
