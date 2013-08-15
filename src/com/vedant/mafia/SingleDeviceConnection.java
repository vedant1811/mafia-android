package com.vedant.mafia;

import java.util.Random;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vedant.mafia.backend.GameRules.GameEnd;
import com.vedant.mafia.backend.Person;
import com.vedant.mafia.backend.ReplyFromGod;

public class SingleDeviceConnection extends AbstractConnection {
	
	private class PoliceReplyHandler{
		private int[] colors;
		int replyColor;
		PoliceReplyHandler() {
			Random random = new Random();
			colors = new int[3];
			colors[random.nextInt(3)] = Color.GREEN;
			colors[random.nextInt(3)] = Color.WHITE;
			colors[random.nextInt(3)] = Color.YELLOW;
			createDialog();
		}
		void setReply(ReplyFromGod reply){
			replyColor = getColor(reply);
		}
		int getColor(ReplyFromGod reply){
			switch(reply){
			case CANT_SAY:
				return colors[0];
			case YES:
				return colors[1];
			case NO:
				return colors[2];
			}
			throw new IllegalArgumentException(
					"Cannot convert " + reply.toString() + "to color");
		}
		void createDialog(){
			View dialogView = LayoutInflater.from(hostApp).inflate(
									R.layout.police_info_dialog, null);
			Button[] dialogButtons = {
					(Button)dialogView.findViewById(R.id.polieInfoButton1),
					(Button)dialogView.findViewById(R.id.polieInfoButton2),
					(Button)dialogView.findViewById(R.id.polieInfoButton3)	};
			ReplyFromGod[] replies = ReplyFromGod.values();
			for(int i=0; i < 3; i++){
				dialogButtons[i].setText(replies[i].toString());
				dialogButtons[i].setBackgroundColor(getColor(replies[i]));
			}
			new AlertDialog.Builder(hostApp)
				.setView(dialogView)
				.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						actionPerformedBy(Message.NA);
					}
				})
				.setPositiveButton("OK", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						actionPerformedBy(Message.NA);
					}
				})
				.show();
		}
	}

	private PoliceReplyHandler policeReplyHandler;
	
	public SingleDeviceConnection(Application hostApp, int numPlayers) {
		super(hostApp, numPlayers);
	}
	
	@Override
	public boolean supportsMultipleUsers() {
		return true;
	}

	@Override
	public void endRoundMessage(Message message) {
		policeReplyHandler.setReply(ReplyFromGod.values()[Integer.parseInt(message.message)]);
	}

	@Override
	protected void getActionFrom(String name) {
		Toast.makeText(hostApp, "Pass this device to " + name,
				Toast.LENGTH_LONG).show();
		hostApp.startActivity(new Intent(hostApp, NextPersonActivity.class).
				putExtra(GameController.PLAYER_NAME, name) );
	}

	@Override
	protected void createNextLogin() {
		Toast.makeText(hostApp, "Pass this device to the next person",
				Toast.LENGTH_LONG).show();
		hostApp.startActivity(new Intent(hostApp, NextPersonActivity.class) );
	}

	@Override
	public void cityRisesTo(String deathOf) {
		hostApp.startActivity(new Intent(hostApp,
			RoundEndsActivity.class).putExtra(GameController.PLAYER_NAME, deathOf) );
	}
	
	@Override
	public void actionPerformedBy(String from){
		if(GameController.db().getRole(from).equals(Person.POLICE)){
			policeReplyHandler = new PoliceReplyHandler();
		}
		else
			super.actionPerformedBy(from);
	}

	@Override
	public void GameEnds(GameEnd result) {
		hostApp.startActivity(new Intent(hostApp, GameEndsActivity.class)
			.putExtra(GameEndsActivity.WINNER, result.toString())
			.putExtra(GameEndsActivity.BUTTON_COLOR, policeReplyHandler.replyColor));
	}

}
