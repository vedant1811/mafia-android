package com.vedant.mafia;

import com.vedant.mafia.backend.*;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class PerformActionActivity extends Activity {
	
	private TextView heading, extraInfo, actionStatement;
	private Spinner dataSpinner;
	private Button performActionButton;
	private String playerName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perform_action);
		heading = (TextView) findViewById(R.id.performActionHeading);
		extraInfo = (TextView) findViewById(R.id.performInfoExtraInfo);
		actionStatement = (TextView) findViewById(R.id.performActionStatement);
		dataSpinner = (Spinner) findViewById(R.id.performActionSpinner);
		performActionButton = (Button) findViewById(R.id.performActionButton);
		
		playerName = getIntent().getStringExtra(GameController.PLAYER_NAME);
		Person person = GameController.db().getRole(playerName);
		heading.append(person.toString());
		String info = MessageCentre.getInfoFor(playerName);
		if (info != null && !info.equals(""))
			extraInfo.setText(info);
		else 
			extraInfo.setVisibility(View.INVISIBLE);
		actionStatement.setText(person.action.actionStatement());
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, MessageCentre.getListFor(playerName));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataSpinner.setAdapter(adapter);
		
		performActionButton.setText(person.action.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.perform_action, menu);
		return false;
	}
	
	public void actionButtonPressed(View v){
		String to;
		if(dataSpinner.getVisibility() == View.VISIBLE){
			to = (String) dataSpinner.getSelectedItem();
			if(to == null)
				to = Message.NO_ONE;
		}else to = Message.NA;
		GameController.actionButtonPressed(new Message(playerName, to));
	}

}
