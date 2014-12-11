package ua.amber_projects.dogodaweather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DailyWeatherConditions {
	
	private WeatherCodesArray weather_codes;
	
	private double temp_day;
	private double temp_day_min;
	private double temp_day_max;
	private double temp_eve;
	private double temp_night;
	private double temp_morn;
	
	private double pressure; // Pressure, hPa
	private double pressure_sea_level;
	private double pressure_grnd_level;
	
	private int humidity;	 //Humidity, %
	
	private double wind_speed;
	private double wind_degree;
	private double wind_gust_speed;
	
	private int clouds; // %
	
	private String precipitation;
	private double precipitation_volume;
	
	private long dtstamp;
	
	
	public DailyWeatherConditions(JSONObject joDayWeather) {
		pressure = pressure_sea_level = pressure_grnd_level = -1;
		precipitation = null;
		precipitation_volume = -1;		
		wind_gust_speed = -1;		
		
		try {
			JSONArray joWeatherCodes = joDayWeather.getJSONArray(GetForecastTask.TAG_WEATHER); 			
			this.weather_codes = new WeatherCodesArray(joWeatherCodes);
			
			
			JSONObject joTemp = joDayWeather.getJSONObject(GetForecastTask.TAG_TEMP);
			
			this.temp_day = joTemp.getDouble(GetForecastTask.TAG_DAY);
			this.temp_day_min = joTemp.getDouble(GetForecastTask.TAG_MIN);
			this.temp_day_max = joTemp.getDouble(GetForecastTask.TAG_MAX);
			this.temp_eve = joTemp.getDouble(GetForecastTask.TAG_EVE);
			this.temp_night = joTemp.getDouble(GetForecastTask.TAG_NIGHT);
			this.temp_morn = joTemp.getDouble(GetForecastTask.TAG_MORN);
			
			
			this.dtstamp = joDayWeather.getLong(GetForecastTask.TAG_DT) * GetForecastTask.MULTIPLIER_TO_MILISECONDS;
			
			this.humidity = joDayWeather.getInt(GetForecastTask.TAG_HUMIDITY);
			
			this.clouds = joDayWeather.getInt(GetForecastTask.TAG_CLOUDS);
			
			try {
            	this.pressure = joDayWeather.getLong(GetForecastTask.TAG_PRESSURE);                    	
            } catch (JSONException jsonEr) {
            	try {
            		this.pressure_sea_level = joDayWeather.getLong(GetForecastTask.TAG_SEA_LEVEL);
            		
            	} catch (JSONException jsonEr2) {
            		this.pressure_grnd_level = joDayWeather.getLong(GetForecastTask.TAG_GRND_LEVEL);                    		
            		
            	}
            }
            
                      
            
            this.wind_speed = joDayWeather.getDouble(GetForecastTask.TAG_SPEED);
            this.wind_degree= joDayWeather.getDouble(GetForecastTask.TAG_DEG);
            //wind_speed = (double) Math.round(10 * wind_speed)/10;  
            try {
            	this.wind_gust_speed = joDayWeather.getDouble(GetForecastTask.TAG_GUST);            	
            	                    	
            } catch (JSONException jsonEr) {
            	;                    	
            }            
            
            try {
            	JSONObject rain = joDayWeather.getJSONObject(GetForecastTask.TAG_RAIN);
         	
            	precipitation_volume = rain.getInt(GetForecastTask.TAG_3H);
            	precipitation = "rain";
            } 
            	catch (JSONException jsonEr) {
            		precipitation = "no";
            		precipitation_volume = -1;
            	}
            
            try {
            	JSONObject snow = joDayWeather.getJSONObject(GetForecastTask.TAG_SNOW);
            	
            	precipitation_volume = snow.getInt(GetForecastTask.TAG_3H);
            	precipitation = "snow";
            } 
            	catch (JSONException jsonEr) {                		
            		precipitation = "no";
            		precipitation_volume = -1;
            	}
			
			
			
		} catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON", "Unhandled json exception!");
            
            //TODO Throw runtime exception?
    	}	
	}
	
	
	public DailyWeatherConditions() {
		this.weather_codes = null; 
		
		pressure = pressure_sea_level = pressure_grnd_level = -1;
		precipitation = null;
		precipitation_volume = -1;		
		wind_gust_speed = -1;		
		
	}
	
	
	public double getTempDay(Degree degree) {
		return degreeConverterFromKelvin(degree, this.temp_day);
	}
	
	public void setTempDay(double _temp_day) {
		this.temp_day = _temp_day; 
	}
	
	public double getTempDayMin(Degree degree) {
		return degreeConverterFromKelvin(degree, this.temp_day_min);
	}
	
	public void setTempDayMin(double _temp_day_min) {
		this.temp_day_min = _temp_day_min;
	}
	
	public double getTempDayMax(Degree degree) {
		return degreeConverterFromKelvin(degree, this.temp_day_max);
	}
	
	public void setTempDayMax(double _temp_day_max) {
		this.temp_day_max = _temp_day_max;
	}
	
	public double getTempEve(Degree degree) {
		return degreeConverterFromKelvin(degree, this.temp_eve);
	}
	
	public void setTempEve(double _temp_eve) {
		this.temp_eve = _temp_eve;
	}
	
	public double getTempNight(Degree degree) {
		return degreeConverterFromKelvin(degree, this.temp_night);
	}
	
	public void setTempNight(double _temp_night) {
		this.temp_night = _temp_night;
	}
	
	public double getTempMorn(Degree degree) {
		return degreeConverterFromKelvin(degree, this.temp_morn);
	}
	
	public void setTempMorn(double _temp_morn) {
		this.temp_morn = _temp_morn;
	}
	
	public int getHumidity() {
		return this.humidity;
	}
	
	public void setHumidity(int _humidity) {
		this.humidity = _humidity;
	}
	
	public double getPressure() {
		return this.pressure;
	}
	
	
	public int getPressure(PressureUnit pu) {
		return pressureConverterFromhPa(pu, this.pressure);
	}	
	
	
	public void setPressure(double _pressure) {
		this.pressure = _pressure;
	}
	
	public double getPressureSeaLevel() {
		return this.pressure_sea_level;
	}
	
	public void setPressureSeaLevel(double _pressure_sea_level) {
		this.pressure_sea_level = _pressure_sea_level;
	}
	
	public double getPressureGrndLevel() {
		return this.pressure_grnd_level;
	}
	
	public void setPressureGrndLevel(double _pressure_grnd_level) {
		this.pressure_grnd_level = _pressure_grnd_level;
	}
	
	public double getWindSpeed() {
		return this.wind_speed;
	}

	public void setWindSpeed(double _wind_speed) {
		this.wind_speed = _wind_speed;
	}
	
	public double getWindDegree() {
		return this.wind_degree;
	}
	
	public void setWindDegree(double _wind_degree) {
		this.wind_degree = _wind_degree;
	}
	
	public double getWindGust() {
		return this.wind_gust_speed;
	}
	
	public void setWindGust(double _wind_gust_speed) {
		this.wind_gust_speed = _wind_gust_speed;
	}
	
	public int getClouds() {
		return this.clouds;
	}
	
	public void setClouds(int _clouds) {
		this.clouds = _clouds ;
	}
	
	public String getPrecipitation() {
		return this.precipitation;
	}
	
	public void setPrecipitation(String _precipitation) {
		this.precipitation = _precipitation;
	}
	
	public double getPrecipitationVolume() {
		return this.precipitation_volume;
	}
	
	public void setPrecipitationVolume(double _precipitation_volume) {
		this.precipitation_volume = _precipitation_volume;
	}
	
	public long getDT() {
		return this.dtstamp;
	}
	
	public void setDT(long _dtstamp) {
		this.dtstamp = _dtstamp;
	}
	
	
	public WeatherCodesArray getWeatherConditions(){
		return this.weather_codes;
	}
	
	public void setWeatherConditions(WeatherCodesArray _weather_codes){
		this.weather_codes = _weather_codes;
	}
	
	
	public double degreeConverterFromKelvin(Degree deg, double kelvin) {
		double result = -273.15;
		switch(deg) {
			case Celsius:
				result = kelvin - 273.15;
				result = (double) Math.round(10 * result)/10;
				break;
			case Fahrenheit:
				result = (kelvin * (9.0/5.0)) - 459.67;
				result = (double) Math.round(result);
				break;
			case Kelvin:
				result = kelvin;
				break;
			default:
				break;
				
		}
		return result;		
	}
	
	public static int pressureConverterFromhPa(PressureUnit pu, double pressure) {
		int result = 0;
		switch(pu) {
			case hPa:
				result = (int) pressure;
			case mmHg:
				result = (int) Math.round((pressure * 7.5006) / 10);
				
					
		}
		
		return result;
	}
	

}
