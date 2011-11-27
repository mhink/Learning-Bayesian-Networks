package edu.msstate.cse.mrh208;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import edu.msstate.cse.mrh208.Bayes.RandomVariable;

public class Dataset {
	public class Entry {
		private Hashtable<RandomVariable, String> entry;
		public Entry() {
			entry = new Hashtable<RandomVariable, String>();
		}
		public void put(RandomVariable rv, String value) {
			entry.put(rv, value);
		}
		public String get(RandomVariable rv) {
			return entry.get(rv);
		}
	}
	private List<RandomVariable> 	variables;
	private List<Entry> 			entries;
	
	public Dataset() {
		//TODO: Dataset constructor needs to be reading from a file somehow.  Sad but true.
		//TODO: Dataset needs neccessary methods for BayesianNetwork to access info it needs.
	}
	
	public List<Entry> filterOnVariable(RandomVariable rv, String rvState, List<Entry> toFilter) {
		List<Entry> filtered = new ArrayList<Entry>();
		for(Entry entry : entries) {
			if(entry.get(rv).equals(rvState)) filtered.add(entry);
		}
		return filtered;
	}
}
