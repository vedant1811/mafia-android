package com.vedant.mafia;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class RoundEndsActivity extends Activity {
	private final static String HEADING = "City rises to ";

	private Spinner dataSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_round_ends);
		String heading = getIntent().getStringExtra(GameController.PLAYER_NAME);
		if (heading == null)
			heading = HEADING + "no death";
		else
			heading = HEADING + "death of " + heading;
		((TextView) findViewById(R.id.roundEndsHeading)).setText(heading);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, GameController.db().names);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataSpinner.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.round_ends, menu);
		return false;
	}
	
	public void voteOutCLicked(View v){
		GameController.votedOut((String)dataSpinner.getSelectedItem());
	}

}
