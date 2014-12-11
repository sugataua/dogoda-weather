package ua.amber_projects.dogodaweather;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WeatherDBAdapter {
	
	private static final String LOG_TAG = "TaskDBAdapter";
	
	private static final String DATABASE_NAME = "WeatherDatabase.db";
	private static final int DATABASE_VERSION = 2;
	
	
	// Tables names
	private static final String TABLE_CITY = "city";
	private static final String TABLE_WEATHER_CONDITIONS = "weather_conditions";
	private static final String TABLE_WEATHER_CODE = "weather_code";
	private static final String TABLE_WEATHER_CONDITIONS_CODE = "weather_conditions_code"; // Connection between conditions and code
	private static final String TABLE_WEATHER_FORECAST = "weather_forecast";
	private static final String TABLE_WEATHER_FORECAST_CODE = "weather_forecast_code"; // Connection between conditions and code


	//Columns names	
	
	public static final String KEY_ID = "_id";
	
	//Table CITY
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_COUNTRY = "country";
	public static final String COLUMN_LAT = "lat";
	public static final String COLUMN_LON = "lon";
	
	//Table WEATHER_CODE
	public static final String COLUMN_MAIN = "main";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_ICON = "icon";
	
	//Table WEATHER_CONDITIONS
	public static final String COLUMN_CITY_ID = "city_id";
	
	public static final String COLUMN_TEMP = "temp";
	public static final String COLUMN_TEMP_MIN = "temp_min";
	public static final String COLUMN_TEMP_MAX = "temp_max";
	
	public static final String COLUMN_HUMIDITY = "humidity";
	
	public static final String COLUMN_PRESSURE = "pressure";
	public static final String COLUMN_PRESSURE_SEA_LEVEL = "pressure_sea_level";
	public static final String COLUMN_PRESSURE_GRND_LEVEL = "pressure_grnd_level";
	
	public static final String COLUMN_WIND_SPEED = "wind_speed";
	public static final String COLUMN_WIND_DEGREE = "wind_degree";
	public static final String COLUMN_WIND_GUST = "wind_gust";
	
	public static final String COLUMN_CLOUDS = "clouds";
	
	public static final String COLUMN_PRECIPITATION = "precipitation";
	public static final String COLUMN_PRECIPITATION_VOLUME = "precipitation_volume";
	
	public static final String COLUMN_SUNRISE = "sunrise";
	public static final String COLUMN_SUNSET = "sunset";
	
	public static final String COLUMN_DT = "dt";
	
	//Table WEATHER_FORECAST
	public static final String COLUMN_TEMP_DAY = "temp_day";
	public static final String COLUMN_TEMP_EVE = "temp_eve";
	public static final String COLUMN_TEMP_NIGHT = "temp_night";
	public static final String COLUMN_TEMP_MORN = "temp_morn";
	
	//Table WEATHER_CONDITIONS_CODE
	public static final String COLUMN_W_CONDITIONS_ID = "w_conditions_id";
	public static final String COLUMN_W_CODE_ID = "w_code_id";
	
	//Table WEATHER_FORECAST_CODE
	public static final String COLUMN_W_FORECAST_ID = "w_forecast_id";
	

	
	// SQL table creation
	
	private static final String CREATE_TABLE_CITY = "create table " + TABLE_CITY + "("			
			+ KEY_ID + " integer primary key, "
			+ COLUMN_NAME + " text, "
			+ COLUMN_COUNTRY + " text, "
			+ COLUMN_LAT + " integer, "
			+ COLUMN_LON + " integer"
			+ "); ";
	
	
	private static final String CREATE_TABLE_WEATHER_CODE = "create table " + TABLE_WEATHER_CODE + "("
			+ KEY_ID + " integer primary key, "
			+ COLUMN_MAIN + " text, "
			+ COLUMN_DESCRIPTION + " text, "
			+ COLUMN_ICON + " text"
			+ "); ";
	
	
	private static final String CREATE_TABLE_WEATHER_CONDITIONS = "create table " + TABLE_WEATHER_CONDITIONS + "("
			+ KEY_ID + " integer primary key, "
			+ COLUMN_CITY_ID + " integer, "
			
			+ COLUMN_TEMP + " real, "
			+ COLUMN_TEMP_MIN + " real, "
			+ COLUMN_TEMP_MAX + " real, "
			
			+ COLUMN_HUMIDITY + " real, "
			+ COLUMN_CLOUDS + " integer, "
			
			+ COLUMN_PRESSURE + " real, "
			+ COLUMN_PRESSURE_SEA_LEVEL + " real, "
			+ COLUMN_PRESSURE_GRND_LEVEL + " real, "
			
			+ COLUMN_WIND_SPEED + " real, "
			+ COLUMN_WIND_DEGREE + " real, "
			+ COLUMN_WIND_GUST + " real, "
			
			+ COLUMN_PRECIPITATION + " text, "
			+ COLUMN_PRECIPITATION_VOLUME + " integer, "
			
			+ COLUMN_SUNRISE + " integer, "
			+ COLUMN_SUNSET + " integer, "
						
			+ COLUMN_DT + " integer, "
			
			+ "foreign key(" + COLUMN_CITY_ID + ") references " 
			+ TABLE_CITY + "(" + KEY_ID + ")"			
			
			+ "); ";
	
	private static final String CREATE_TABLE_WEATHER_FORECAST = "create table " + TABLE_WEATHER_FORECAST + "("
			+ KEY_ID + " integer primary key, "
			+ COLUMN_CITY_ID + " integer, "
			
			+ COLUMN_TEMP_DAY + " real, "
			+ COLUMN_TEMP_MIN + " real, "
			+ COLUMN_TEMP_MAX + " real, "
			+ COLUMN_TEMP_EVE + " real, "
			+ COLUMN_TEMP_NIGHT + " real, "
			+ COLUMN_TEMP_MORN +  " real, "
			
			+ COLUMN_HUMIDITY + " real, "
			+ COLUMN_CLOUDS + " integer, "
			
			+ COLUMN_PRESSURE + " real, "
			+ COLUMN_PRESSURE_SEA_LEVEL + " real, "
			+ COLUMN_PRESSURE_GRND_LEVEL + " real, "
			
			+ COLUMN_WIND_SPEED + " real, "
			+ COLUMN_WIND_DEGREE + " real, "
			+ COLUMN_WIND_GUST + " real, "
			
			+ COLUMN_PRECIPITATION + " text, "
			+ COLUMN_PRECIPITATION_VOLUME + " integer, "
						
			+ COLUMN_DT + " integer, "
			
			+ "foreign key(" + COLUMN_CITY_ID + ") references " 
			+ TABLE_CITY + "(" + KEY_ID + ") "			
			
			+ "); ";
	
	
	private static final String CREATE_TABLE_WEATHER_CONDITIONS_CODE = "create table " + TABLE_WEATHER_CONDITIONS_CODE + "("
			+ KEY_ID + " integer primary key, "
			
			+ COLUMN_W_CONDITIONS_ID + " integer, "
			+ COLUMN_W_CODE_ID  + " integer, "
			
			+ "foreign key(" + COLUMN_W_CONDITIONS_ID + ") references " 
			+ TABLE_WEATHER_CONDITIONS + "(" + KEY_ID + "), "	
			
			+ "foreign key(" + COLUMN_W_CODE_ID + ") references " 
			+ TABLE_WEATHER_CODE + "(" + KEY_ID + ") "			
			
			+ "); ";
	
	
	private static final String CREATE_TABLE_WEATHER_FORECAST_CODE = "create table " + TABLE_WEATHER_FORECAST_CODE + "("
			+ KEY_ID + " integer primary key, "
			
			+ COLUMN_W_FORECAST_ID + " integer, "
			+ COLUMN_W_CODE_ID  + " integer, "
			
			+ "foreign key(" + COLUMN_W_FORECAST_ID + ") references " 
			+ TABLE_WEATHER_FORECAST + "(" + KEY_ID + "), "	
			
			+ "foreign key(" + COLUMN_W_CODE_ID + ") references " 
			+ TABLE_WEATHER_CODE + "(" + KEY_ID + ") "			
			
			+ "); ";
			
	

	// Переменная для хранения объекта БД
	private SQLiteDatabase db;
	
	//Application context
	private final Context context;
		
	private weatherDBHelper dbHelper;
	
	public WeatherDBAdapter(Context _context) {
		context = _context;
		dbHelper = new weatherDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public WeatherDBAdapter open() throws SQLException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLException e) {
			db = dbHelper.getReadableDatabase();
		}
		return this;
	}
	
	public void close() {
		db.close();
	}
	
	
	public long insertCurrentWeather( CurrentWeatherConditions _currentWeather) {
		long cityRowID;
		long[] codesRowID;
		long currentWeatherRowID;
		
		
//		Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//
//		if (c.moveToFirst()) {
//		    while ( !c.isAfterLast() ) {
//		    	Log.d(LOG_TAG, "Table Name=> "+c.getString(0));		        
//		        c.moveToNext();
//		    }
//		}

		
		cityRowID = insertCity( _currentWeather.getCity() );
		codesRowID = insertWeatherCodesArray( _currentWeather.getWeatherConditions() );
		
		ContentValues cv = new ContentValues();
		
		cv.put(COLUMN_CITY_ID, cityRowID);
		
		cv.put(COLUMN_TEMP, _currentWeather.getTemp(Degree.Kelvin));
		cv.put(COLUMN_TEMP_MIN, _currentWeather.getTempMin(Degree.Kelvin));
		cv.put(COLUMN_TEMP_MAX, _currentWeather.getTempMax(Degree.Kelvin));
		
		cv.put(COLUMN_HUMIDITY, _currentWeather.getHumidity());
		cv.put(COLUMN_CLOUDS, _currentWeather.getClouds());
		
		cv.put(COLUMN_PRESSURE, _currentWeather.getPressure());
		cv.put(COLUMN_PRESSURE_SEA_LEVEL, _currentWeather.getPressureSeaLevel());
		cv.put(COLUMN_PRESSURE_GRND_LEVEL, _currentWeather.getPressureGrndLevel());

		cv.put(COLUMN_WIND_SPEED, _currentWeather.getWindSpeed());
		cv.put(COLUMN_WIND_DEGREE, _currentWeather.getWindDegree());
		cv.put(COLUMN_WIND_GUST, _currentWeather.getWindGust());
		
		cv.put(COLUMN_PRECIPITATION, _currentWeather.getPrecipitation());
		cv.put(COLUMN_PRECIPITATION_VOLUME, _currentWeather.getPrecipitationVolume());		
		
		cv.put(COLUMN_SUNRISE, _currentWeather.getSunrise());
		cv.put(COLUMN_SUNSET, _currentWeather.getSunset());
		
		cv.put(COLUMN_DT, _currentWeather.getDT());
		
		
		currentWeatherRowID = db.insert(TABLE_WEATHER_CONDITIONS, null, cv);
		
		for (int i = 0; i < codesRowID.length; i++) {
			insertWeatherConditionsCode(currentWeatherRowID,codesRowID[i]);			
		}
		
		Log.d(LOG_TAG, "CurrentWeather inserted, id=" + currentWeatherRowID);
		
		
		return currentWeatherRowID;
	}
	
	
	
	public long insertOrUpdateCurrentWeather( CurrentWeatherConditions _currentWeather) {
		long cityRowID;
		long[] codesRowID;
		long currentWeatherRowID = -1;
		String selection = COLUMN_CITY_ID + "=" + _currentWeather.getCity().getCityId() + " and " +
				COLUMN_DT + "=" + _currentWeather.getDT();
		String[] columns = {KEY_ID};
		
		Cursor c = db.query(TABLE_WEATHER_CONDITIONS, columns, selection, null, null, null, null);	
		
		if (c.moveToFirst()) {
			if (c.getCount() != 1)
			{
				Log.e(LOG_TAG, "Duplicate time of current weather");
			} else {
				ContentValues cv = new ContentValues();
				
				int idColIndex = c.getColumnIndex(KEY_ID);
				
				long cwID = c.getLong(idColIndex);
				
				cv.put(COLUMN_TEMP, _currentWeather.getTemp(Degree.Kelvin));
				cv.put(COLUMN_TEMP_MIN, _currentWeather.getTempMin(Degree.Kelvin));
				cv.put(COLUMN_TEMP_MAX, _currentWeather.getTempMax(Degree.Kelvin));
				
				cv.put(COLUMN_HUMIDITY, _currentWeather.getHumidity());
				cv.put(COLUMN_CLOUDS, _currentWeather.getClouds());
				
				cv.put(COLUMN_PRESSURE, _currentWeather.getPressure());
				cv.put(COLUMN_PRESSURE_SEA_LEVEL, _currentWeather.getPressureSeaLevel());
				cv.put(COLUMN_PRESSURE_GRND_LEVEL, _currentWeather.getPressureGrndLevel());

				cv.put(COLUMN_WIND_SPEED, _currentWeather.getWindSpeed());
				cv.put(COLUMN_WIND_DEGREE, _currentWeather.getWindDegree());
				cv.put(COLUMN_WIND_GUST, _currentWeather.getWindGust());
				
				cv.put(COLUMN_PRECIPITATION, _currentWeather.getPrecipitation());
				cv.put(COLUMN_PRECIPITATION_VOLUME, _currentWeather.getPrecipitationVolume());		
				
				cv.put(COLUMN_SUNRISE, _currentWeather.getSunrise());
				cv.put(COLUMN_SUNSET, _currentWeather.getSunset());
				
				deleteWeatherConditionsCode(cwID);
				
				codesRowID = insertWeatherCodesArray( _currentWeather.getWeatherConditions() );
				
				for (int i = 0; i < codesRowID.length; i++) {
					insertWeatherConditionsCode(cwID,codesRowID[i]);			
				}
				
				db.update(
						TABLE_WEATHER_CONDITIONS,
						cv,
						KEY_ID + " = ?", 
						new String[] { String.valueOf(cwID) } );
				
				currentWeatherRowID = cwID;
				
				Log.d(LOG_TAG, "Updated current weather: " + cwID);				
			}
		} else {
			currentWeatherRowID = insertCurrentWeather(_currentWeather);
			
		}
		
		c.close();
		return currentWeatherRowID;
		
		
	}
	
	
	public void deleteOldWeatherConditions(long _beforeDTstamp) {
		String selection = COLUMN_DT + " < " + _beforeDTstamp;
		String[] columns = {KEY_ID};
		
		Cursor c = db.query(TABLE_WEATHER_CONDITIONS, columns, selection, null, null, null, null);
		
		if (c.moveToFirst()) {
			
			do {
				int idColIndex = c.getColumnIndex(KEY_ID);
				long id_current = c.getLong(idColIndex);
				deleteWeatherConditionsCode(id_current);
				deleteWeatherConditions(id_current);
				Log.d(LOG_TAG, "Deleted from " + TABLE_WEATHER_CONDITIONS + " id=" + String.valueOf(id_current));
			} while(c.moveToNext());
			
		}
				
	}
	
	public int deleteWeatherConditions(long _id){
		return db.delete(TABLE_WEATHER_CONDITIONS,
				KEY_ID + " = ?",
				new String[] { String.valueOf(_id) });
	}
	
	
	public void deleteOldWeatherForecast(long _beforeDTstamp) {
		String selection = COLUMN_DT + " < " + _beforeDTstamp;
		String[] columns = {KEY_ID};
		
		Cursor c = db.query(TABLE_WEATHER_FORECAST, columns, selection, null, null, null, null);
		
		if (c.moveToFirst()) {
			int idColIndex = c.getColumnIndex(KEY_ID);			
			do {
				
				long id_current = c.getLong(idColIndex);
				deleteForecastConditionsCode(id_current);
				deleteWeatherForecast(id_current);
				Log.d(LOG_TAG, "Deleted from " + TABLE_WEATHER_FORECAST + " id=" + String.valueOf(id_current));
			} while(c.moveToNext());
			
		}
				
	}
	
	
	public int deleteWeatherForecast(long _id){
		return db.delete(TABLE_WEATHER_FORECAST,
				KEY_ID + " = ?",
				new String[] { String.valueOf(_id) });
	}
	
	public void deleteAllWeatherConditions() {
		db.delete(TABLE_WEATHER_CONDITIONS, null, null);
	}
	
	public CurrentWeatherConditions getLastCurrentWeather(long _cityRowID, long _afterDTstamp) {
		CurrentWeatherConditions cwc = null;
		String selection = COLUMN_CITY_ID + "=" + _cityRowID + " and "
				+ COLUMN_DT + ">=" + _afterDTstamp + " and "
				+ COLUMN_SUNRISE + " is not null ";
		
		String orderBy = COLUMN_DT + " DESC";
		
		
		
		Cursor c = db.query(TABLE_WEATHER_CONDITIONS, null, selection, null, null, null, orderBy);
		
		if (c.moveToFirst()) {
			int idColIndex = c.getColumnIndex(KEY_ID);
			int cityIDColIndex = c.getColumnIndex(COLUMN_CITY_ID);
			int tempColIndex = c.getColumnIndex(COLUMN_TEMP);
			int tempMinColIndex = c.getColumnIndex(COLUMN_TEMP_MIN);
			int tempMaxColIndex = c.getColumnIndex(COLUMN_TEMP_MAX);
			int pressureColIndex = c.getColumnIndex(COLUMN_PRESSURE);
			int pressureSLColIndex = c.getColumnIndex(COLUMN_PRESSURE_SEA_LEVEL);
			int pressureGLColIndex = c.getColumnIndex(COLUMN_PRESSURE_GRND_LEVEL);
			int windSpeedColIndex = c.getColumnIndex(COLUMN_WIND_SPEED);
			int windDegreeColIndex = c.getColumnIndex(COLUMN_WIND_DEGREE);
			int windGustColIndex = c.getColumnIndex(COLUMN_WIND_GUST);
			int humidityColIndex = c.getColumnIndex(COLUMN_HUMIDITY);
			int cloudsColIndex = c.getColumnIndex(COLUMN_CLOUDS);
			int sunriseColIndex = c.getColumnIndex(COLUMN_SUNRISE);
			int sunsetColIndex = c.getColumnIndex(COLUMN_SUNSET);
			int precColIndex = c.getColumnIndex(COLUMN_PRECIPITATION);
			int precVolumeColIndex = c.getColumnIndex(COLUMN_PRECIPITATION_VOLUME);
			int dtColIndex = c.getColumnIndex(COLUMN_DT);
			
			City city = getCity(c.getInt(cityIDColIndex));
			WeatherCodesArray wcodeArr;
			
			
			wcodeArr = getHourlyWeatherCodesArray(c.getInt(idColIndex));
			
			cwc = new CurrentWeatherConditions(city,wcodeArr);
			
			
			cwc.setTemp(c.getDouble(tempColIndex));
			
			if (! c.isNull(tempMinColIndex)) {
				cwc.setTempMin(c.getDouble(tempMinColIndex));					
			}
			
			if (! c.isNull(tempMaxColIndex)) {
				cwc.setTempMax(c.getDouble(tempMaxColIndex));					
			}
			
			
			cwc.setHumidity(c.getInt(humidityColIndex));
			cwc.setClouds(c.getInt(cloudsColIndex));
			
										
			
			cwc.setPressure(c.getDouble(pressureColIndex));
			if (! c.isNull(pressureSLColIndex)) {
				cwc.setPressureSeaLevel(c.getDouble(pressureSLColIndex));					
			}
			
			if (! c.isNull(pressureGLColIndex)) {
				cwc.setPressureGrndLevel(c.getDouble(pressureGLColIndex));				
			}
			
			
			cwc.setPrecipitation(c.getString(precColIndex));
			cwc.setPrecipitationVolume(c.getDouble(precVolumeColIndex));
			
			cwc.setWindSpeed(c.getDouble(windSpeedColIndex));
			cwc.setWindDegree(c.getDouble(windDegreeColIndex));
			if (! c.isNull(windGustColIndex)) {
				cwc.setWindGust(c.getDouble(windGustColIndex));					
			}
			
			
			cwc.setSunrise(c.getLong(sunriseColIndex));
			cwc.setSunset(c.getLong(sunsetColIndex));
			
			cwc.setDT(c.getLong(dtColIndex));
			
			
		}
		
		c.close();
		
		return cwc;
		
		
		
		
		
	}
	
	
	public long insertOrUpdateDailyForecast(DailyForecast _dfData) {
		long cityRowID = insertCity( _dfData.getCity() );
		DailyWeatherConditions[] forecastWeatherConditions =  _dfData.getDailyWeather();
		
		for (int i = 0; i < forecastWeatherConditions.length; i++) {
			insertOrUpdateDailyWeatherConditions(cityRowID, forecastWeatherConditions[i]);
			
		}
		
		return 0;				
	}
	
	public DailyForecast getDailyForecast(long _cityRowID, long _afterDTstamp) {
		DailyForecast dfData = null;
		DailyWeatherConditions[] fwc;
		String selection = COLUMN_CITY_ID + "=" + _cityRowID + " and "
				+ COLUMN_DT + ">=" + _afterDTstamp; 
		
		String orderBy = COLUMN_DT;
		String[] columns = {KEY_ID};		
		
		
		Cursor c = db.query(TABLE_WEATHER_FORECAST, columns, selection, null, null, null, orderBy);
		
		if (c.moveToFirst()) {
			int idColIndex = c.getColumnIndex(KEY_ID);
			int rowCount = c.getCount();
			long wCondRowId;// = c.getLong(idColIndex);
			int i = 0;
			
			fwc = new DailyWeatherConditions[rowCount];
			
//			for (int i = 0; i < rowCount; i++) {
//				fwc[i] = getDailyWeatherConditions(wCondRowId);							
//				
//				if (c.moveToNext()) {
//					wCondRowId = c.getLong(idColIndex);					
//				}
//				
//			}
			
			do {
				wCondRowId = c.getLong(idColIndex);
				fwc[i] = getDailyWeatherConditions(wCondRowId);								
				i++;
			} while (c.moveToNext());
								
			dfData = new DailyForecast();
			
			City city = getCity(_cityRowID);
			
			dfData.setCity(city);
			dfData.setDailyWeather(fwc);
						
		}
		c.close();		
		
		return dfData;
		
	}
	
	
	public long insertOrUpdateDailyWeatherConditions(long _cityRowID, DailyWeatherConditions _dwcData) {
		long[] codesRowID;
		long weatherConditionsRowID = -1;
		
		String selection = COLUMN_CITY_ID + "=" + _cityRowID+ " and " +
				COLUMN_DT + "=" + _dwcData.getDT();
		String[] columns = {KEY_ID};
		
		Cursor c = db.query(TABLE_WEATHER_FORECAST, columns, selection, null, null, null, null);
		
		if (c.moveToFirst()) {
			if (c.getCount() != 1) {
				Log.e(LOG_TAG, "Duplicate day weather conditions (by time)");				
			} else {
				
				ContentValues cv = new ContentValues();
				
				int idColIndex = c.getColumnIndex(KEY_ID);				
				long dailyWeatherID = c.getLong(idColIndex);
				
				cv.put(COLUMN_TEMP_DAY, _dwcData.getTempDay(Degree.Kelvin));
				cv.put(COLUMN_TEMP_MIN, _dwcData.getTempDayMin(Degree.Kelvin));
				cv.put(COLUMN_TEMP_MAX, _dwcData.getTempDayMax(Degree.Kelvin));
				cv.put(COLUMN_TEMP_EVE, _dwcData.getTempEve(Degree.Kelvin));
				cv.put(COLUMN_TEMP_NIGHT, _dwcData.getTempNight(Degree.Kelvin));
				cv.put(COLUMN_TEMP_MORN, _dwcData.getTempMorn(Degree.Kelvin));
					
				
				cv.put(COLUMN_HUMIDITY, _dwcData.getHumidity());
				cv.put(COLUMN_CLOUDS, _dwcData.getClouds());
				
				cv.put(COLUMN_PRESSURE, _dwcData.getPressure());
				cv.put(COLUMN_PRESSURE_SEA_LEVEL, _dwcData.getPressureSeaLevel());
				cv.put(COLUMN_PRESSURE_GRND_LEVEL, _dwcData.getPressureGrndLevel());

				cv.put(COLUMN_WIND_SPEED, _dwcData.getWindSpeed());
				cv.put(COLUMN_WIND_DEGREE, _dwcData.getWindDegree());
				cv.put(COLUMN_WIND_GUST, _dwcData.getWindGust());
				
				cv.put(COLUMN_PRECIPITATION, _dwcData.getPrecipitation());
				cv.put(COLUMN_PRECIPITATION_VOLUME, _dwcData.getPrecipitationVolume());	
				
				
				deleteForecastConditionsCode(dailyWeatherID);
				
				codesRowID = insertWeatherCodesArray( _dwcData.getWeatherConditions() );
				
				for (int i = 0; i < codesRowID.length; i++) {
					insertForecastConditionsCode(dailyWeatherID,codesRowID[i]);			
				}
				
				weatherConditionsRowID = db.update(
						TABLE_WEATHER_FORECAST,
						cv,
						KEY_ID + " = ?", 
						new String[] { String.valueOf(dailyWeatherID) } );	
				
				weatherConditionsRowID = dailyWeatherID;
				
				
				Log.d(LOG_TAG, "Daily weather updated, id=" + weatherConditionsRowID);
				
								
											
			}
		} else {			
			weatherConditionsRowID = insertDailyWeatherConditions(_cityRowID, _dwcData);
		}
		
		c.close();
		
		return weatherConditionsRowID;
	}
	
	
	public long insertDailyWeatherConditions(long _cityRowID, DailyWeatherConditions _dwcData) {
		
		long[] codesRowID;
		long weatherConditionsRowID;
		
		codesRowID = insertWeatherCodesArray( _dwcData.getWeatherConditions() );
		
		ContentValues cv = new ContentValues();
		
		cv.put(COLUMN_CITY_ID, _cityRowID);
		
		cv.put(COLUMN_TEMP_DAY, _dwcData.getTempDay(Degree.Kelvin));
		cv.put(COLUMN_TEMP_MIN, _dwcData.getTempDayMin(Degree.Kelvin));
		cv.put(COLUMN_TEMP_MAX, _dwcData.getTempDayMax(Degree.Kelvin));
		cv.put(COLUMN_TEMP_EVE, _dwcData.getTempEve(Degree.Kelvin));
		cv.put(COLUMN_TEMP_NIGHT, _dwcData.getTempNight(Degree.Kelvin));
		cv.put(COLUMN_TEMP_MORN, _dwcData.getTempMorn(Degree.Kelvin));
			
		
		cv.put(COLUMN_HUMIDITY, _dwcData.getHumidity());
		cv.put(COLUMN_CLOUDS, _dwcData.getClouds());
		
		cv.put(COLUMN_PRESSURE, _dwcData.getPressure());
		cv.put(COLUMN_PRESSURE_SEA_LEVEL, _dwcData.getPressureSeaLevel());
		cv.put(COLUMN_PRESSURE_GRND_LEVEL, _dwcData.getPressureGrndLevel());

		cv.put(COLUMN_WIND_SPEED, _dwcData.getWindSpeed());
		cv.put(COLUMN_WIND_DEGREE, _dwcData.getWindDegree());
		cv.put(COLUMN_WIND_GUST, _dwcData.getWindGust());
		
		cv.put(COLUMN_PRECIPITATION, _dwcData.getPrecipitation());
		cv.put(COLUMN_PRECIPITATION_VOLUME, _dwcData.getPrecipitationVolume());		
		
		cv.put(COLUMN_DT, _dwcData.getDT());
		
		
		weatherConditionsRowID = db.insert(TABLE_WEATHER_FORECAST, null, cv);
			
		
		for (int i = 0; i < codesRowID.length; i++) {
			insertForecastConditionsCode(weatherConditionsRowID,codesRowID[i]);			
		}
		
		Log.d(LOG_TAG, "Daily weather inserted, id=" + weatherConditionsRowID);
		
				
		return weatherConditionsRowID;
	}
	
	public DailyWeatherConditions getDailyWeatherConditions(long _dwcRowID) {
		
		DailyWeatherConditions dwc = null;
		
		String selection = KEY_ID + "=" + _dwcRowID;
		
		Cursor c = db.query(TABLE_WEATHER_FORECAST, null, selection, null, null, null, null);
		
		if (c.moveToFirst()) {
			int idColIndex = c.getColumnIndex(KEY_ID);						
			int tempDayColIndex = c.getColumnIndex(COLUMN_TEMP_DAY);
			int tempMinColIndex = c.getColumnIndex(COLUMN_TEMP_MIN);
			int tempMaxColIndex = c.getColumnIndex(COLUMN_TEMP_MAX);
			int tempEveColIndex = c.getColumnIndex(COLUMN_TEMP_EVE);
			int tempNightColIndex = c.getColumnIndex(COLUMN_TEMP_NIGHT);
			int tempMornColIndex = c.getColumnIndex(COLUMN_TEMP_MORN);			
			int pressureColIndex = c.getColumnIndex(COLUMN_PRESSURE);
			int pressureSLColIndex = c.getColumnIndex(COLUMN_PRESSURE_SEA_LEVEL);
			int pressureGLColIndex = c.getColumnIndex(COLUMN_PRESSURE_GRND_LEVEL);
			int windSpeedColIndex = c.getColumnIndex(COLUMN_WIND_SPEED);
			int windDegreeColIndex = c.getColumnIndex(COLUMN_WIND_DEGREE);
			int windGustColIndex = c.getColumnIndex(COLUMN_WIND_GUST);
			int humidityColIndex = c.getColumnIndex(COLUMN_HUMIDITY);
			int cloudsColIndex = c.getColumnIndex(COLUMN_CLOUDS);						
			int precColIndex = c.getColumnIndex(COLUMN_PRECIPITATION);
			int precVolumeColIndex = c.getColumnIndex(COLUMN_PRECIPITATION_VOLUME);
			int dtColIndex = c.getColumnIndex(COLUMN_DT);
			
			WeatherCodesArray wCodeArr = getDailyWeatherCodesArray(c.getInt(idColIndex));
			
			dwc = new DailyWeatherConditions();
			
			dwc.setWeatherConditions(wCodeArr);
			
			dwc.setTempDay(c.getDouble(tempDayColIndex));
			dwc.setTempDayMin(c.getDouble(tempMinColIndex));					
			dwc.setTempDayMax(c.getDouble(tempMaxColIndex));
			dwc.setTempMorn(c.getDouble(tempMornColIndex));
			dwc.setTempNight(c.getDouble(tempNightColIndex));
			dwc.setTempEve(c.getDouble(tempEveColIndex));
			
			dwc.setHumidity(c.getInt(humidityColIndex));
			dwc.setClouds(c.getInt(cloudsColIndex));
			
			dwc.setPressure(c.getDouble(pressureColIndex));
			if (! c.isNull(pressureSLColIndex)) {
				dwc.setPressureSeaLevel(c.getDouble(pressureSLColIndex));					
			}
			
			if (! c.isNull(pressureGLColIndex)) {
				dwc.setPressureGrndLevel(c.getDouble(pressureGLColIndex));				
			}
			
			
			dwc.setPrecipitation(c.getString(precColIndex));
			dwc.setPrecipitationVolume(c.getDouble(precVolumeColIndex));
			
			dwc.setWindSpeed(c.getDouble(windSpeedColIndex));
			dwc.setWindDegree(c.getDouble(windDegreeColIndex));
			if (! c.isNull(windGustColIndex)) {
				dwc.setWindGust(c.getDouble(windGustColIndex));					
			}
		
			dwc.setDT(c.getLong(dtColIndex));
			
		}
		
		c.close();
				
		return dwc;
	}
	
	public void deleteAllWeatherForecast() {
		db.delete(TABLE_WEATHER_FORECAST, null, null);
	}
	

	
	
	public long insertCity(City _city) {
		long cityRowID;		
		
		String selection = KEY_ID + "=" + _city.getCityId(); 
		
		Cursor c = db.query(TABLE_CITY, null, selection, null, null, null, null);
		
		if (c.moveToFirst()) {
			Log.d(LOG_TAG, "City is present in table");
			
			cityRowID = _city.getCityId();
		} else {
			ContentValues cv = new ContentValues();
			
			cv.put(KEY_ID, _city.getCityId());
			cv.put(COLUMN_NAME, _city.getCityName());
			cv.put(COLUMN_COUNTRY, _city.getCountry());
			cv.put(COLUMN_LAT, _city.getLat());
			cv.put(COLUMN_LON, _city.getLon());
			
			cityRowID = db.insert(TABLE_CITY, null, cv);
			Log.d(LOG_TAG, "Insert new city in table, id=" + cityRowID);
		}
		c.close();
						
		if (cityRowID == -1) {
			Log.e(LOG_TAG, "Error while inserting!");				
		}
		
		Log.d(LOG_TAG, "Work with city, id=" + cityRowID);		
		
		return cityRowID;
	}
	
	
	public City getCity(long _rowID) {
		City city = null;
				
		String selection = KEY_ID + " = " + _rowID;
		
		Cursor c = db.query(TABLE_CITY, null, selection, null, null, null, null);
		
		if (c.moveToFirst()) {
			
			int nameColIndex = c.getColumnIndex(COLUMN_NAME);
			int countryColIndex = c.getColumnIndex(COLUMN_COUNTRY);
			int latColIndex = c.getColumnIndex(COLUMN_LAT);
			int lonColIndex = c.getColumnIndex(COLUMN_LON);
			
			city = new City(
					(int) _rowID,
					c.getString(nameColIndex),
					c.getString(countryColIndex),
					c.getDouble(latColIndex),
					c.getDouble(lonColIndex)
					);			
					
		}
		
		c.close();
		
		return city;
		
	}
	
	public void deleteAllCity() {
		db.delete(TABLE_CITY, null, null);
	}
	
	
	
	public long insertWeatherCode(WeatherCode _weatherCondition) {
		long rowID;
				
		String selection = KEY_ID + " = " + _weatherCondition.getId(); 
		
		Cursor c = db.query(TABLE_WEATHER_CODE, null, selection, null, null, null, null);
		if (c.moveToFirst()) {
			rowID = _weatherCondition.getId();									
		} else {
			ContentValues cv = new ContentValues();
			
			cv.put(KEY_ID, _weatherCondition.getId());
			cv.put(COLUMN_MAIN, _weatherCondition.getMain());
			cv.put(COLUMN_DESCRIPTION, _weatherCondition.getDescription());
			cv.put(COLUMN_ICON, _weatherCondition.getIcon());
			
			rowID = db.insert(TABLE_WEATHER_CODE, null, cv);						
		}
		
		c.close();
		
				
		if (rowID == -1) {
			Log.e(LOG_TAG, "Error while inserting!");				
		}
		
		return rowID;
	}
	
	public WeatherCode getWeatherCode(long _rowID) {
		WeatherCode weatherCode = null;		
		String selection = KEY_ID + "=" + _rowID;
		
		Cursor c = db.query(TABLE_WEATHER_CODE, null, selection, null, null, null, null);
		
		if (c.moveToFirst()) {
			int mainColIndex = c.getColumnIndex(COLUMN_MAIN);
			int descrColIndex = c.getColumnIndex(COLUMN_DESCRIPTION);
			int iconColIndex = c.getColumnIndex(COLUMN_ICON);
			
			
			
			weatherCode = new WeatherCode(
					(int) _rowID,
					c.getString(mainColIndex), 
					c.getString(descrColIndex), 
					c.getString(iconColIndex)
					);
						
		}
		
		c.close();
		
		return weatherCode;		
	}
	
	public void deleteAllWeatherCode() {
		db.delete(TABLE_WEATHER_CODE, null, null);
	}
	
	
	public long[] insertWeatherCodesArray(WeatherCodesArray _wCArray) {
		long[] rowIDs;
		
		WeatherCode[] codesArray = _wCArray.getWeatherCodes();
		rowIDs = new long[codesArray.length];
		
		for (int i = 0; i < codesArray.length; i++) {
			rowIDs[i] = insertWeatherCode(codesArray[i]); 			
		}			
		
		return rowIDs;		
	}
	
	public WeatherCodesArray getHourlyWeatherCodesArray(long _weatherConditionsID) {
		
		WeatherCodesArray wcodeArr = null;
		
		String selection = COLUMN_W_CONDITIONS_ID + "=" + _weatherConditionsID;		
		
		Cursor cursor = db.query(TABLE_WEATHER_CONDITIONS_CODE, null, selection, null, null, null, null);			
		
		
		if (cursor.moveToFirst()) {
			
			int rowCount =  cursor.getCount();
			//int condColIndex = cursor.getColumnIndex(COLUMN_W_CONDITIONS_ID);
			int codeColIndex = cursor.getColumnIndex(COLUMN_W_CODE_ID);
					
			WeatherCode codesArr [] = new WeatherCode[rowCount];
			
			for (int i = 0; i < rowCount; i++) {
				long wcodeIDnext = cursor.getLong(codeColIndex);
				codesArr[i] = getWeatherCode(wcodeIDnext);
				
				
				cursor.moveToNext();
									
			} 
			cursor.close();
			
			wcodeArr = new WeatherCodesArray(codesArr);
		}
		
		return wcodeArr;
			
		
	}
	
	
	
	public WeatherCodesArray getDailyWeatherCodesArray(long _weatherForecastConditionsID) {
		
		WeatherCodesArray wcodeArr = null;
		
		String selection = COLUMN_W_FORECAST_ID + "=" + _weatherForecastConditionsID;		
		
		Cursor cursor = db.query(TABLE_WEATHER_FORECAST_CODE, null, selection, null, null, null, null);			
		
		
		if (cursor.moveToFirst()) {
			
			int rowCount =  cursor.getCount();
			//int condColIndex = cursor.getColumnIndex(COLUMN_W_FORECAST_ID);
			int codeColIndex = cursor.getColumnIndex(COLUMN_W_CODE_ID);
					
			WeatherCode codesArr [] = new WeatherCode[rowCount];
			
			//Log.d("RowCount", "" + rowCount);
			
			for (int i = 0; i < rowCount; i++) {
				long wcodeIDnext = cursor.getLong(codeColIndex);
				codesArr[i] = getWeatherCode(wcodeIDnext);								
				cursor.moveToNext();
									
			} 
			cursor.close();
			
			wcodeArr = new WeatherCodesArray(codesArr);
		}
		
		return wcodeArr;
			
		
	}
	
	
	
	public long insertWeatherConditionsCode(long wConditionsID, long wCodeID) {
		long rowID;		
		ContentValues cv = new ContentValues();
				
		cv.put(COLUMN_W_CONDITIONS_ID, wConditionsID);
		cv.put(COLUMN_W_CODE_ID, wCodeID);
				
		rowID = db.insert(TABLE_WEATHER_CONDITIONS_CODE, null, cv);	
		
		return rowID;
	}
	
	
	public int deleteWeatherConditionsCode(long _wConditionsID) {
			
		return db.delete(TABLE_WEATHER_CONDITIONS_CODE,
				COLUMN_W_CONDITIONS_ID + " = ?", 
				new String[] { String.valueOf(_wConditionsID) } );
	}
	
	public void deleteAllConditionsCode() {
		db.delete(TABLE_WEATHER_CONDITIONS_CODE, null, null);
	}
	
	
	
	
	
	
		public long insertForecastConditionsCode(long _wForecastConditionsID, long _wCodeID) {
		long rowID;		
		ContentValues cv = new ContentValues();
				
		cv.put(COLUMN_W_FORECAST_ID, _wForecastConditionsID);
		cv.put(COLUMN_W_CODE_ID, _wCodeID);
				
		rowID = db.insert(TABLE_WEATHER_FORECAST_CODE, null, cv);	
		
		return rowID;
	}
		
	public int deleteForecastConditionsCode(long _wForecastConditionsID) {

		return db.delete(TABLE_WEATHER_FORECAST_CODE,
				COLUMN_W_FORECAST_ID + " = ?",
				new String[] { String.valueOf(_wForecastConditionsID) });
	}
	
	public void deleteAllForecastConditionsCode() {
		db.delete(TABLE_WEATHER_FORECAST_CODE, null, null);
	}
	
	
	
	public void cleanAllTables() {
		deleteAllCity();
		deleteAllWeatherCode();
		deleteAllWeatherConditions();
		deleteAllWeatherForecast();
		deleteAllConditionsCode();
		deleteAllForecastConditionsCode();
	}
/*	// Метод для добавления данных, возвращает индекс 
	// свежевставленного объекта
	public long insertEntry( SampleObject _SampleObject ) {
		// Здесь создается объект ContentValues, содержащий 
		// нужные поля и  производится вставка
		//return index;
	return 0;
	}
	
	
	// Метод для удаления строки таблицы по индексу
	public boolean removeEntry(long _rowIndex) {
	return db.delete(DATABASE_TABLE, KEY_ID + "=" + 
	_rowIndex, null) > 0;
	}
	// Метод для получения всех данных.
	// Возвращает курсор, который можно использовать для 
	// привязки к адаптерам типа SimpleCursorAdapter 
	public Cursor getAllEntries() {
	return db.query(DATABASE_TABLE, new String[] { KEY_ID, KEY_NAME },
	null, null, null, null, null);
	}
	// Возвращает экземпляр объекта по индексу 
	public SampleObject getEntry(long _rowIndex) {
	// Получите курсор, указывающий на нужные данные из БД
	// и создайте новый объект, используя этими данными
	// Если ничего не найдено, верните null
	return objectInstance;
	}
	// Изменяет объект по индексу
	
	// Увы, это не ORM :(
	public boolean updateEntry(long _rowIndex, SampleObject _SampleObject) {
	// Создайте объект ContentValues на основе свойств SampleObject 
	// и используйте его для обновления строки в таблице
	return true; // Если удалось обновить, иначе false :)
	}
	
	*/
	
	private static class weatherDBHelper extends SQLiteOpenHelper {
		public weatherDBHelper(Context context, String name, 
				CursorFactory factory,  int version) {
				super(context, name, factory, version);
		}
		
		@Override
		public void onCreate(SQLiteDatabase _db) {
			Log.d("TaskDBAdapter", "Creaing database:");
			_db.execSQL(CREATE_TABLE_CITY);
			_db.execSQL(CREATE_TABLE_WEATHER_CODE);
			_db.execSQL(CREATE_TABLE_WEATHER_CONDITIONS);
			_db.execSQL(CREATE_TABLE_WEATHER_FORECAST);
			_db.execSQL(CREATE_TABLE_WEATHER_CONDITIONS_CODE);
			_db.execSQL(CREATE_TABLE_WEATHER_FORECAST_CODE);
		
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
		// Выдача сообщения в журнал, полезно при отладке
			Log.w("TaskDBAdapter", "Upgrading from version " + _oldVersion
			+ " to " + _newVersion
			+ ", which will destroy all old data");
			// Обновляем БД до новой версии.
			// В простейшем случае убиваем старую БД
			// и заново создаем новую.
			// В реальной жизни стоит подумать о пользователях 
			// вашего приложения и их реакцию на потерю старых данных.
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER_CODE);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER_CONDITIONS);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER_FORECAST);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER_CONDITIONS_CODE);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER_FORECAST_CODE);
			
			onCreate(_db);
		}
		
	}

}
