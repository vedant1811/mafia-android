package com.vedant.mafia;


public class Message{
	public static final String SEPARATOR = ",",
								GOD = "%god",
								NA = "%NA",
								NO_ONE = "%noOne";
	
	private static final String PRIVATE_SEPARATOR = "_";
	
	public final String from, to, message;
	
	public Message(String from, String to, String message) {
		this.from = from;
		this.to = to;
		this.message = message;
	}
	
	public Message(String message){
		String messages[] = message.split(PRIVATE_SEPARATOR);
		from = messages[0];
		to = messages[1];
		if(messages.length > 2)
			this.message = messages[2];
		else
			this.message = null;
			
	}
	
	public Message(String from, String to) {
		this(from, to, null);
	}
	
	@Override
	public String toString(){
		if(message == null)
			return from+PRIVATE_SEPARATOR+to;
		else
			return from+PRIVATE_SEPARATOR+to+PRIVATE_SEPARATOR+message;
	}
	
}