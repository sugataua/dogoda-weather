package ua.amber_projects.dogodaweather;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class PrefActivity extends PreferenceActivity {
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.pref);
	    
	    
	    
	    ListPreference listPreference = (ListPreference) findPreference("city_id");
	        
	    
	    listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

	        public boolean onPreferenceChange(Preference preference, Object newValue) {
	        	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	        	ListPreference listCity = (ListPreference) preference;
	        	//CharSequence currText = listCity.getEntry();
	        	
	        	String textValue = newValue.toString();	        	
	        	int index = listCity.findIndexOfValue(textValue);

	            CharSequence[] entries = listCity.getEntries();

	            if (index >= 0) {
	            	Log.d("Pref", entries[index].toString());
	        	    Editor ed = sp.edit();
	        	    ed.putString(GetForecastTask.TAG_CITY, entries[index].toString());
	        	    ed.commit();
	            }
	            return true;
	        }
	    });
	    
	  }
	

}
