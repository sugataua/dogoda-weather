package ua.amber_projects.dogodaweather;

import org.json.JSONObject;
import org.json.JSONException;
//import org.json.JSONArray;

import android.util.Log;




public class CurrentWeatherConditions extends WeatherConditions {
	
	protected City city;
	
	protected long sunrise;
	protected long sunset;
		
	public CurrentWeatherConditions(JSONObject joCurrentWeather) {
		super(joCurrentWeather);
		
		int cityId;
		String cityName;
		String country;
		double lat;
		double lon;
		
				
		try {
			
			cityId = joCurrentWeather.getInt(GetForecastTask.TAG_ID);
			cityName = joCurrentWeather.getString(GetForecastTask.TAG_NAME);
			
			JSONObject coordinates = joCurrentWeather.getJSONObject(GetForecastTask.TAG_COORD);            
			lon = coordinates.getDouble(GetForecastTask.TAG_LON);                    
			lat = coordinates.getDouble(GetForecastTask.TAG_LAT);
            
            JSONObject sys = joCurrentWeather.getJSONObject(GetForecastTask.TAG_SYS);
            
            country = sys.getString(GetForecastTask.TAG_COUNTRY);
            
            this.sunrise = sys.getLong(GetForecastTask.TAG_SUNRISE) * GetForecastTask.MULTIPLIER_TO_MILISECONDS;
            this.sunset = sys.getLong(GetForecastTask.TAG_SUNSET) * GetForecastTask.MULTIPLIER_TO_MILISECONDS;
            
            this.city = new City(cityId, cityName, country, lat, lon);
            
  		
		} catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON", "Unhandled json exception!");
        }
		
	}
	
	public CurrentWeatherConditions(City _city, WeatherCodesArray _wcArr) {
		super(_wcArr);
		this.city = _city;
		this.sunrise = 0;
		this.sunset = 0;
	}
	
	public City getCity() {
		return this.city;
	}
	
	public long getSunrise() {
		return this.sunrise;
	}
	
	public void setSunrise(long sunrise) {
		this.sunrise = sunrise;
	}
	
	public long getSunset() {
		return this.sunset;
	}
	
	public void setSunset(long sunset) {
		this.sunset = sunset;
	}
	



}
