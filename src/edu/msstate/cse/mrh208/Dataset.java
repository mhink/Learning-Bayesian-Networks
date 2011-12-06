package edu.msstate.cse.mrh208;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import edu.msstate.cse.mrh208.Bayes.RandomVariable;

public class Dataset extends Loggable{
	public class Entry extends Loggable{
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
		
		@Override
		public String toString() {
			return this.toString(0);
		}
		
		@Override
		public String toString(int tabDepth) {			
			StringBuilder sb = new StringBuilder(tabs(tabDepth) + super.toString());
			
			sb.append("[ ");
			for(String entryValue : entryValues.values()) sb.append(entryValue + " ");
			sb.deleteCharAt(sb.length()-1).append(" ]\t");
			
			sb.append("\t{ ");
			for(RandomVariable rv : entryValues.keySet()) sb.append(rv.toShortString() + "\t");
			sb.deleteCharAt(sb.length()-1).append(" }");
			
			return sb.toString();
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
	private Multiset<Entry> 			entries;
	
	public Dataset() {
		//TODO: Dataset constructor needs to be reading from a file somehow.  Sad but true.
		variables 	= new HashSet<RandomVariable>();
		entries		= HashMultiset.create();
	}
	
	public static Dataset fromData(List<Map<RandomVariable, String>> data, Set<RandomVariable> variables) {
		Dataset toReturn = new Dataset();
		toReturn.variables = variables;
		log("This variable list is being used", toReturn.variables);
		for(Map<RandomVariable, String> datapoint : data) {
			toReturn.entries.add(toReturn.new Entry(datapoint));
		}
		return toReturn;
	}
	
	public Set<RandomVariable> getVariables() {
		return variables;
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
	
	public int size() {
		return entries.size();
	}
	
	@Override
	public String toString() {
		return this.toString(0);
	}
	
	@Override
	public String toString(int tabDepth) {		
		StringBuilder sb = new StringBuilder(tabs(tabDepth) + super.toString());
		for(Entry entry : entries) sb.append(tabs(tabDepth)).append("\t").append(entry.toString());
		return sb.toString();
	}
}
