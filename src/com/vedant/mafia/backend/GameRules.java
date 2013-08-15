package com.vedant.mafia.backend;

import java.util.ArrayList;
import java.util.Set;

import com.vedant.mafia.GameController;
import com.vedant.mafia.Message;
import com.vedant.mafia.MessageCentre;

public class GameRules {
	public enum GameEnd{
		GAME_DOES_NOT_END,
		MAFIA_WIN,
		NON_MAFIA_WIN
	}
	private static final String SUGGESTIONS_TO_HITMAN = " suggested to kill ",
		HITMAN_KILLED = " has already been killed. You may suggest a player for the next round";
	
	public static GameEnd doesGameEnd(String votedOut){
		Person roleVotedOut = GameController.db().getRole(votedOut);
		GameController.db().remove(votedOut);
		switch(roleVotedOut){
		case GODFATHER:
		case HITMAN:
			int index = GameController.db().types.indexOf(Person.MAFIA);
			if(index < 0){
				index = GameController.db().types.indexOf(Person.WITCH);
				if(index < 0)
					return GameEnd.NON_MAFIA_WIN;
			}
			GameController.db().types.remove(index);
			GameController.db().types.add(index, Person.HITMAN);
		case MAFIA:
		case WITCH:
			break;
		default: // for non mafia
			int numNonMafia = 0;
			for(Person person: GameController.db().types)
				if(!person.isMafia())
					numNonMafia++;
			switch(2*numNonMafia - GameController.db().types.size()){// ie difference between mafia and non mafia
			case 1:
				if(GameController.db().types.contains(Person.ANGEL))
					break;
			case 0:
				return GameEnd.MAFIA_WIN;
			}
		}
		return GameEnd.GAME_DOES_NOT_END;
	}
	
	public static Message processRoundEndMessage(Set<Message> gameMessages, Message msg){
		switch(GameController.db().getRole(msg.from).action){
		case CHECK:
			String message;
			if (isSleeping(gameMessages, msg.from))
				message = String.valueOf(ReplyFromGod.CANT_SAY.ordinal());
			else if (GameController.db().getRole(msg.to).thumbsUp())
				message = String.valueOf(ReplyFromGod.YES.ordinal());
			else
				message = String.valueOf(ReplyFromGod.NO.ordinal());
			return new Message(Message.GOD, msg.from, message);
		}
		throw new IllegalArgumentException("Cant process round end message: "+msg.toString());
	}
	
	public static String cityRisesToDeathOf(Set<Message> gameMessages){
		for(Message msg: gameMessages){
			if(msg.from.equals(GameController.db().getHitman())){
				if(isSaved(gameMessages, msg.to))
					return null;
				else
					return msg.to;
			}
		}
		throw new IllegalArgumentException("No message from hitman");
	}
	
	private static boolean isSaved(Set<Message> messages, String victim){
		for(Message msg: messages){
			if(GameController.db().getRole(msg.from).equals(Person.ANGEL)
				&& msg.to.equals(victim) && !isSleeping(messages, msg.from))
				return true;
		}
		return false;
	}
	
	private static boolean isSleeping(Set<Message> messages, String player) {
		for(Message msg: messages){
			if(GameController.db().getRole(msg.from).equals(Person.WITCH)
				&& msg.to.equals(player))
				return true;
		}
		return false;
	}

	public static boolean isGameMessage(String from){
		switch(GameController.db().getRole(from).action){
		case CHECK:
		case SUGGEST:
			return false;
		}
		return true;
	}
	
	public static Message processMessage(Message message){
		switch(GameController.db().getRole(message.from)){
		case MAFIA:
			return new Message(message.from, GameController.db().getHitman(), message.to);
		case CIVILIAN:
			return null;
		case HITMAN:
		case GODFATHER:
			for(String player: GameController.db().names)
				if(GameController.db().getRole(player).equals(Person.MAFIA))
					MessageCentre.newProcessedMessage(new Message(message.from, player, message.to));
		default:
			return message;
		}
	}
	
	public static String generateInfo(Message message){
		switch(GameController.db().getRole(message.to).action){
		case KILL:
			String[] mafia = message.from.split(Message.SEPARATOR);
			String[] victims = message.message.split(Message.SEPARATOR);
			StringBuilder userMsg = new StringBuilder();
			for(int i = 0 ; i < mafia.length; i++){
				userMsg.append(mafia[i]).append(SUGGESTIONS_TO_HITMAN).append(victims[i]);
				if(i < mafia.length - 1)
					userMsg.append("\n");
			}
			return userMsg.toString();
			
		case SUGGEST:
			return message.message + HITMAN_KILLED;
		}
		throw new UnsupportedOperationException("cannot generate for message" + message.toString());
	}

	public static Message combineMessage(Message newMessage, Message oldMessage) {
		switch(GameController.db().getRole(oldMessage.to)){
		case GODFATHER:
			return new Message(oldMessage.from + Message.SEPARATOR + newMessage.from , oldMessage.to,
					oldMessage.message + Message.SEPARATOR + newMessage.message);
		}
		throw new UnsupportedOperationException(
				"cannnot combine " + oldMessage.toString() + " and " + newMessage.toString());
	}
	
	public static ArrayList<String> getList(String name, Message message){
		switch (GameController.db().getRole(name).action) {
		case CHECK:
		case SAVE:
		case SLEEP:
			return GameController.db().names;
		case SUGGEST:
			if(message != null)
				break;
		case KILL:
			return createKillableList();
		}
		return null;
	}
	
	private static ArrayList<String> createKillableList(){
		ArrayList<String> killable = new ArrayList<String>(
				GameController.db().names);
		for(String name: GameController.db().names){
			Person person = GameController.db().getRole(name);
			if(person.isMafia())
				killable.remove(name);
		}
		return killable;
	}
	
}
