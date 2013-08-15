package com.vedant.mafia;

import java.util.ArrayList;

import com.vedant.mafia.backend.Person;

public class PlayersDb {
	public ArrayList<String> names;
	public ArrayList<Person> types;
	public ArrayList<Integer> pins;
	
	public void reset(){
		names.clear();
		pins.clear();
		types.clear();
	}
	
	public void add(String name, int pin){
		names.add(name);
		pins.add(pin);
	}
	
	public String getHitman(){
		int index = types.indexOf(Person.GODFATHER);
		if(index<0)
			index = types.indexOf(Person.HITMAN);
		return names.get(index);
	}
	
	public Person getRole(String name){
		return types.get(names.indexOf(name));
	}

	public boolean check(String name, int pin) {
		if(pins.get(names.indexOf(name)) == pin)
			return true;
		else
			return false;
	}
	
	public void remove(String name){
		int index = names.indexOf(name);
		names.remove(index);
		types.remove(index);
		pins.remove(index);
	}
	
	public PlayersDb(int initCapacity){
		names = new ArrayList<String> (initCapacity);
		types = new ArrayList<Person> (initCapacity);
		pins = new ArrayList<Integer> (initCapacity);
	}
}
