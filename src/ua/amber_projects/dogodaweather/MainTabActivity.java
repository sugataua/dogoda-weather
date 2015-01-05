package ua.amber_projects.dogodaweather;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import java.text.SimpleDateFormat;

public class MainTabActivity extends TabActivity {
	
	@SuppressLint("SimpleDateFormat")
	static final SimpleDateFormat ft_date_time = new SimpleDateFormat("dd.MM.yyyy HH:mm"); 	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab_activity);
		
		TabHost tabHost = getTabHost();
		
		TabHost.TabSpec tabSpec;
		
		tabSpec = tabHost.newTabSpec("tag1");
		tabSpec.setIndicator(getResources().getString(R.string.now));	
		tabSpec.setContent(new Intent(this, CurrentWeatherActivity.class));
		tabHost.addTab(tabSpec);
		
		tabSpec = tabHost.newTabSpec("tag2");
		tabSpec.setIndicator("Hourly forecast");
		tabSpec.setContent(new Intent(this, HourlyForecastActivity.class));
		tabHost.addTab(tabSpec);
		
		tabSpec = tabHost.newTabSpec("tag3");
		tabSpec.setIndicator("Daily forecast");
		tabSpec.setContent(new Intent(this, DailyForecastActivity.class));
		tabHost.addTab(tabSpec);
	}

}
