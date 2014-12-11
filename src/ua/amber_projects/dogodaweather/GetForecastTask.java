package ua.amber_projects.dogodaweather;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import ua.amber_projects.dogodaweather.ServiceHandler;

import android.os.AsyncTask;
import android.util.Log;

public class GetForecastTask extends AsyncTask<Void, Void, Void> {
	
	final static String APIKEY = "de23c150a1644cb195e5072ee217bc2d";
	
	// Special block with test value
	public final static String KIEV_ID = "703448";
	public final static int KIEV_ID_INT = 703448;
	
	protected int CITY_ID_INT = KIEV_ID_INT;
	
	protected String cityID ;
	
	// URLs for API call. Answers in JSON.    
    private static String URL_CURRENT_WEATHER = "http://api.openweathermap.org/data/2.5/weather";
    private static String URL_HOURLY_FORECAST = "http://api.openweathermap.org/data/2.5/forecast";
    private static String URL_DAILY_FORECAST = "http://api.openweathermap.org/data/2.5/forecast/daily";
    
    
	 // JSON Node names
     public static final String TAG_COORD  = "coord";
     public static final String TAG_LAT  = "lat";
	 public static final String TAG_LON  = "lon";
	
	 public static final String TAG_SYS  = "sys";
	 public static final String TAG_ID  = "id";
	
	 public static final String TAG_COUNTRY  = "country";
	 public static final String TAG_SUNRISE  = "sunrise";
	 public static final String TAG_SUNSET  = "sunset";
	
	 public static final String TAG_WEATHER  = "weather";
	 
	 public static final String TAG_MAIN  = "main";
	 public static final String TAG_DESCRIPTION = "description";
	 public static final String TAG_ICON = "icon";
	
	 public static final String TAG_BASE = "base";
	
	 
	 public static final String TAG_TEMP = "temp";
	 public static final String TAG_TEMP_MIN = "temp_min";
	 public static final String TAG_TEMP_MAX = "temp_max";
	 public static final String TAG_PRESSURE = "pressure";
	 public static final String TAG_SEA_LEVEL = "sea_level";
	 public static final String TAG_GRND_LEVEL = "grnd_level";
	  
	 public static final String TAG_HUMIDITY = "humidity";
	
	 public static final String TAG_WIND = "wind";
	 public static final String TAG_SPEED = "speed";
	 public static final String TAG_DEG = "deg";
	 public static final String TAG_GUST = "gust";    
	
	 public static final String TAG_CLOUDS = "clouds";
	 public static final String TAG_ALL = "all";
	
	 public static final String TAG_DT = "dt";
	 public static final String TAG_DT_TXT = "dt_txt";
	 
	 public static final String TAG_NAME = "name";
	 public static final String TAG_COD = "cod";
	
	 public static final String TAG_RAIN = "rain";
	 public static final String TAG_SNOW = "snow";
	 public static final String TAG_3H = "3h";
	 
	 public static final String TAG_CNT = "cnt";
	 public static final String TAG_LIST = "list";
	 
	 public static final String TAG_MORN = "morn";
	 public static final String TAG_DAY = "day";
	 public static final String TAG_EVE= "eve";
	 public static final String TAG_NIGHT = "night";
	 public static final String TAG_MIN = "min";
	 public static final String TAG_MAX = "max";
	 
	 public static final String TAG_CITY = "city";
	 
	 
	 
//	 private static final String PRECIPITATION = "prec";
//	 private static final String PRECIPITATION_VOLUME_3H = "prec_vol";
	 
	 //private static final String LAST_CALL = "last_call";
	 
	 
	 public static final int MULTIPLIER_TO_MILISECONDS = 1000;
	 
	 protected String [] forecastStr;
	 protected CurrentWeatherConditions cwcData;
	 protected HourlyForecast hfData;
	 protected DailyForecast dfData;
	    
	 public GetForecastTask(String cityID) {
		 this.cityID = cityID;
	 }
	 
	 public GetForecastTask(int cityID) {
		 this.cityID = "" + cityID;
	 }
    
    @Override
    protected void onPreExecute() {
    	super.onPreExecute();    	
    }
    
    @Override
	protected Void doInBackground(Void... arg0) {
    	
    	getCurrentWeather();
    	getHourlyForecast();
    	getDailyForecast();
    	
    	return null;    	
    }
    
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
    
    
    private void getCurrentWeather() {
    	//CurrentWeather cw;
    	
		// Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();
        
        // Creating list of parameters 
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(TAG_ID, cityID));
        params.add(new BasicNameValuePair("APPID", APIKEY));
        
        // Making API call and getting request
        String jsonStr = sh.makeServiceCall(URL_CURRENT_WEATHER, ServiceHandler.GET, params);
        
        if (jsonStr != null) {
        	try {
        		JSONObject jsonObj = new JSONObject(jsonStr);
        		
        		int answerCode = jsonObj.getInt(TAG_COD);
        		
        		if (answerCode == 200) {
        			
        			cwcData = new CurrentWeatherConditions(jsonObj);
        			
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
        	
    }
    
    private void getHourlyForecast() {
   
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
        			
        			hfData = new HourlyForecast(jsonObj);
        			
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
        	
    
    	
    }
    
    
    private void getDailyForecast() {
    	
    	   	
		// Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();
        
        // Creating list of parameters 
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(TAG_ID, cityID));
        params.add(new BasicNameValuePair("APPID", APIKEY));
        
        // Making API call and getting request
        String jsonStr = sh.makeServiceCall(URL_DAILY_FORECAST, ServiceHandler.GET, params);
        
        if (jsonStr != null) {
        	try {
        		JSONObject jsonObj = new JSONObject(jsonStr);
        		
        		int answerCode = jsonObj.getInt(TAG_COD);
        		
        		if (answerCode == 200) {
        			       			
        			dfData = new DailyForecast(jsonObj);        			
        			
        			
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
        	
    	
    }
    
	

}
