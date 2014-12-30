package ua.amber_projects.dogodaweather;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class UpdateWeatherService extends Service {
	
	final String LOG_TAG = "UW_Service";
	
	// URLs for API call. Answers in JSON.    
    private final static String URL_CURRENT_WEATHER = "http://api.openweathermap.org/data/2.5/weather";
    private final static String URL_HOURLY_FORECAST = "http://api.openweathermap.org/data/2.5/forecast";
    private final static String URL_DAILY_FORECAST = "http://api.openweathermap.org/data/2.5/forecast/daily";
    
    private final static String APIKEY = "de23c150a1644cb195e5072ee217bc2d";
		
	public static final String TAG_ID  = "id";
	public static final String TAG_COD = "cod";
	
	public static boolean isRunning = false;
	
	private LocationManager locationManager;
	
		
	
	Timer timer;
	UpdateWeather uwTask;
	UpdateLocation ulTask;
	long interval = 1000 * 60 * 60 * 3;	
	
	SharedPreferences sp;
	//PrefEditor  

	
	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG, "OnCreate");
		isRunning = true;
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);		
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		timer = new Timer();
		
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "onStartCommand");
		
		String city_ID = sp.getString("city_id", "703448");
		
		if (uwTask != null) uwTask.cancel();
		
		uwTask = new UpdateWeather(city_ID);
		ulTask = new UpdateLocation();
		timer.schedule(uwTask, 1000, interval);
		timer.schedule(ulTask, 1000, interval/2);
		
				
		return super.onStartCommand(intent, flags, startId);
	}
	
	public void onDestroy() {
		super.onDestroy();
		isRunning = false;
		
		Log.d(LOG_TAG, "onDestroy");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	class UpdateLocation extends TimerTask {
		
		
		public void run() {
			
			locationManager.requestLocationUpdates(
			        LocationManager.GPS_PROVIDER, 1000 * 10, 10,
			        locationListener);
			
		}
		
		
		private LocationListener locationListener = new LocationListener() {

		    @Override
		    public void onLocationChanged(Location location) {
		      Log.d("LL","onLocationChanged");
		      
		      //locationManager.removeUpdates(locationListener);
		      
		      
		      
		    }

		    @Override
		    public void onProviderDisabled(String provider) {
		    	Log.d("LL","onProviderDisabled");
		    }

		    @Override
		    public void onProviderEnabled(String provider) {
		    	Log.d("LL","onProviderEnabled");
		    }

		    @Override
		    public void onStatusChanged(String provider, int status, Bundle extras) {
		    	Log.d("LL","onStatusChanged");		    	
		    }
		  };
	}
	
		
	class UpdateWeather extends TimerTask {
		
		String cityID;
		
		UpdateWeather(String _cityID) {			
			this.cityID = _cityID;
		}
				
		
		
		public void run() {
			
			
			
			
			
			CurrentWeatherConditions currentWeather = getCurrentWeather();
			DailyForecast dailyWeather = getDailyForecast();
			HourlyForecast hourlyForecast = getHourlyForecast();
						
			Log.d(LOG_TAG, "Run timer task");

			WeatherDBAdapter wDBAdapter;
	    	
	    	wDBAdapter = new WeatherDBAdapter(UpdateWeatherService.this);
	    	wDBAdapter.open();
	    	
	        if (currentWeather != null) {	  
	        	
	        	wDBAdapter.insertOrUpdateCurrentWeather(currentWeather);
	        }
	        
	        if (hourlyForecast != null) {
	        	
	        	wDBAdapter.insertOrUpdateHourlyForecast(hourlyForecast);
	        }
	        
	        
	        if (dailyWeather != null) {
	        	
	        	wDBAdapter.insertOrUpdateDailyForecast(dailyWeather);
	        	
	        }	        	
	    		    	
	    	wDBAdapter.close();
	    	
	    	boolean updatedFlag = (currentWeather != null && dailyWeather != null && hourlyForecast != null);
	    	
	    	
	    	if (updatedFlag) {
	    		
	    		Editor ed = sp.edit();	    	
	    		
	    		Date now = new Date();
	    		
	    		
	    		ed.putLong("lastUpdateDT", now.getTime());
	    		
	    		ed.commit();
	    		
		    	Intent intent = new Intent(CurrentWeatherActivity.BROADCAST_ACTION);
		    	
		    	sendBroadcast(intent);	    		
	    	}
	    		    	

					
			
		}
		
	    private CurrentWeatherConditions getCurrentWeather() {
	    	CurrentWeatherConditions currentWeather = null;
	    	
			// Creating service handler class instance
	        ServiceHandler sh = new ServiceHandler();
	        
	        // Creating list of parameters 
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair(TAG_ID, this.cityID));
	        params.add(new BasicNameValuePair("APPID", APIKEY));
	        
	        // Making API call and getting request
	        String jsonStr = sh.makeServiceCall(URL_CURRENT_WEATHER, ServiceHandler.GET, params);
	        
	        if (jsonStr != null) {
	        	try {
	        		JSONObject jsonObj = new JSONObject(jsonStr);
	        		
	        		int answerCode = jsonObj.getInt(TAG_COD);
	        		
	        		if (answerCode == 200) {
	        			
	        			currentWeather = new CurrentWeatherConditions(jsonObj);
	        			
	        		} else {
	        			Log.e("Answer", "Code is not 200 OK!"); 
	        			//TODO catch 404 and other "bad" codes
	        		}
	        		
	        	} catch (JSONException e) {
	                e.printStackTrace();
	                Log.e("JSON", "Unhandled json exception!");
	        	}
	        	
	        } else {
	        	Log.e("ServiceHandler", "Couldn't get any data from the url");
	        }
	        
	        return currentWeather;
	        	
	    }
	    
	    
	    
	    private HourlyForecast getHourlyForecast() {
	    	
	    	HourlyForecast hourlyForecast = null;
	    	   
			// Creating service handler class instance
	        ServiceHandler sh = new ServiceHandler();
	        
	        // Creating list of parameters 
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair(TAG_ID, cityID));
	        params.add(new BasicNameValuePair("APPID", APIKEY));
	        
	        // Making API call and getting request
	        String jsonStr = sh.makeServiceCall(URL_HOURLY_FORECAST, ServiceHandler.GET, params);
	        
	        if (jsonStr != null) {
	        	try {
	        		JSONObject jsonObj = new JSONObject(jsonStr);
	        		
	        		int answerCode = jsonObj.getInt(TAG_COD);
	        		
	        		if (answerCode == 200) {
	        			
	        			hourlyForecast = new HourlyForecast(jsonObj);
	        			
	        		} else {
	        			Log.e("Answer", "Code is not 200 OK!"); 
	        			//TODO catch 404 and other "bad" codes
	        		}
	        		
	        	} catch (JSONException e) {
	                e.printStackTrace();
	                Log.e("JSON", "Unhandled json exception!");
	        	}
	        	
	        } else {
	        	Log.e("ServiceHandler", "Couldn't get any data from the url");
	        }
	        
	        return hourlyForecast;	        	
	    
	    	
	    }
		
	    
	    private DailyForecast getDailyForecast() {
	    	DailyForecast dailyForecast = null;
	    	
    	   	
			// Creating service handler class instance
	        ServiceHandler sh = new ServiceHandler();
	        
	        // Creating list of parameters 
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair(TAG_ID, this.cityID));
	        params.add(new BasicNameValuePair("APPID", APIKEY));
	        
	        // Making API call and getting request
	        String jsonStr = sh.makeServiceCall(URL_DAILY_FORECAST, ServiceHandler.GET, params);
	        
	        if (jsonStr != null) {
	        	try {
	        		JSONObject jsonObj = new JSONObject(jsonStr);
	        		
	        		int answerCode = jsonObj.getInt(TAG_COD);
	        		
	        		if (answerCode == 200) {
	        			       			
	        			dailyForecast = new DailyForecast(jsonObj);        			
	        			
	        			
	        		} else {
	        			Log.e("Answer", "Code is not 200 OK!"); 
	        			//TODO catch 404 and other "bad" codes
	        		}
	        		
	        	} catch (JSONException e) {
	                e.printStackTrace();
	                Log.e("JSON", "Unhandled json exception!");
	        	}
	        	
	        } else {
	        	Log.e("ServiceHandler", "Couldn't get any data from the url");
	        }
	        
	        return dailyForecast;
	        	
	    	
	    }
	    
		
	}

}
