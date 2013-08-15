package com.vedant.mafia;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NextPersonActivity extends Activity {
	
	private EditText pinEditText, nameEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next_person);
		
		nameEditText = (EditText)findViewById(R.id.nameEditText);
		pinEditText = (EditText)findViewById(R.id.pinEditText);
		String playerName = getIntent().getStringExtra(GameController.PLAYER_NAME);
		if(playerName != null){
			nameEditText.setText(playerName);
			nameEditText.setEnabled(false);
			((Button)findViewById(R.id.loginButton)).setText("Login");
			((TextView)findViewById(R.id.enterCredentialsText)).setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.next_person, menu);
		return false;
	}
	
	public void loginButtonPressed(View v){
		int pin;
		try {
			String pinString = pinEditText.getText().toString();
			if (pinString.length() != 4) {
				throw new IllegalArgumentException();
			}
			pin = Integer.parseInt(pinString);
		} catch (IllegalArgumentException e) {
			pinEditText.setText("");
			pinEditText.requestFocus();
			Toast.makeText(this, "Illegal PIN! Enter a 4 digit number",
					Toast.LENGTH_SHORT).show();
			return;
		}
		String name = nameEditText.getText().toString();
		if(nameEditText.isEnabled()){
			if(GameController.db().names.contains(name)){
				Toast.makeText(this, "Name Exists. Please enter another name",
						Toast.LENGTH_SHORT).show();
				nameEditText.setText("");
				nameEditText.requestFocus();
			}else
				GameController.createLogin(name, pin);
		}else{
			if(!GameController.db().check(name, pin) ){
				Toast.makeText(this, "incorrect PIN. retry",
						Toast.LENGTH_SHORT).show();
				pinEditText.setText("");
				pinEditText.requestFocus();
			}else{
				startActivity(new Intent(this, PerformActionActivity.class).
						putExtra(GameController.PLAYER_NAME, name) );
			}
		}
	}
}
