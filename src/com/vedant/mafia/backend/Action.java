package com.vedant.mafia.backend;

public enum Action {
	NOTHING,
	KILL,
	SUGGEST,
	SAVE,
	SLEEP,
	CHECK;

	public String actionStatement() {
		switch (this) {
		case NOTHING:
			return "No action to perfrom. Click the button below to continue";
		case KILL:
			return "Who do want to kill?";
		case SUGGEST:
			return "You can suggest a person to the hitman to be killed";
		case SAVE:
			return "Who do you want to save?";
		case SLEEP:
			return "Who do want to put to sleep?";
		case CHECK:
			return "Who do want to check?";
		default:
			throw new IllegalArgumentException("no statement for action "
					+this.toString());
		}
	}
}
