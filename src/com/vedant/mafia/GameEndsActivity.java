package com.vedant.mafia;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameEndsActivity extends Activity {
	public static final String WINNER = "winner", BUTTON_COLOR = "buttonColor";
	
	private static final String GAME_OVER = "Game is Over.\n";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_ends);
		((TextView)findViewById(R.id.gameEndsText)).setText(
			GAME_OVER + getIntent().getStringExtra(WINNER));
		((Button)findViewById(R.id.voteOutButton)).setBackgroundColor(
				getIntent().getIntExtra(BUTTON_COLOR, Color.LTGRAY));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_ends, menu);
		return false;
	}
	
	public void anotherGameClicked(View v){
		startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
	}

}
