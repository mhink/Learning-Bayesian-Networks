package edu.msstate.cse.mrh208;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import edu.msstate.cse.mrh208.Bayes.RandomVariable;

public class Dataset extends Loggable{
	Set<RandomVariable>		variables;
	private Multiset<Entry> entries;
	
	public Dataset() {
		variables 	= new HashSet<RandomVariable>();
		entries		= HashMultiset.create();
	}
	
	public static Dataset fromData(List<Map<RandomVariable, String>> data, Set<RandomVariable> variables) {
		Dataset toReturn = new Dataset();
		toReturn.variables = variables;
		for(Map<RandomVariable, String> datapoint : data) {
			toReturn.entries.add(new Entry(toReturn, datapoint));
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
		StringBuilder sb = new StringBuilder(super.toString(tabDepth));
		for(Entry entry : entries) sb.append(entry.toString(tabDepth + 1));
		return sb.toString();
	}
}
