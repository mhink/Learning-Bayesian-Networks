package edu.msstate.cse.mrh208.Bayes;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import edu.msstate.cse.mrh208.Dataset;
import edu.msstate.cse.mrh208.PartialEntry;

public class RandomVariable {
	public List<RandomVariable> parents;
	public List<String> states;
	public String name;
	
	public RandomVariable() {
		
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(other.getClass() != this.getClass()) return false;
		
		if(this.name.equals(((RandomVariable)other).name)) return true;
		
		return false;
	}
	
	public List<PartialEntry> combineWith(RandomVariable other) {
		List<PartialEntry> partialEntries = new ArrayList<PartialEntry>();
		for(String thisState : this.states) 
			for(String otherState : other.states) {
				PartialEntry entry = new PartialEntry();
				entry.put(this, thisState);
				entry.put(other, otherState);
				partialEntries.add(entry);
			}
		
		return partialEntries;
	}
}
