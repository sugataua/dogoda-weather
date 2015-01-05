package ua.amber_projects.dogodaweather;

import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.TextView;

public class HourlyForecastActivity extends Activity {
	
		
	SharedPreferences sp;
	
	HourlyForecastAdapter hourlyAdapter;
	WeatherDBAdapter tempWDBAdapter;
	
	ListView lvMain;
	TextView tvCityCountry;
	TextView tvLastUpdate;
	
	
	
	
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
		long luDTstamp = sp.getLong("lastUpdateDT", 0);
		
		Date lastUpdated = new Date(luDTstamp);		
						
		int numberCityID = Integer.parseInt(city_ID);
			
		Calendar calendarToday = Calendar.getInstance();		
		
		tempWDBAdapter.open();		
			
		HourlyForecast hourlyForecast = tempWDBAdapter.getHourlyForecast(numberCityID, calendarToday.getTimeInMillis());		
			
		tempWDBAdapter.close();
		
		if (hourlyForecast != null) {
			
			hourlyAdapter = new HourlyForecastAdapter(this, hourlyForecast.getHourlyWeather() );
			
			tvCityCountry.setText(hourlyForecast.getCity().getCityName() + ", " +
					hourlyForecast.getCity().getCountry());
			
			if (luDTstamp == 0) {
				
				tvLastUpdate.setText(R.string.empty);				
				
			} else {
				
				tvLastUpdate.setText("Last updated: " + MainTabActivity.ft_date_time.format(lastUpdated));
				
			}	
			
						
			lvMain.setAdapter(hourlyAdapter);
			
		}
			
		
		super.onResume();
	}
	
	

}
