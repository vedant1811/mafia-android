package com.vedant.mafia;

import java.util.Iterator;

import android.app.Application;

import com.vedant.mafia.backend.GameRules.GameEnd;

public abstract class AbstractConnection {
	
	Application hostApp;
	Players players;

	public AbstractConnection(Application app, int numPlayers) {
		hostApp = app;
		if (supportsMultipleUsers()) {
			players= new Players(numPlayers);
			createNextLogin();
		}
	}
	
	protected class Players implements Iterator<String>{
		int playersLeft;
		Players(int numPlayers){
			playersLeft = numPlayers;
		}
		
		void resetIterator(){
			playersLeft = GameController.db().names.size();
		}
		
		@Override
		public boolean hasNext() {
			if(playersLeft > 0)
				return true;
			else
				return false;
		}

		@Override
		public String next() {
			return GameController.db().names.get(
					GameController.db().names.size() - playersLeft--);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("not yet implemented");
		}
	}

	public void newLoginCreated(String name, int pin) {
		players.next();
		if (players.hasNext())
			createNextLogin();
		else
			GameController.setupComplete();
	}
	
	protected void getNextAction(){
		if(players.hasNext())
			getActionFrom(players.next());
		else
			GameController.roundEnds();
	}
	
	public void newRound(){
		players.resetIterator();
		getActionFrom(players.next());
	}
	
	public void actionPerformedBy(String from){
		getNextAction();
	}

	protected abstract void createNextLogin();

	protected abstract void getActionFrom(String name);

	public abstract void endRoundMessage(Message message);

	public abstract boolean supportsMultipleUsers();

	public abstract void cityRisesTo(String deathOf);

	public abstract void GameEnds(GameEnd result);
}
