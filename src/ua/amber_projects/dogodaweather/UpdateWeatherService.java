package ua.amber_projects.dogodaweather;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
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
	
	
	//private boolean isActivatedFlag = false;
	
	Timer timer;
	UpdateWeather uwTask;
	long interval = 1000 * 60 * 60 * 3;	
	
	SharedPreferences sp;

	
	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG, "OnCreate");
		isRunning = true;
		
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		timer = new Timer();
		
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "onStartCommand");
		
		String city_ID = sp.getString("city_id", "703448");
		
		if (uwTask != null) uwTask.cancel();
		
		uwTask = new UpdateWeather(city_ID);		
		timer.schedule(uwTask, 1000, interval);
		
				
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
	
		
	class UpdateWeather extends TimerTask {
		
		String cityID;
		
		UpdateWeather(String _cityID) {			
			this.cityID = _cityID;
		}
				
		
		
		public void run() {
			
			
			
			
			
			CurrentWeatherConditions currentWeather = getCurrentWeather();
			DailyForecast dailyWeather = getDailyForecast();
						
			Log.d(LOG_TAG, "Run timer task");

			WeatherDBAdapter wDBAdapter;
	    	
	    	wDBAdapter = new WeatherDBAdapter(UpdateWeatherService.this);
	    	wDBAdapter.open();
	    	
	        if (currentWeather != null) {	  
	        	
	        	wDBAdapter.insertOrUpdateCurrentWeather(currentWeather);
	        }
	        
	        
	        if (dailyWeather != null) {
	        	
	        	wDBAdapter.insertOrUpdateDailyForecast(dailyWeather);
	        	
	        }	        	
	    		    	
	    	wDBAdapter.close();
	    	
	    	boolean updatedFlag = (currentWeather != null && dailyWeather != null);
	    	
	    	
	    	if (updatedFlag) {
		    	Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
		    	
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
