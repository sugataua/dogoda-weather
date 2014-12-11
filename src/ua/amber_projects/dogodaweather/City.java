package ua.amber_projects.dogodaweather;

import org.json.JSONObject;
import org.json.JSONException;

import android.util.Log;

public class City {
	private int cityId;
	private String cityName;
	private String country;
	private double lat;
	private double lon;
	
	public City(int _id, String name, String country, double lat, double lon) {
		this.setCityId(_id);
		this.setCityName(name);
		this.setCountry(country);
		this.setLat(lat);
		this.setLon(lon);
	}
	
	public City(JSONObject joCity) {
		try {
			cityId = joCity.getInt(GetForecastTask.TAG_ID);
			cityName = joCity.getString(GetForecastTask.TAG_NAME);
			country = joCity.getString(GetForecastTask.TAG_COUNTRY);
			
			JSONObject joCoord = joCity.getJSONObject(GetForecastTask.TAG_COORD);
			lat = joCoord.getDouble(GetForecastTask.TAG_LAT);
			lon = joCoord.getDouble(GetForecastTask.TAG_LON);			
		} catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON", "Unhandled json exception!");
            
            //TODO Throw runtime exception?
    	}		
	}

	public int getCityId() {
		return cityId;
	}

	private void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	private void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountry() {
		return country;
	}

	private void setCountry(String country) {
		this.country = country;
	}

	public double getLat() {
		return lat;
	}

	private void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	private void setLon(double lon) {
		this.lon = lon;
	}
	
	

}
