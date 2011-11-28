package edu.msstate.cse.mrh208;

import java.util.Hashtable;

import edu.msstate.cse.mrh208.Bayes.RandomVariable;

public class PartialEntry {
	private Hashtable<RandomVariable, String> entry;
	public PartialEntry() {
		entry = new Hashtable<RandomVariable, String>();
	}
	public void put(RandomVariable rv, String value) {
		entry.put(rv, value);
	}
	public String get(RandomVariable rv) {
		return entry.get(rv);
	}
}
