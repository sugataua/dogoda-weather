package ua.amber_projects.dogodaweather;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DailyForecastAdapter extends BaseAdapter {
	
	Context ctx;
	LayoutInflater layoutInflater;
	DailyWeatherConditions[] dailyConditions;
	
	
	DailyForecastAdapter(Context context, DailyWeatherConditions[] _dailyConditions) {
	    
		ctx = context;	    
	    dailyConditions = _dailyConditions;
	    
	    layoutInflater = (LayoutInflater) ctx
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  }
	
	

	@Override
	public int getCount() {
		
		return dailyConditions.length;
	}

	@Override
	public Object getItem(int position) {
		
		return dailyConditions[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		
		View view = convertView;
	    if (view == null) {
	      view = layoutInflater.inflate(R.layout.item1, parent, false);
	    }
	    
	    DailyWeatherConditions dw = getDailyWeather(position);
	        
	    
	    Calendar calendarDate = Calendar.getInstance();
	    calendarDate.setTimeInMillis(dw.getDT());
	    
	    
	    SimpleDateFormat ft_time =
	    		new SimpleDateFormat ("EEEE");
		
		
		   
		   ft_time.setTimeZone(TimeZone.getDefault());
	       String day_of_week = ft_time.format(calendarDate.getTime());
	    
	    
	    ((TextView) view.findViewById(R.id.tvDayTemp)).setText(dw.getTempDay(Degree.Celsius) + "°");
	    ((TextView) view.findViewById(R.id.tvNightTemp)).setText(dw.getTempNight(Degree.Celsius) + "°");
	    ((TextView) view.findViewById(R.id.tvDate)).setText(day_of_week);
	    
	    
	    WeatherCode[] weatherCodes = dw.getWeatherConditions().getWeatherCodes();
	    
	    int imageResourceId = MainActivity.getIconID(weatherCodes[0]);
	    ((ImageView) view.findViewById(R.id.ivDailyWeather)).setImageResource(imageResourceId);
	    
	    String packageName = ctx.getPackageName();
	    
	    int resId = ctx.getResources().getIdentifier("id_meaning_" + weatherCodes[0].getId(), "string", packageName);
	    	    
	    
    	//tvTodayPrecForecast.setText(descriptionTodayResID);
	    
	    ((TextView) view.findViewById(R.id.tvWeatherDescription)).setText(resId);
	    
	    
  
		
		return view;
	}
	
	
	DailyWeatherConditions getDailyWeather(int position) {
		return ((DailyWeatherConditions) getItem(position));
	}
	
	
	

}
