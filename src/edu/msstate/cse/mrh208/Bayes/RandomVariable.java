package edu.msstate.cse.mrh208.Bayes;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import edu.msstate.cse.mrh208.Dataset;

public class RandomVariable {
	public List<RandomVariable> parents;
	public List<String> states;
	public String name;
	
	public RandomVariable() {
		parents = new ArrayList<RandomVariable>();
		states = new ArrayList<String>();		
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(other.getClass() != this.getClass()) return false;
		
		if(this.name.equals(((RandomVariable)other).name)) return true;
		
		return false;
	}
}
