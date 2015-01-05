package ua.amber_projects.dogodaweather;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class DailyForecastActivity extends Activity {
	
	DailyForecastAdapter dailyAdapter;
	SharedPreferences sp;
	WeatherDBAdapter tempWDBAdapter;
	ListView lvMain;
	TextView tvCityCountry;
	TextView tvLastUpdate;
	
	final SimpleDateFormat ft_date_time = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.daily_weather_activity);
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		tempWDBAdapter = new WeatherDBAdapter(this);
		
		
		lvMain = (ListView) findViewById(R.id.lvMain);
		tvCityCountry = (TextView) this.findViewById(R.id.tvHeaderText);
		tvLastUpdate = (TextView) this.findViewById(R.id.tvFooterText);
		
		
		
		
		
		
		
	}
	
	@Override
	protected void onResume() {
		
		long lastUpdate = sp.getLong("lastUpdateDT", 0);
		
		Date lastUpdDate = new Date(lastUpdate);
		
		String city_ID = sp.getString("city_id", GetForecastTask.KIEV_ID);
		
		int numberCityID = Integer.parseInt(city_ID);
			
		Calendar calendarToday = Calendar.getInstance();
		
		calendarToday.set(Calendar.HOUR_OF_DAY, 0);
		calendarToday.set(Calendar.MINUTE, 0);
		calendarToday.set(Calendar.SECOND, 0);
		calendarToday.set(Calendar.MILLISECOND, 0);
		
		tempWDBAdapter.open();		
			
		DailyForecast dailyForecast = tempWDBAdapter.getDailyForecast(numberCityID, calendarToday.getTimeInMillis());
		
		tempWDBAdapter.close();
		
		if (dailyForecast != null) {
			
			dailyAdapter = new DailyForecastAdapter(this, dailyForecast.getDailyWeather() );
			
			
			tvCityCountry.setText(dailyForecast.getCity().getCityName() + ", " +
					dailyForecast.getCity().getCountry());
			
			lvMain.setAdapter(dailyAdapter);
			
			if (lastUpdate == 0) {
				
				tvLastUpdate.setText(R.string.empty);
				
				
			} else {
				
				tvLastUpdate.setText("Last updated: " + ft_date_time.format(lastUpdDate));
				
			}
			
			lvMain.setAdapter(dailyAdapter);
			
			
		}
		
	
		
		super.onResume();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		
		MenuItem mi = menu.add(0,0,0,getResources().getString(R.string.preferences));
				
		mi.setIntent(new Intent(this, PrefActivity.class));
		
		return super.onCreateOptionsMenu(menu);
	}

}
