package ua.amber_projects.dogodaweather;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainTabActivity extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab_activity);
		
		TabHost tabHost = getTabHost();
		
		TabHost.TabSpec tabSpec;
		
		tabSpec = tabHost.newTabSpec("tag1");
		tabSpec.setIndicator("Now");
		tabSpec.setContent(new Intent(this, MainActivity.class));
		tabHost.addTab(tabSpec);
		
		tabSpec = tabHost.newTabSpec("tag2");
		tabSpec.setIndicator("Daily forecast");
		tabSpec.setContent(new Intent(this, DailyForecastActivity.class));
		tabHost.addTab(tabSpec);
	}

}
