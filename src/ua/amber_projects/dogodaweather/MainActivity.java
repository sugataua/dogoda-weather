package ua.amber_projects.dogodaweather;


import java.net.InetAddress;
import java.util.Calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	TextView tvTemp;
	//TextView tvTempMinMax;
	TextView tvCityName;
	TextView tvHumidity;
	TextView tvPressure;
	TextView tvWeatherDescription;
	ImageView ivWeatherCondition;
	
	TextView tvTodayTempForecast;
	TextView tvTodayPrecForecast;
	ImageView ivTodayWeatherCondition;
	
	TextView tvTomorrowTempForecast;
	TextView tvTomorrowPrecForecast;
	ImageView ivTomorrowWeatherCondition;
	
	final int REQUEST_CODE_CITY_CHANGE = 1;
	
	public final static String PARAM_PINTENT = "pendingIntent";
	public final static String PARAM_RESULT = "result";
	
	public final static int STATUS_UPDATED = 200;
	
	
	public final static String BROADCAST_ACTION = "ua.amber_projects.dogodaweather";
	
	
	String city_ID; // = GetForecastTask.KIEV_ID;
	String city_Name;
	
	SharedPreferences sp;
	
	BroadcastReceiver br;
	
	
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the layout for this activity
		setContentView(R.layout.new_main_activity);
		
		
    	tvTemp = (TextView) findViewById(R.id.tvTemp);
    	tvCityName = (TextView) findViewById(R.id.tvCityName);
    	
    	tvHumidity = (TextView) findViewById(R.id.tvHumidity);
    	tvPressure = (TextView) findViewById(R.id.tvPressure);
    	tvWeatherDescription = (TextView) findViewById(R.id.tvWeatherDescription);
    	ivWeatherCondition = (ImageView) findViewById(R.id.ivWeatherCondition);
    	
    	tvTodayTempForecast = (TextView) findViewById(R.id.tvTodayTempForecast);
    	tvTodayPrecForecast = (TextView) findViewById(R.id.tvTodayPrecForecast);
    	
    	ivTodayWeatherCondition = (ImageView) findViewById (R.id.ivTodayWeatherCondition);
    	
    	tvTomorrowTempForecast = (TextView) findViewById(R.id.tvTonightTempForecast);
    	tvTomorrowPrecForecast = (TextView) findViewById(R.id.tvTonightPrecForecast);
    	ivTomorrowWeatherCondition = (ImageView) findViewById (R.id.ivTonightWeatherCondition);
    	
    	
		
    	sp = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	    	
    	city_ID = sp.getString("city_id", GetForecastTask.KIEV_ID);
		
    	if (UpdateWeatherService.isRunning) {
			stopService(new Intent(this, UpdateWeatherService.class));				
		}
		startService(new Intent(this, UpdateWeatherService.class));	
    	
    	   br = new BroadcastReceiver() {
    		      // действи€ при получении сообщений
    		   
    		   	  @Override
    		      public void onReceive(Context context, Intent intent) {
    		   		  
    		   		  Log.d("BroadcastR", "Received");
    		   		  String city_ID = sp.getString("city_id", GetForecastTask.KIEV_ID);
    		   		  refreshWeatherOnScreen(city_ID);
    		        
//    		        int status = intent.getIntExtra("asasa", 0);
//    		        //Log.d("BR", "onReceive: task = " + task + ", status = " + status);
//    		        
//   		        
//    		        // Ћовим сообщени€ об окончании задач
//    		        if (status == STATUS_UPDATED) {
//
//    		          }
    		        }
    		      };
    		   
    	   
    	    // создаем фильтр дл€ BroadcastReceiver
    	    IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
    	    // регистрируем (включаем) BroadcastReceiver
    	    registerReceiver(br, intFilt);
    	

    	    
    	
		
		
	}
	
	@Override
	protected void onResume() {
		
		
		//stopService(new Intent(this, UpdateWeatherService.class));
		
		String new_city_ID = sp.getString("city_id", GetForecastTask.KIEV_ID);
		
		if (! city_ID.equals(new_city_ID)) {			
			city_ID = new_city_ID;
			
			if (UpdateWeatherService.isRunning) {
				stopService(new Intent(this, UpdateWeatherService.class));				
			}
			startService(new Intent(this, UpdateWeatherService.class));						
		}
		
		refreshWeatherOnScreen(city_ID);
		
		//new UpdateTask().execute();

		super.onResume();	
		
	}
	
	
	@Override
	protected void onDestroy() {
		//stopService(new Intent(this, UpdateWeatherService.class));		
		super.onDestroy();
		unregisterReceiver(br);
	}
	
	
	
	private void refreshWeatherOnScreen(String city_ID) {
		
		
		int numberCityID = Integer.parseInt(city_ID);
		WeatherDBAdapter tempWDBAdapter;
		tempWDBAdapter = new WeatherDBAdapter(MainActivity.this);
		
		
		tempWDBAdapter.open();
		
		Calendar calendarNow = Calendar.getInstance();
		Calendar calendarToday = Calendar.getInstance();
		
		calendarToday.set(Calendar.HOUR_OF_DAY, 0);
		calendarToday.set(Calendar.MINUTE, 0);
		calendarToday.set(Calendar.SECOND, 0);
		calendarToday.set(Calendar.MILLISECOND, 0);
		
		long currentTimeMillis = calendarNow.getTimeInMillis();
		
		//Log.d("Time", "now: " + currentTimeMillis);
		long currentTimeMinus3H = currentTimeMillis - (1000 * 60 * 60 * 3);		 
		//Log.d("Time", "3 hours before: " + currentTimeMinus3H);
		//Log.d("Time", "This day: " + calendarToday.getTimeInMillis());
		
				
		CurrentWeatherConditions cWC = tempWDBAdapter.getLastCurrentWeather(numberCityID, currentTimeMinus3H);
		DailyForecast dF = tempWDBAdapter.getDailyForecast(numberCityID, calendarToday.getTimeInMillis());
		
		//tempWDBAdapter.cleanAllTables();
		
		tempWDBAdapter.close();
		
		if (cWC != null) {
			showCurrentWeather(cWC);			
		} else {
			showEmptyCurrent();
		}
		
		if (dF != null) {
			showTomorrowWeather(dF);
		} else {
			showEmptyTomorrow();
		}
		
	}
	
	private void showCurrentWeather(CurrentWeatherConditions _cwcData) {
		
		if (_cwcData != null) {
			
			String temperatureUnit = sp.getString("units_temp", "Celsius");
			Degree prefDegree = Degree.valueOf(temperatureUnit);
			
			String pressureUnit = sp.getString("units_pressure", "mmHg");
			PressureUnit prefPressureUnit = PressureUnit.valueOf(pressureUnit);			
			
			
	    	City city = _cwcData.getCity();
	    	
	    	tvCityName.setText(city.getCityName() + ", " + city.getCountry());
	    	
	    	int temp = (int) Math.round(_cwcData.getTemp(prefDegree) );
	    	
	    	tvTemp.setText(temp + "∞");
	    	
	    	//tvTempMinMax.setText(_cwcData.getTempMin(Degree.Celsius) + "∞  Ч  " + _cwcData.getTempMax(Degree.Celsius)+ "∞");
	    	tvHumidity.setText(getResources().getString(R.string.humidity) + ": " + _cwcData.getHumidity() + "%");
	    	tvPressure.setText(getResources().getString(R.string.pressure) + ": " + _cwcData.getPressure(prefPressureUnit) + " " + pressureUnit);
	    	
	    	WeatherCode wCode = _cwcData.getWeatherConditions().getWeatherCodes()[0];
	    		        	
	    	int imageResourceId = getIconID(wCode);
	    	
	    	ivWeatherCondition.setImageResource(imageResourceId);
	    	
	    	
	    	
	    	//tvWeatherDescription.setText(wCode.getDescription());	
	    	
	    	String packageName = getPackageName();
	    	int descriptionResID = getStringResourceByName(packageName,"id_meaning_" + wCode.getId());
	    	tvWeatherDescription.setText(descriptionResID);
	    	
	    	
	    	
		} else {
			tvTemp.setText(R.string.not_available);    			
			tvCityName.setText(R.string.not_available);
			tvHumidity.setText(R.string.not_available);
			tvPressure.setText(R.string.not_available);
			tvWeatherDescription.setText(R.string.not_available);    			   			
			tvTodayTempForecast.setText(R.string.not_available);
			tvTodayPrecForecast.setText(R.string.not_available);    			    			
			tvTomorrowTempForecast.setText(R.string.not_available);
			tvTomorrowPrecForecast.setText(R.string.not_available);
		}
						
	}
	
	private void showTomorrowWeather(DailyForecast _dwfData) {
		DailyWeatherConditions[] dwc;
		
		if (_dwfData != null) {
			dwc = _dwfData.getDailyWeather();
			//dwc[0].get
			if (dwc.length >= 2) {
				if (dwc[1] != null)
				{
					String tempValues = "" + dwc[0].getTempDayMin(Degree.Celsius)
							+ "∞ Ч " + dwc[0].getTempDayMax(Degree.Celsius) + "∞";
					tvTodayTempForecast.setText(tempValues);
					String tempValues2 = "" + dwc[1].getTempDayMin(Degree.Celsius)
							+ "∞ Ч " + dwc[1].getTempDayMax(Degree.Celsius) + "∞";
					tvTomorrowTempForecast.setText(tempValues2);
					
					WeatherCode wCode =  dwc[0].getWeatherConditions().getWeatherCodes()[0];
					WeatherCode wCodeTomorrow =  dwc[1].getWeatherConditions().getWeatherCodes()[0];
								
			    	int imageResourceId = getIconID(wCode);
			    	int imageResourceIdTomorow = getIconID(wCodeTomorrow);
			    	
			    	ivTodayWeatherCondition.setImageResource(imageResourceId);
			    	ivTomorrowWeatherCondition.setImageResource(imageResourceIdTomorow);
			    	
			    	String packageName = getPackageName();
			    	int descriptionTodayResID = getStringResourceByName(packageName,"id_meaning_" + wCode.getId());
			    	int descriptionTomorrowResID = getStringResourceByName(packageName,"id_meaning_" + wCodeTomorrow.getId());
			    	
			    	
			    	tvTodayPrecForecast.setText(descriptionTodayResID);
			    	tvTomorrowPrecForecast.setText(descriptionTomorrowResID);				
				}				
			} else {
				String tempValues = "" + dwc[0].getTempDayMin(Degree.Celsius)
						+ "∞ Ч " + dwc[0].getTempDayMax(Degree.Celsius) + "∞";
				tvTodayTempForecast.setText(tempValues);	
				
				WeatherCode wCode =  dwc[0].getWeatherConditions().getWeatherCodes()[0];
				int imageResourceId = getIconID(wCode);
				ivTodayWeatherCondition.setImageResource(imageResourceId);
				String packageName = getPackageName();
		    	int descriptionTodayResID = getStringResourceByName(packageName,"id_meaning_" + wCode.getId());
		    	tvTodayPrecForecast.setText(descriptionTodayResID);
			}
			
		
		}
		
	}
	
	private void showEmptyCurrent() {
		
		tvCityName.setText(sp.getString(GetForecastTask.TAG_CITY, "Unknown"));
	
		tvTemp.setText(R.string.not_available);   
		ivWeatherCondition.setImageResource(R.drawable.na1);
	
		tvHumidity.setText(R.string.empty);
		tvPressure.setText(R.string.empty);
		tvWeatherDescription.setText(R.string.empty);    	
		
	}
	
	private void showEmptyTomorrow() {
		   			   			
		tvTodayTempForecast.setText(R.string.not_available);
		tvTodayPrecForecast.setText(R.string.empty);    			    			
		tvTomorrowTempForecast.setText(R.string.not_available);
		tvTomorrowPrecForecast.setText(R.string.empty);
				   	
    	ivTodayWeatherCondition.setImageResource(android.R.color.transparent);

    	ivTomorrowWeatherCondition.setImageResource(android.R.color.transparent);
		
	}
	
	private class UpdateTask extends GetForecastTask {
		
		
		WeatherDBAdapter wDBAdapter;
		
		//ListView lvMain;

		
		private ProgressDialog dialog;
		
		
		public UpdateTask() {
			super(city_ID);
			dialog = new ProgressDialog(MainActivity.this);
			
		}
		
		@Override
	    protected void onPreExecute() {
	    	super.onPreExecute();
	    	
	    	
	    	wDBAdapter = new WeatherDBAdapter(MainActivity.this);
	    	wDBAdapter.open();
	    	
	    	//lvMain = (ListView) findViewById(R.id.listVewDaileForecast);
	
	        dialog.setMessage("Updating weather, please wait.");
	        dialog.show();
	    	

	    	
	    }
		
		
	    @Override
	    protected void onPostExecute(Void result) {
	        super.onPostExecute(result);
	        
//	        // создаем адаптер
//	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
//	            android.R.layout.simple_list_item_1, super.forecastStr);
//
//	        // присваиваем адаптер списку
//	        lvMain.setAdapter(adapter);
	        
			
			Calendar calendarToday = Calendar.getInstance();
			
			calendarToday.set(Calendar.HOUR_OF_DAY, 0);
			calendarToday.set(Calendar.MINUTE, 0);
			calendarToday.set(Calendar.SECOND, 0);
			calendarToday.set(Calendar.MILLISECOND, 0);
			
			
			long currentTimeMillis = calendarToday.getTimeInMillis();
			
			
			long currentTimeMinusDay = currentTimeMillis - (1000 * 60 * 60 * 24);		 
			
	        wDBAdapter.deleteOldWeatherConditions(currentTimeMinusDay);
	        wDBAdapter.deleteOldWeatherForecast(currentTimeMinusDay);
	        
	        if (cwcData != null) {
	        	
	        	showCurrentWeather(cwcData);
	        	wDBAdapter.insertOrUpdateCurrentWeather(cwcData);
	        }
	        
	        if (dfData != null) {
	        	showTomorrowWeather(dfData);
	        	wDBAdapter.insertOrUpdateDailyForecast(dfData);
	        }
	        	
	        wDBAdapter.close();
	        
	        if (dialog.isShowing()) {
	            dialog.dismiss();
	        }
	        
	        
	        	        
	        
	    }
		
	}
	
	public static int getIconID(WeatherCode _weatherCode) {
		int code = _weatherCode.getId();
		
		switch(code) {
			case 800:
				return R.drawable.sunny;				
			case 801:
				return R.drawable.few_clouds;
			case 802:
				return R.drawable.scattered_clouds;
			case 803:
				return R.drawable.overcast;
			case 804:
				return R.drawable.overcast;
			default:
				code = code / 100;
				switch (code) {
					case 2:
						return R.drawable.thunder;
					case 3:
						return R.drawable.showers;
					case 5:
						return R.drawable.heavy_rain;
					case 6:
						return R.drawable.snowy;
					case 7:
						return R.drawable.fog;
					default:
						return R.drawable.sunny;
				}
				
		}
		
						
	}
	
	public void updateWeatherClick(View view) {
		
		if (isServerAvailable()) {
			new UpdateTask().execute();			
		} else {
			Toast.makeText(this,  getResources().getString(R.string.connection_impossible), Toast.LENGTH_LONG).show();			
		}
		
		
	}
	
	public void changeCityClick(View view) {
		
		
		Intent intentCC = new Intent(MainActivity.this, CityChoise.class);
	    //startActivity(intentCC);
		startActivityForResult(intentCC, REQUEST_CODE_CITY_CHANGE);
		
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuItem mi = menu.add(1,1,0,getResources().getString(R.string.preferences));
		//MenuItem mi2 = menu.add(2,2,1,"Daily forecast");		
		mi.setIntent(new Intent(this, PrefActivity.class));
		//mi2.setIntent(new Intent(this, MainTabActivity.class));
		return super.onCreateOptionsMenu(menu);
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.d("MainAct", "requestCode = " + requestCode + ", resultCode = " + resultCode);
    	if (resultCode == RESULT_OK) {
    		switch (requestCode) {
    		case REQUEST_CODE_CITY_CHANGE:
    			String cityId = data.getStringExtra("city_id");
    			Log.d("Catch", cityId);
    			city_ID = cityId;
    			//sPref = getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
    			//Editor ed = sPref.edit();
    			//ed.putString(CITY_CODE, cityId);
    			//ed.putBoolean(UPDATE_REQUIRED, true);
    			
    			//ed.commit();
    			//sPref
    			
    			tvCityName.setText(data.getStringExtra("city_name"));
    			//not_available
    			tvTemp.setText(R.string.not_available);    			
    			//tvCityName.setText(R.string.not_available);
    			tvHumidity.setText(R.string.empty);
    			tvPressure.setText(R.string.empty);
    			tvWeatherDescription.setText(R.string.empty);    			   			
    			tvTodayTempForecast.setText(R.string.not_available);
    			tvTodayPrecForecast.setText(R.string.empty);    			    			
    			tvTomorrowTempForecast.setText(R.string.not_available);
    			tvTomorrowPrecForecast.setText(R.string.empty);
    			
    			ivWeatherCondition.setImageResource(R.drawable.na1);
   	    	
    	    	ivTodayWeatherCondition.setImageResource(android.R.color.transparent);
    	
    	    	ivTomorrowWeatherCondition.setImageResource(android.R.color.transparent);
    			
    			
    			break;
    		
    		}
    	
    	} else {
    		Toast.makeText(this, "No changes!", Toast.LENGTH_SHORT).show();    		
    	}
    }
	
    
    public static boolean isServerAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("api.openweathermap.org"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }
    
    private int getStringResourceByName(String packageName, String aString) {
        //String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return resId;
      }
	
	
	
	
	
	

}
