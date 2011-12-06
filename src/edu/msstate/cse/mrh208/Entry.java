package edu.msstate.cse.mrh208;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.msstate.cse.mrh208.Bayes.RandomVariable;

public class Entry extends Loggable{
	private final Dataset dataset;
	private Map<RandomVariable, String> entryValues;
	
	public Entry(Dataset dataset) {
		this.dataset = dataset;
		entryValues = new HashMap<RandomVariable, String>();
		
		Iterator<RandomVariable> variablesIterator = this.dataset.variables.iterator();
		while(variablesIterator.hasNext()) {
			entryValues.put(variablesIterator.next(), null);
		}
	}
	
	public Entry(Dataset dataset, Map<RandomVariable, String> datapoint) {
		this.dataset = dataset;
		entryValues = new HashMap<RandomVariable, String>(datapoint);
	}
	
	public boolean isConsistentWith(RandomVariable rv, String state) {
		if(!entryValues.containsKey(rv)) return false;
		if(!entryValues.get(rv).equals(state)) return false;
		else return true;
	}
	
	public Entry clone() {
		Entry clone = new Entry(this.dataset);
		for(Map.Entry<RandomVariable, String> entryValue : entryValues.entrySet()) {
			clone.entryValues.put(entryValue.getKey(), entryValue.getValue());
		}
		
		return clone;
	}

	@Override
	public String toString() {
		return this.toString(0);
	}
	
	@Override
	public String toString(int tabDepth) {			
		StringBuilder sb = new StringBuilder(super.toString(tabDepth));
		
		sb.append("[ ");
		Iterator ite = entryValues.entrySet().iterator();
		
		while(ite.hasNext()) {
			Map.Entry<RandomVariable, String> nextEntry 
				= (Map.Entry<RandomVariable, String>) ite.next();
			sb.append(nextEntry.getKey().name + " = " + nextEntry.getValue() + ", ");
		}
		sb.deleteCharAt(sb.length()-2).append("]\t");
		
		sb.append("\t[ ");
		for(RandomVariable rv : entryValues.keySet()) 
			sb.append(rv.toShortString(-1)).append(" ");
		sb.deleteCharAt(sb.length()-1).append(" ]");
		
		return sb.toString();
	}
}