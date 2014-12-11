package ua.amber_projects.dogodaweather;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;




public class CityChoise extends Activity implements OnClickListener {
	
	
	final String LOG_TAG = "CCLogs";
	private EditText filterText = null;
	private ArrayAdapter<CharSequence> adapter = null;
	
	  ListView lvMain;
	  String[] names;
	  String[] codes;
	
	  /** Called when the activity is first created. */
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_city_choice);

	    lvMain = (ListView) findViewById(R.id.ListViewCC);
	    
	    // Prepare to listenig changes in filter field
	    filterText = (EditText) findViewById(R.id.list_search_box);
	    filterText.addTextChangedListener(filterTextWatcher);
	    // устанавливаем режим выбора пунктов списка 
	    lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	    // Создаем адаптер, используя массив из файла ресурсов
	    adapter = ArrayAdapter.createFromResource(
	        this, R.array.city_names,
	        android.R.layout.simple_list_item_single_choice);
	    
	    lvMain.setTextFilterEnabled(true);
	    lvMain.setAdapter(adapter);
	    
	    Button btnChecked = (Button) findViewById(R.id.btnChecked);
	    btnChecked.setOnClickListener(this);

	    // получаем массив из файла ресурсов
	    codes = getResources().getStringArray(R.array.city_code);
	    names = getResources().getStringArray(R.array.city_names);
	    
	  }

	  public void onClick(View arg0) {
		  Intent intent = new Intent();
		  
		  // TODO: Заглушка! Не работает с двумя городами имеющими одинаковые названия
		  int posC = getPositionOfValue(names, (String) adapter.getItem(lvMain.getCheckedItemPosition()));
		  

		  intent.putExtra("city_id", codes[posC]);
		  intent.putExtra("city_name", names[posC]);
		  // пишем в лог выделенный элемент
		  Log.d(LOG_TAG, "checked: " + names[posC] + "code: " + codes[posC]);
		  setResult(RESULT_OK, intent);
		  finish();
	  }
	  
	  
	  private TextWatcher filterTextWatcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
		    }

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
		            int after) {
		    }

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
		            int count) {
		        adapter.getFilter().filter(s);
		    }
			
	  };
	  
	  
	  
	  @Override
		protected void onDestroy() {
		    super.onDestroy();
		    filterText.removeTextChangedListener(filterTextWatcher);
	  }
	
	  
	  private int getPositionOfValue(String[] array, String value) {
		  int position = -1;
		  
		  for (int i = 0; i < array.length; i++) {
			  if (array[i].equals(value)) {
				  position = i;
				  break;
			  }
		  }
		  
		  return position; 
	  }
	  
	  
	  public void backClick(View view) {
		  this.finish();
	  }
	  
	 

	/*private EditText filterText = null;
	ArrayAdapter<String> adapter = null;
	private ListView lv1;
	
	private String lv_arr[] =
		{ "Kiev", "Moscow", "London", "Vasyuki" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    setContentView(R.layout.activity_city_choice);

	    filterText = (EditText) findViewById(R.id.list_search_box);
	    lv1 = (ListView)findViewById(R.id.ListViewCC);
	    filterText.addTextChangedListener(filterTextWatcher);
	    
	    adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , lv_arr);
	    lv1.setTextFilterEnabled(true);
	    lv1.setAdapter(adapter);
	    
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
	    }

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
	            int after) {
	    }

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
	            int count) {
	        adapter.getFilter().filter(s);
	    }



	};

	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    filterText.removeTextChangedListener(filterTextWatcher);
	}*/

}
