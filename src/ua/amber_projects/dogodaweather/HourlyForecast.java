package ua.amber_projects.dogodaweather;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import android.util.Log;

public class HourlyForecast {
	private WeatherConditions[] hourlyWeather;
	private City city;
	
	
	public HourlyForecast(City city) {
		this.city = city;
		this.hourlyWeather = null; 
	}
	
	public HourlyForecast(JSONObject joHourlyForecast) {

		int countHW;
		
		try {
			
			JSONObject joCity = joHourlyForecast.getJSONObject(GetForecastTask.TAG_CITY);
			
			city = new City(joCity);
	
			countHW = joHourlyForecast.getInt(GetForecastTask.TAG_CNT);
			
			hourlyWeather = new WeatherConditions[countHW];
			
			JSONArray joHWList = joHourlyForecast.getJSONArray(GetForecastTask.TAG_LIST);
			
			for (int i = 0; i < countHW; i++) {
				try {				
					JSONObject weatherConditions = joHWList.getJSONObject(i);
					hourlyWeather[i] = new WeatherConditions(weatherConditions);				
					
				} catch (JSONException e) {
	                e.printStackTrace();
	                Log.e("JSON", "Unhandled json exception!");
	                
	                //TODO Throw runtime exception?
	        	}		
				
			}
			
					
			
		}  catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON", "Unhandled json exception!");
            
            //TODO Throw runtime exception?
    	}		
		
	}
		
	public City getCity() {
		return this.city;
	}
	
	public void setHourlyWeather(WeatherConditions[] hourlyWeather) {
		this.hourlyWeather = hourlyWeather;
	}
	
	public WeatherConditions[] getHourlyWeather() {
		return this.hourlyWeather;
	}

}
