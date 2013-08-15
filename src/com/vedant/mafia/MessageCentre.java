package com.vedant.mafia;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.vedant.mafia.backend.GameRules;

public class MessageCentre {
	
	private static MessageCentre instance = null;
	
	private HashSet<Message> personMessages, gameMessages;
	
	public static void newRound(){
		if(instance == null)
			instance = new MessageCentre();
		else{
			instance.personMessages.clear();
			instance.gameMessages.clear();
		}
	}
	
	public static String cityRisesTo(){
		for(Message msg: instance.personMessages)
			GameController.roundEndMessage(
				GameRules.processRoundEndMessage(instance.gameMessages, msg) );
		return GameRules.cityRisesToDeathOf(instance.gameMessages);
	}
	
	public static void newProcessedMessage(Message newMessage){
		if(GameRules.isGameMessage(newMessage.from))
			instance.gameMessages.add(newMessage);
		else{
			Iterator<Message> i = instance.personMessages.iterator();
			while(i.hasNext()){
				Message oldMessage = i.next();
				if(oldMessage.to.equals(newMessage.to)){
					i.remove();
					instance.personMessages.add(GameRules.combineMessage(newMessage, oldMessage) );
					return;
				}
			}
			instance.personMessages.add(newMessage);
		}
	}
	
	public static String getInfoFor(String name){
		Message message = instance.findPersonMessageFor(name);
		if(message == null)
			return null;
		else{
			instance.personMessages.remove(message);
			return GameRules.generateInfo(message);
		}
	}
	
	public static ArrayList<String> getListFor(String name){
		return GameRules.getList(name, instance.findPersonMessageFor(name) );
	}
	
	private Message findPersonMessageFor(String name){
		for(Message message: personMessages)
			if(message.to.equals(name))
				return message;
		return null;
	}
	
	private MessageCentre(){
		personMessages = new HashSet<Message>();
		gameMessages =  new HashSet<Message>();
	}
}
