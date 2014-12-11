package ua.amber_projects.dogodaweather;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WeatherCode {

	private int id;
	private String main;
	private String description;
	private String icon;
	
	WeatherCode (int id, String main, String description, String icon) {
		this.id = id;
		this.main = main;
		this.description = description;
		this.icon = icon;
	}
	
	WeatherCode (JSONObject weatherCondition) {
		try {
			this.id = weatherCondition.getInt(GetForecastTask.TAG_ID);
			this.main = weatherCondition.getString(GetForecastTask.TAG_MAIN);
			this.description = weatherCondition.getString(GetForecastTask.TAG_DESCRIPTION);
			this.icon = weatherCondition.getString(GetForecastTask.TAG_ICON);					
		} catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON", "Unhandled json exception!");
            
            //TODO Catch exceptions
    	}
	
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getMain() {
		return this.main;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getIcon() {
		return this.icon;
	}
	


}
