package com.vedant.mafia;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vedant.mafia.backend.Person;

public class HostActivity extends Activity {
	ListView charListView;
	int[] personList;
	Editable totalText;
	
	private class PersonAdapter extends ArrayAdapter<Person> {

		PersonAdapter(Context context, int resource, int textViewResourceId,
				Person[] objects) {
			super(context, resource, textViewResourceId, objects);
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent){
			View row = super.getView(position, convertView, parent);
			final EditText numInput = (EditText) row.findViewById(R.id.numInput);
			final Button plusButton = (Button) row.findViewById(R.id.plusButton),
							minusButton= (Button) row.findViewById(R.id.minusButton);
			
			plusButton.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					Editable curText = numInput.getText();
					int n;
					try {
						n = Integer.parseInt(curText.toString())+1;
					} catch (NumberFormatException e) {
						n = 1;
					}
					curText.replace(0, curText.length(), String.valueOf(n));
				}
			});
			
			minusButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Editable curText = numInput.getText();
					int n;
					try {
						n = Integer.parseInt(curText.toString())+1;
					} catch (NumberFormatException e) {
						n = 0;
					}
					if(n < 0)
						n = 0;
					curText.replace(0, curText.length(), String.valueOf(n));
				}
			});
			
			numInput.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					try {
						listItemChanged(position, Integer.parseInt(s.toString()));
					} catch (NumberFormatException e){
						s.replace(0, s.length(), "0");
					}
				}
			});
			return row;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_host);
		charListView = (ListView) findViewById(R.id.charactersListView);
		
		View footerView = LayoutInflater.from(getApplicationContext()).
				inflate(R.layout.list_view_footer, null, false);
		totalText = ((EditText)footerView.findViewById(R.id.total_text)).getText();
		charListView.addFooterView(footerView, totalText, false);
		PersonAdapter adapter = new PersonAdapter(this, R.layout.list_item,
				R.id.layoutText, Person.values());
		charListView.setAdapter(adapter);
		personList = new int[Person.values().length];
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.host, menu);
		return true;
	}
	
	public void startGameClicked(View v){
		GameController.initialize(getApplication());
		if(personList[Person.GODFATHER.ordinal()] != 1)
			Toast.makeText(getBaseContext(), "there can be only 1 Godfather",
					Toast.LENGTH_SHORT).show();
		else
			GameController.beginSetup(personList);
	}
	
	private void listItemChanged(int position, int newValue){
		personList[position] = newValue;
		totalText.clear();
		totalText.append(String.valueOf(GameController.totalOfPersonList(personList)));
	}

}
