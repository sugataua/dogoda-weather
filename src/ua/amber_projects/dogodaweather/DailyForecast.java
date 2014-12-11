package ua.amber_projects.dogodaweather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DailyForecast {
	
	private City city;
	
	private DailyWeatherConditions[] dailyWeatherConditions;
	
		
	public DailyForecast(JSONObject joDailyForecast) {
		int countDW;
		
		try {
			JSONObject joCity = joDailyForecast.getJSONObject(GetForecastTask.TAG_CITY);
			
			city = new City(joCity);
			
			countDW = joDailyForecast.getInt(GetForecastTask.TAG_CNT);
			
			dailyWeatherConditions = new DailyWeatherConditions[countDW];
			
			JSONArray joHWList = joDailyForecast.getJSONArray(GetForecastTask.TAG_LIST);
			
			for (int i = 0; i < countDW; i++) {
				try {				
					JSONObject dayWeatherConditions = joHWList.getJSONObject(i);
					dailyWeatherConditions[i] = new DailyWeatherConditions(dayWeatherConditions);				
					
				} catch (JSONException e) {
	                e.printStackTrace();
	                Log.e("JSON", "Unhandled json exception!");
	                
	                //TODO Throw runtime exception?
	        	}		
				
			}
			
		} catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON", "Unhandled json exception!");
            
            //TODO Throw runtime exception?
    	}		
		
		
		
		
	}
	
	public DailyForecast() {
		this.city = null;
		this.dailyWeatherConditions = null;	
		
	}
	
	
	public City getCity() {
		return this.city;
	}
	
	public void setCity(City _city) {
		this.city = _city;
	}
	
	public DailyWeatherConditions[] getDailyWeather() {
		return this.dailyWeatherConditions;
	}
	
	public void setDailyWeather(DailyWeatherConditions[] _dwc) {
		this.dailyWeatherConditions = _dwc;
	}
	
	
		

	
	

		
}

