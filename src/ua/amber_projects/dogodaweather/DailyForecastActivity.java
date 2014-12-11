package ua.amber_projects.dogodaweather;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class DailyForecastActivity extends Activity {
	
	DailyForecastAdapter dailyAdapter;
	SharedPreferences sp;
	WeatherDBAdapter tempWDBAdapter;
	ListView lvMain;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.daily_weather_activity);
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		tempWDBAdapter = new WeatherDBAdapter(this);
		
		
		lvMain = (ListView) findViewById(R.id.lvMain);
		
		
		
		
		
	}
	
	@Override
	protected void onResume() {
		
		String city_ID = sp.getString("city_id", GetForecastTask.KIEV_ID);
		
		int numberCityID = Integer.parseInt(city_ID);
			
		Calendar calendarToday = Calendar.getInstance();
		
		calendarToday.set(Calendar.HOUR_OF_DAY, 0);
		calendarToday.set(Calendar.MINUTE, 0);
		calendarToday.set(Calendar.SECOND, 0);
		calendarToday.set(Calendar.MILLISECOND, 0);
		
		tempWDBAdapter.open();		
			
		DailyForecast dF = tempWDBAdapter.getDailyForecast(numberCityID, calendarToday.getTimeInMillis());
	
		tempWDBAdapter.close();
		
		dailyAdapter = new DailyForecastAdapter(this, dF.getDailyWeather() );
		
		lvMain.setAdapter(dailyAdapter);
		
		
		super.onResume();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		
		MenuItem mi = menu.add(0,0,0,getResources().getString(R.string.preferences));
				
		mi.setIntent(new Intent(this, PrefActivity.class));
		
		return super.onCreateOptionsMenu(menu);
	}

}
