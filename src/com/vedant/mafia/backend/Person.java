package com.vedant.mafia.backend;

public enum Person {
	CIVILIAN(Action.NOTHING),
	MAFIA(Action.SUGGEST),
	GODFATHER(Action.KILL),
	HITMAN(Action.KILL),
	POLICE(Action.CHECK),
	WITCH(Action.SLEEP),
	ANGEL(Action.SAVE);
	
	public final Action action;
	
	private Person(Action action){
		this.action = action;
	}
	
	public boolean isMafia(){
		switch (this){
		case MAFIA:
		case GODFATHER:
		case HITMAN:
			return true;
		default:
			return false;
		}
	}
	
	public boolean thumbsUp(){
		switch (this){
		case MAFIA:
		case HITMAN:
			return true;
		default:
			return false;
		}
	}
}
