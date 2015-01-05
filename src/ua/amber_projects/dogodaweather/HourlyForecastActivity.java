package ua.amber_projects.dogodaweather;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class HourlyForecastActivity extends Activity {
	
	HourlyForecastAdapter hourlyAdapter;
	SharedPreferences sp;
	WeatherDBAdapter tempWDBAdapter;
	ListView lvMain;
	TextView tvCityCountry;
	TextView tvLastUpdate;
	final SimpleDateFormat ft_date_time = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
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
		
		String city_ID = sp.getString("city_id", GetForecastTask.KIEV_ID);
		long lastUpdate = sp.getLong("lastUpdateDT", 0);
		
		Date lastUpdDate;
		
		lastUpdDate = new Date(lastUpdate);
			

		
		
				
		int numberCityID = Integer.parseInt(city_ID);
			
		Calendar calendarToday = Calendar.getInstance();
		
		
		tempWDBAdapter.open();		
			
		HourlyForecast hourlyForecast = tempWDBAdapter.getHourlyForecast(numberCityID, calendarToday.getTimeInMillis());
		
			
		tempWDBAdapter.close();
		
		if (hourlyForecast != null) {
			
			hourlyAdapter = new HourlyForecastAdapter(this, hourlyForecast.getHourlyWeather() );
			
			tvCityCountry.setText(hourlyForecast.getCity().getCityName() + ", " +
					hourlyForecast.getCity().getCountry());
			
			if (lastUpdate == 0) {
				
				tvLastUpdate.setText(R.string.empty);
				
				
			} else {
				
				tvLastUpdate.setText("Last updated: " + ft_date_time.format(lastUpdDate));
				
			}
			
			
						
			lvMain.setAdapter(hourlyAdapter);
			
		}
		
	
		
		super.onResume();
	}
	
	
	private View createHeader(String text) {
	      View v = getLayoutInflater().inflate(R.layout.header, null);
	      ( (TextView) v.findViewById(R.id.tvHeaderText) ).setText(text);
	      return v;
	}
	
	
	private View createFooter(String text) {
	      View v = getLayoutInflater().inflate(R.layout.footer, null);
	      ( (TextView) v.findViewById(R.id.tvFooterText) ).setText(text);
	      return v;
	}

}
