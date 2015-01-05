package ua.amber_projects.dogodaweather;


import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class CurrentWeatherActivity extends Activity {
	
	SharedPreferences sp;	
	BroadcastReceiver br;
	
	TextView tvTemp;	
	TextView tvCityCountry;
	TextView tvHumidity;
	TextView tvPressure;
	TextView tvWeatherDescription;
	ImageView ivWeatherCondition;
	TextView tvClouds;
	TextView tvWind;
	TextView tvPrec;
	TextView tvUpdatedTime;
	
	final int REQUEST_CODE_CITY_CHANGE = 1;	
	public final static String PARAM_PINTENT = "pendingIntent";
	public final static String PARAM_RESULT = "result";	
	public final static int STATUS_UPDATED = 200;	
	public final static String BROADCAST_ACTION = "ua.amber_projects.dogodaweather";	
	
	String city_ID;
	String city_Name;	
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the layout for this activity
		setContentView(R.layout.new_current_activity);    			
		
		tvCityCountry = (TextView) findViewById(R.id.tvCityCountry);
		
    	tvTemp = (TextView) findViewById(R.id.tvTemp);    	
    	tvHumidity = (TextView) findViewById(R.id.tvHumidity);
    	tvPressure = (TextView) findViewById(R.id.tvPressure);
    	
    	tvWeatherDescription = (TextView) findViewById(R.id.tvWeatherDescription);
    	ivWeatherCondition = (ImageView) findViewById(R.id.ivWeatherCondition);
	
    	tvClouds = (TextView) findViewById(R.id.tvClouds);
    	tvWind = (TextView) findViewById(R.id.tvWind);
    	tvPrec = (TextView) findViewById(R.id.tvPrec);

    	tvUpdatedTime = (TextView) findViewById(R.id.tvUpdatedTime);

    	sp = PreferenceManager.getDefaultSharedPreferences(this);
    	    	
    	city_ID = sp.getString("city_id", GetForecastTask.KIEV_ID);
		
		
    	// Weather update service starting
    	if ( !UpdateWeatherService.isRunning ) {
    		startService(new Intent(this, UpdateWeatherService.class));				
		}
			
    	
    	// Receiver to catch weather updates from UpdateService
    	br = new BroadcastReceiver() {    		      
    		@Override
    		public void onReceive(Context context, Intent intent) {
    			
    			String city_ID = sp.getString("city_id", GetForecastTask.KIEV_ID);
    			refreshWeatherOnScreen(city_ID);

    		}
    	};    		   

    	IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
    	
    	registerReceiver(br, intFilt);
	
	}
	
	@Override
	protected void onResume() {
				
		// Getting city id from application preferences
		String new_city_ID = sp.getString("city_id", GetForecastTask.KIEV_ID);
		
		// Checking for city id changes
		if (! city_ID.equals(new_city_ID)) {			
			city_ID = new_city_ID;
			
			// Restarting service for getting weather data for new city
			if (UpdateWeatherService.isRunning) {
				stopService(new Intent(this, UpdateWeatherService.class));				
			}
			startService(new Intent(this, UpdateWeatherService.class));						
		}
		
		refreshWeatherOnScreen(city_ID);
		
		super.onResume();			
	}
	
	
	@Override
	protected void onDestroy() {						
		super.onDestroy();
		
		unregisterReceiver(br);
	}	
	

	private void refreshWeatherOnScreen(String city_ID) {	
				
		int numberCityID = Integer.parseInt(city_ID);			
		
		Calendar calendarNow = Calendar.getInstance();
		// TODO: change magic numbers!
		long currentTimeMinus3H = calendarNow.getTimeInMillis() - (1000 * 60 * 60 * 3);
		
		WeatherDBAdapter tempWDBAdapter = new WeatherDBAdapter(CurrentWeatherActivity.this);
		
		tempWDBAdapter.open();
		
		CurrentWeatherConditions currentWeather = tempWDBAdapter.getLastCurrentWeather(numberCityID, currentTimeMinus3H);

		// It`s feature for debugging
		//tempWDBAdapter.cleanAllTables();
		
		tempWDBAdapter.close();
		
		long luDTStamp = sp.getLong("lastUpdateDT", 0);		
		Date luDate = new Date(luDTStamp);						
		
		tvUpdatedTime.setText("Last updated: " + MainTabActivity.ft_date_time.format(luDate));
		
		if (currentWeather != null) {
			
			showCurrentWeather(currentWeather);
			
		} else {
			
			showLackWeatherData();
			
		}
		
		
	}
	
	
	private void showCurrentWeather(CurrentWeatherConditions _currentWeather) {
		
		if (_currentWeather != null) {
			
			String tempUnit = sp.getString("units_temp", "Celsius");
			Degree tempDegree = Degree.valueOf(tempUnit);
			
			String pressureUnit = sp.getString("units_pressure", "mmHg");
			PressureUnit prefPressureUnit = PressureUnit.valueOf(pressureUnit);		
			
	    	City city = _currentWeather.getCity();
	    	
	    	tvCityCountry.setText(city.getCityName() + ", " + city.getCountry());
	    		    		    	
	    	tvTemp.setText(_currentWeather.getTempInt(tempDegree) + "°");
	    		    	
	    	tvHumidity.setText(getResources().getString(R.string.humidity) + ": " + _currentWeather.getHumidity() + "%");
	    	tvPressure.setText(getResources().getString(R.string.pressure) + ": " + _currentWeather.getPressure(prefPressureUnit) + " " + pressureUnit);
	    	
	    	// Image for weather conditions, we take only first image from array
	    	WeatherCode wCode = _currentWeather.getWeatherConditions().getWeatherCodes()[0];
	    		        	
	    	int imageResourceId = getIconID(wCode);
	    	
	    	ivWeatherCondition.setImageResource(imageResourceId);
	    	
	    	tvClouds.setText(getResources().getString(R.string.clouds) + ": " + _currentWeather.getClouds() + "%");
	    	tvWind.setText(getResources().getString(R.string.wind) + ": " + _currentWeather.getWindSpeed() + " m/s, " + _currentWeather.getWindDegree());
	    	
	    	if (_currentWeather.getPrecipitation().equals("no")) {
	    		
	    		tvPrec.setText(R.string.empty);
	    		
	    	} else {
	    		// TODO: precipitation
	    		tvPrec.setText("Precipitation: " + _currentWeather.getPrecipitation() + ", " + _currentWeather.getPrecipitationVolume() + " mm");
	    		
	    	}
	    		    	
	    	String packageName = getPackageName();
	    	int descriptionResID = getStringResourceByName(packageName,"id_meaning_" + wCode.getId());
	    	tvWeatherDescription.setText(descriptionResID);   	
	    	
	    	
		} else {
			
			showLackWeatherData();
			
		}						
	}
	
	private void showLackWeatherData() {
					
		tvTemp.setText(R.string.not_available);   
		ivWeatherCondition.setImageResource(R.drawable.na1);
	
		tvHumidity.setText(R.string.empty);
		tvPressure.setText(R.string.empty);
		tvWeatherDescription.setText(R.string.empty); 
		
		tvClouds.setText(R.string.empty);
		tvWind.setText(R.string.empty);
		tvPrec.setText(R.string.empty);
		
		tvCityCountry.setText("City id: " + city_ID);
		
	}
	

	
	public static int getIconID(WeatherCode _weatherCode) {
		
		int code = _weatherCode.getId();
		
		switch(code) {
			case 800:
				return R.drawable.sunny;				
			case 801:
				return R.drawable.few_clouds;
			case 802:
				return R.drawable.scattered_clouds;
			case 803:
				return R.drawable.overcast;
			case 804:
				return R.drawable.overcast;
			default:
				code = code / 100;
				switch (code) {
					case 2:
						return R.drawable.thunder;
					case 3:
						return R.drawable.showers;
					case 5:
						return R.drawable.heavy_rain;
					case 6:
						return R.drawable.snowy;
					case 7:
						return R.drawable.fog;
					default:
						return R.drawable.sunny;
				}
				
		}		
						
	}
	

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuItem mi = menu.add(1,1,0,getResources().getString(R.string.preferences));
				
		mi.setIntent(new Intent(this, PrefActivity.class));
		
		return super.onCreateOptionsMenu(menu);
	}
	

    // Checking connection to openweathermap server
    public static boolean isServerAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("api.openweathermap.org"); 

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }
    
    
    // It`s very brute way to get id of description
    private int getStringResourceByName(String packageName, String aString) {
    	
        //String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        
        return resId;
      }
	
	
	
	
	
	

}
