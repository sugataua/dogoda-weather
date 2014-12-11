package ua.amber_projects.dogodaweather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;



public class WeatherCodesArray {
	
	public WeatherCode [] weatherCodes;
	
	WeatherCodesArray (JSONArray weatherArray) {
		int arrayLength = weatherArray.length();
		
		weatherCodes = new WeatherCode[arrayLength];
		
		for (int i = 0; i < arrayLength; i++) {
			try {				
				JSONObject w = weatherArray.getJSONObject(i);
				weatherCodes[i] = new WeatherCode(w);				
				
			} catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON", "Unhandled json exception!");
                
                //TODO Throw runtime exception?
        	}		
			
		}
	}
	
	public WeatherCodesArray(WeatherCode [] _weatherCodes) {
		this.weatherCodes = _weatherCodes;
	}
	
	public int length() {
		return this.weatherCodes.length;
	}
	
	public WeatherCode[] getWeatherCodes(){
		return this.weatherCodes;
		
	}
	


}
