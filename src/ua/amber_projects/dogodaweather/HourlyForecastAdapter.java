package ua.amber_projects.dogodaweather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HourlyForecastAdapter extends BaseAdapter {
	
	Context ctx;
	LayoutInflater layoutInflater;
	ArrayList<WeatherConditions> hourlyForecast;
	
	HourlyForecastAdapter(Context context, WeatherConditions[] weatherConditions) {
		
		this.ctx = context;
		this.hourlyForecast = new ArrayList<WeatherConditions>(Arrays.asList(weatherConditions));
		
	    this.layoutInflater = (LayoutInflater) ctx
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);		  
		
	}
	
	
	@Override
	public int getCount() {
		
		return hourlyForecast.size();
	}
	@Override
	public Object getItem(int position) {
		
		return hourlyForecast.get(position);
	}
	@Override
	public long getItemId(int position) {
		
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		
		if (view == null) {
			view = layoutInflater.inflate(R.layout.item1, parent, false);
		}
		
		WeatherConditions hourConds = getHourlyWeather(position);
		
		Calendar calendarDate = Calendar.getInstance();
	    calendarDate.setTimeInMillis(hourConds.getDT());
	    
	    
	    SimpleDateFormat ft_hour =
	    		new SimpleDateFormat ("HH:mm dd.MM");
		
		
		   
	    ft_hour.setTimeZone(TimeZone.getDefault());
	    String hour_of_day = ft_hour.format(calendarDate.getTime());
	    
	    
	    ((TextView) view.findViewById(R.id.tvDayTemp)).setText(hourConds.getTempMax(Degree.Celsius) + "°");
	    ((TextView) view.findViewById(R.id.tvNightTemp)).setText("");
	    ((TextView) view.findViewById(R.id.tvDate)).setText(hour_of_day);
	    
	    
	    WeatherCode[] weatherCodes = hourConds.getWeatherConditions().getWeatherCodes();
	    
	    int imageResourceId = CurrentWeatherActivity.getIconID(weatherCodes[0]);
	    ((ImageView) view.findViewById(R.id.ivDailyWeather)).setImageResource(imageResourceId);
	    
	    String packageName = ctx.getPackageName();
	    
	    int resId = ctx.getResources().getIdentifier("id_meaning_" + weatherCodes[0].getId(), "string", packageName);
	    	    
	    
    	//tvTodayPrecForecast.setText(descriptionTodayResID);
	    
	    ((TextView) view.findViewById(R.id.tvWeatherDescription)).setText(resId);
		
		
		
		
		return view;
	}
	
	WeatherConditions getHourlyWeather(int position) {
		return ((WeatherConditions) getItem(position));
	}
	
	

}
