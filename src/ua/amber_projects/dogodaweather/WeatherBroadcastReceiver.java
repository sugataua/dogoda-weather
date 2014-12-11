package ua.amber_projects.dogodaweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WeatherBroadcastReceiver extends BroadcastReceiver {
	
	
	final String LOG_TAG = "WeatherBR";

	@Override
	public void onReceive(Context context, Intent intent) {
				
		Log.d(LOG_TAG, "onReceive " + intent.getAction());
		context.startService(new Intent(context, UpdateWeatherService.class));		

	}

}
