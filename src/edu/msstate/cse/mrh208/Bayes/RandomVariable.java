package edu.msstate.cse.mrh208.Bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import edu.msstate.cse.mrh208.Loggable;

public class RandomVariable extends Loggable{
	public Set<RandomVariable> parents;
	public Set<String> states;
	public String name;
	
	public RandomVariable() {
		parents = new HashSet<RandomVariable>();
		states = new HashSet<String>();
	}
	
	public String toShortString(int tabDepth) {
		return super.toString(tabDepth).replace("RandomVariable@", "\"" + name+"\"@");
	}
	
	public String toShortString() {
		return this.toShortString(0);
	}
	
	@Override
	public String toString() {
		return this.toString(0);
	}
	
	@Override
	public String toString(int tabDepth) {		
		StringBuilder sb = new StringBuilder(super.toString(tabDepth));
			sb.append(tabs(tabDepth)).append("\tName: \t\t\"" + name + "\"");
			sb.append(tabs(tabDepth)).append("\tStates: \t{ ");
		for(String state : states) sb.append(state + " ");
		sb.deleteCharAt(sb.length()-1).append(" }")
			.append(tabs(tabDepth)).append("\tParents:\t{ ");
		for(RandomVariable parent : parents) sb.append(parent.toShortString() + " ");
		sb.deleteCharAt(sb.length()-1).append(" }");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(other.getClass() != this.getClass()) return false;
		
		if(this.name.equals(((RandomVariable)other).name)) return true;
		
		return false;
	}
	
	public static List<Map<RandomVariable, String>> combineVariables(Set<RandomVariable> randomVariables) {
		List<Set<String>> 					states 	= new ArrayList<Set<String>>();
		List<Map<RandomVariable, String>> 	result 	= new ArrayList<Map<RandomVariable, String>>();
		
		for(RandomVariable randomVariable : randomVariables) {
			states.add(randomVariable.states);
		}
		
		Set<List<String>> cartesianProduct = Sets.cartesianProduct(states);
		
		for(List<String> element : cartesianProduct) {
			Map<RandomVariable, String>	toAdd = new HashMap<RandomVariable, String>();
			Iterator<RandomVariable> 	i1 = randomVariables.iterator();
			Iterator<String> 			i2 = element.iterator();
			
			//Note for the future: look into writing a "co-iterator" class.
			while(i1.hasNext() && i2.hasNext()) {
				toAdd.put(i1.next(), i2.next());
			}
			
			result.add(toAdd);
		}
		
		return result;
	}
	
	public RandomVariable copyWithoutParents() {
		RandomVariable clone = new RandomVariable();
		clone.states.addAll(this.states);
		clone.name = this.name;
		
		return clone;
	}
	
	public String randomState() {
		return states.toArray(new String[0])[new Random().nextInt(states.size())];
	}
}
