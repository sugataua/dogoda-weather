package ua.amber_projects.dogodaweather;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import android.util.Log;

enum Degree {
	Celsius, Fahrenheit, Kelvin
}
enum PressureUnit {
	hPa, mmHg	
	
}

public class WeatherConditions {
			
	protected WeatherCodesArray weather_codes;
	
	protected double temp;
	protected double temp_min;
	protected double temp_max;
	
	protected double pressure;
	protected double pressure_sea_level;
	protected double pressure_grnd_level;
	
	protected int humidity;
	
	protected double wind_speed;
	protected double wind_degree;
	protected double wind_gust_speed;
	
	protected int clouds;
	
	protected String precipitation;
	protected double precipitation_volume;
	
	protected long dtstamp;
	
	public WeatherConditions(JSONObject joWeatherConditions) {
		pressure = pressure_sea_level = pressure_grnd_level = -1;
		precipitation = null;
		precipitation_volume = -1;
		temp_min = temp_max = -1;
		wind_gust_speed = -1;
		
		try {
			
            // Getting JSON Array node with weather codes
            JSONArray weatherArr = joWeatherConditions.getJSONArray(GetForecastTask.TAG_WEATHER);
            this.weather_codes = new WeatherCodesArray(weatherArr);
						
			this.dtstamp = joWeatherConditions.getLong(GetForecastTask.TAG_DT) * GetForecastTask.MULTIPLIER_TO_MILISECONDS;
					
            
            JSONObject main = joWeatherConditions.getJSONObject(GetForecastTask.TAG_MAIN);
            
            try {
            	temp_min = main.getDouble(GetForecastTask.TAG_TEMP_MIN);
            	temp_max = main.getDouble(GetForecastTask.TAG_TEMP_MAX);
            	
            } catch (JSONException jsonEr) {
            	;                  	              		                 		                    	
        	}
            
            this.temp = main.getDouble(GetForecastTask.TAG_TEMP);
            this.humidity = main.getInt(GetForecastTask.TAG_HUMIDITY);
            
            try {
            	this.pressure = main.getLong(GetForecastTask.TAG_PRESSURE);                    	
            } catch (JSONException jsonEr) {
            	try {
            		this.pressure_sea_level = main.getLong(GetForecastTask.TAG_SEA_LEVEL);
            		
            	} catch (JSONException jsonEr2) {
            		this.pressure_grnd_level = main.getLong(GetForecastTask.TAG_GRND_LEVEL);                    		
            		
            	}
            }
            
            JSONObject wind = joWeatherConditions.getJSONObject(GetForecastTask.TAG_WIND);
            
            
            this.wind_speed = wind.getDouble(GetForecastTask.TAG_SPEED);
            this.wind_degree= wind.getDouble(GetForecastTask.TAG_DEG);
            
          //wind_speed = (double) Math.round(10 * wind_speed)/10;
            
            try {
            	this.wind_gust_speed = wind.getDouble(GetForecastTask.TAG_GUST);            	
            	                    	
            } catch (JSONException jsonEr) {
            	;                    	
            }
            
            
            JSONObject cloudiness = joWeatherConditions.getJSONObject(GetForecastTask.TAG_CLOUDS);
                            
            this.clouds = cloudiness.getInt(GetForecastTask.TAG_ALL);
            
            
            try {
            	JSONObject rain = joWeatherConditions.getJSONObject(GetForecastTask.TAG_RAIN);
         	
            	precipitation_volume = rain.getInt(GetForecastTask.TAG_3H);
            	precipitation = "rain";
            } 
            	catch (JSONException jsonEr) {
            		precipitation = "no";
            		precipitation_volume = -1;
            	}
            
            try {
            	JSONObject snow = joWeatherConditions.getJSONObject(GetForecastTask.TAG_SNOW);
            	
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
        }
	}
	
	public WeatherConditions(WeatherCodesArray _weather_codes) {
		this.weather_codes = _weather_codes;
		
		pressure = pressure_sea_level = pressure_grnd_level = -1;
		precipitation = null;
		precipitation_volume = -1;
		temp_min = temp_max = -1;
		wind_gust_speed = -1;
		
		
	}
	
	public double getTemp(Degree degree) {
		return degreeConverterFromKelvin(degree,this.temp);
	}
	
	public void setTemp(double _temp) {
		this.temp = _temp;
	}
	
	public double getTempMin(Degree degree) {
		return degreeConverterFromKelvin(degree,this.temp_min);
	}
	
	public void setTempMin(double _tempMin) {
		this.temp_min = _tempMin;
	}
	
	public double getTempMax(Degree degree) {
		return degreeConverterFromKelvin(degree,this.temp_max);
	}
	
	public void setTempMax(double _tempMax) {
		this.temp_max = _tempMax;
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
				break;
			case mmHg:
				result = (int) Math.round((pressure * 7.5006) / 10);
				break;
			default:
				break;
				
					
		}
				
		
		return result;
	}
	
	
	
}
