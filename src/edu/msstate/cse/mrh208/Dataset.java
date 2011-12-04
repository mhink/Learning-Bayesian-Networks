package edu.msstate.cse.mrh208;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

import edu.msstate.cse.mrh208.Bayes.RandomVariable;

public class Dataset {
	public class Entry {
		private Map<RandomVariable, String> entryValues;
		
		public Entry() {
			entryValues = new HashMap<RandomVariable, String>();
			
			Iterator<RandomVariable> variablesIterator = variables.iterator();
			while(variablesIterator.hasNext()) {
				entryValues.put(variablesIterator.next(), null);
			}
		}
		
		public Entry(Map<RandomVariable, String> datapoint) {
			entryValues = new HashMap<RandomVariable, String>(datapoint);
		}
		
		public boolean isConsistentWith(RandomVariable rv, String state) {
			if(!entryValues.containsKey(rv)) return false;
			if(!entryValues.get(rv).equals(state)) return false;
			else return true;
		}
		
		public Entry clone() {
			Entry clone = new Entry();
			for(Map.Entry<RandomVariable, String> entryValue : entryValues.entrySet()) {
				clone.entryValues.put(entryValue.getKey(), entryValue.getValue());
			}
			
			return clone;
		}
	}
	private Set<RandomVariable>			variables;
	public 	Multiset<Entry> 			entries;
	
	public Dataset() {
		//TODO: Dataset constructor needs to be reading from a file somehow.  Sad but true.
		variables 	= new HashSet<RandomVariable>();
		entries		= HashMultiset.create();
	}
	
	public static Dataset fromData(List<Map<RandomVariable, String>> data) {
		Dataset toReturn = new Dataset();
		for(Map<RandomVariable, String> datapoint : data) {
			toReturn.entries.add(toReturn.new Entry(datapoint));
		}
		return toReturn;
	}
	
	public Dataset filter(Map<RandomVariable, String> constraints) {
		//I gotta say... I'm pleased with how elegant this code is.
		Dataset consistent = new Dataset();
		
		entryloop:
		for(Entry entry : entries) {
			for(Map.Entry<RandomVariable, String> constraint : constraints.entrySet()) {
				if(!entry.isConsistentWith(
						constraint.getKey(), 
						constraint.getValue())){
					continue entryloop;
				}
			}
			consistent.entries.add(entry);
		}
		
		return consistent;
	}
	
	
}
