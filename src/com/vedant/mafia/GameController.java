package com.vedant.mafia;

import java.util.Collections;

import android.app.Application;

import com.vedant.mafia.backend.GameRules;
import com.vedant.mafia.backend.GameRules.GameEnd;
import com.vedant.mafia.backend.Person;

public class GameController {
	public static final String PLAYER_NAME = "name";

	private static GameController instance;

	public PlayersDb db;
	
	private Application curApp;
	private AbstractConnection connection;

	static void initialize(Application currentApp) {
		instance = new GameController(currentApp);
	}

	static void beginSetup(int[] personList) {
		int numPlayers = totalOfPersonList(personList);
		instance.db = new PlayersDb(numPlayers);
		for (int j = 0; j < personList.length; j++) {
			while (personList[j]-- > 0)
				instance.db.types.add(Person.values()[j]);
		}
		instance.connection = new SingleDeviceConnection(instance.curApp, numPlayers);
	}

	static void createLogin(String name, int pin) {
		instance.connection.newLoginCreated(name, pin);
	}

	public static void setupComplete() {
		Collections.shuffle(instance.db.types);
		instance.newRound();
	}
	
	private void newRound() {
		connection.newRound();
		MessageCentre.newRound();
	}

	public static void roundEnds() {
		String deathOf = MessageCentre.cityRisesTo();
		if (deathOf != null) {
			db().remove(deathOf);
		}
		instance.connection.cityRisesTo(deathOf);
	}
	
	public static void actionButtonPressed(Message message){
		Message processedMessage = GameRules.processMessage(message);
		if(processedMessage != null)
			MessageCentre.newProcessedMessage(processedMessage);
		instance.connection.actionPerformedBy(message.from);
	}

	static int totalOfPersonList(int[] personList) {
		int sum = 0;
		for (int i : personList)
			sum += i;
		return sum;
	}

	public static PlayersDb db() {
		return instance.db;
	}

	public static void votedOut(String votedOut) {
		GameEnd result = GameRules.doesGameEnd(votedOut);
		if(result.equals(GameEnd.GAME_DOES_NOT_END))
			instance.newRound();
		else
			instance.connection.GameEnds(result);
	}
	
	public static void roundEndMessage(Message message){
		instance.connection.endRoundMessage(message);
	}

	private GameController(Application currentApp) {
		curApp = currentApp;
	}
}
