package edu.msstate.cse.mrh208;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import edu.msstate.cse.mrh208.Bayes.RandomVariable;

public class Dataset {
	public class Entry {
		private List<String> entryValues;
		public Entry() {
			entryValues = new ArrayList<String>();
		}
		public boolean setVariable(RandomVariable randomVariable, String state) {
			if(!randomVariable.states.contains(state)) return false;
			if(!variables.contains(randomVariable)) { variables.add(randomVariable); }
			
			int idx = variables.indexOf(randomVariable);
			
			if(entryValues.size() > idx) entryValues.set(idx, state);
			else entryValues.add(state);
			
			return true;
		}
		public String getVariableState(RandomVariable randomVariable) {
			if(!variables.contains(randomVariable)) return null;
			
			return entryValues.get(variables.indexOf(randomVariable));
		}
		
		public Entry clone() {
			Entry clone = new Entry();
			for(int i = 0; i < entryValues.size(); i++) {
				clone.entryValues.add(this.entryValues.get(i));
			}
			return clone;
		}
	}
	private List<RandomVariable>	variables;
	public List<Entry> 			entries;
	
	public Dataset() {
		//TODO: Dataset constructor needs to be reading from a file somehow.  Sad but true.
		//TODO: Dataset needs neccessary methods for BayesianNetwork to access info it needs.
	}
	
	public static Dataset filterOnVariable(RandomVariable rv, String rvState, Dataset toFilter) {
		Dataset filtered = new Dataset();
		for(Entry entry : toFilter.entries) {
			if(entry.getVariableState(rv).equals(rvState)) filtered.entries.add(entry);
		}
		return filtered;
	}
	
	public int entriesConsistentWith(List<RandomVariable> vars, List<String> varStates) {
		Dataset filtered = this;
		
		for(int i = 0; i < vars.size(); i++) {
			filtered = Dataset.filterOnVariable(vars.get(i), varStates.get(i), filtered);
		}
		
		return filtered.entries.size();
	}
	
	public static Dataset allCombosOfStates(List<RandomVariable> randomVariables) {
		Dataset toReturn = new Dataset();
		toReturn.variables = new ArrayList<RandomVariable>();
		toReturn.entries = new ArrayList<Entry>();
		toReturn.entries.add(toReturn.new Entry());
		
		for(RandomVariable randomVariable : randomVariables) {
			toReturn.combine(randomVariable);
		}
		
		return toReturn;
	}
	
	public Dataset combine(RandomVariable rv) {
		int entriesSize = this.entries.size();
		this.copy(rv.states.size());
		
		for(int i = 0; i < this.entries.size(); i++) {
			String state = rv.states.get(i / entriesSize);
			this.entries.get(i).setVariable(rv, state);
		}
		
		return this;
	}
	
	public Dataset copy(int times) {
		int j = this.entries.size() * (times- 1) ;
		for(int i = 0; i < j; i++) {
			this.entries.add(this.entries.get(i % j).clone());			//NEED TO BE ABLE TO DEEP COPY ENTRIES
		} 
		
		return this;
	}
}
