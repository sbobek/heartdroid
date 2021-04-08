/**
*
*     Copyright 2013-15 by Szymon Bobek, Grzegorz J. Nalepa, Mateusz Ślażyński
*
*
*     This file is part of HeaRTDroid.
*     HeaRTDroid is a rule engine that is based on HeaRT inference engine,
*     XTT2 representation and other concepts developed within the HeKatE project .
*
*     HeaRTDroid is free software: you can redistribute it and/or modify
*     it under the terms of the GNU General Public License as published by
*     the Free Software Foundation, either version 3 of the License, or
*     (at your option) any later version.
*
*     HeaRTDroid is distributed in the hope that it will be useful,
*     but WITHOUT ANY WARRANTY; without even the implied warranty of
*     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*     GNU General Public License for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with HeaRTDroid.  If not, see <http://www.gnu.org/licenses/>.
*
**/


package heart.uncertainty;

import heart.xtt.Rule;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class ConflictSet implements Iterable<SimpleEntry<Rule,UncertainTrue>>{
	private HashMap<Rule, UncertainTrue> conflictSet;
	private LinkedList<SimpleEntry<Rule,UncertainTrue>> orderedSet;
	
	public ConflictSet() {
		conflictSet = new HashMap<Rule, UncertainTrue>();
		orderedSet = new LinkedList<SimpleEntry<Rule,UncertainTrue>>();
	}
	
	public void add(Rule key,UncertainTrue value){
		if(conflictSet.put(key, value) == null){
			orderedSet.add(new SimpleEntry<Rule,UncertainTrue>(key,value));
		}
	}
	
	public void clear(){
		conflictSet.clear();
		orderedSet.clear();
	}

	@Override
	public Iterator<SimpleEntry<Rule,UncertainTrue>> iterator() {
		return orderedSet.iterator();
	}

	public ConflictSet copy() {
		ConflictSet cs = new ConflictSet();
		for(SimpleEntry<Rule,UncertainTrue> c: this){
			cs.add(c.getKey(),c.getValue());
		}
		return cs;
	}

	public boolean isEmpty(){
		return orderedSet.isEmpty();
	}
	
	public SimpleEntry<Rule, UncertainTrue> getFirst(){
		return orderedSet.getFirst();
	}
	
	public SimpleEntry<Rule, UncertainTrue> getLast(){
		return orderedSet.getLast();
	}
	
	public int size(){
		return orderedSet.size(); 
	}
	
	
}
