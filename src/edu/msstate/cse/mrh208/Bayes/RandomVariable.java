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
		this.parents = new HashSet<RandomVariable>();
		this.states = new HashSet<String>();
	}
	
	public RandomVariable(String name) {
		this.parents = new HashSet<RandomVariable>();
		this.states = new HashSet<String>();
		this.name = name;
	}
	
	private RandomVariable(RandomVariable from) {
		this.parents = new HashSet<RandomVariable>(from.parents);
		this.states  = from.states;
		this.name 	= from.name;
	}
	
	public RandomVariable clone() {
		return new RandomVariable(this);
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
	
	public String randomState() {
		return states.toArray(new String[0])[new Random().nextInt(states.size())];
	}

	public String toShortString() {
		return this.toShortString(0);
	}
	
	@Override 
	public boolean equals(Object aThat) {
		if(aThat == null) return false;
		if(!(aThat instanceof RandomVariable)) return false;
		if(aThat == this) return true;
		
		RandomVariable that = (RandomVariable) aThat;
		if(this.name.equals(that.name)) return true;
		return false;
	}
	
	@Override
	public String toShortString(int tabDepth) {
		return super.toShortString(tabDepth).replace("RandomVariable", this.name);
	}

	@Override
	public String toString() {
		return this.toString(0);
	}

	@Override
	public String toString(int tabDepth) {
		StringBuilder sb = new StringBuilder(super.toString(tabDepth));
		
		sb.append(this.name + " = { ");
		for(String state : states) sb.append(state + " ");
		sb.deleteCharAt(sb.length()-1).append(" }\t");
		sb.append("Parents:\t{ ");
		for(RandomVariable parent : parents) sb.append(parent.toShortString() + " ");
		sb.deleteCharAt(sb.length()-1).append(" }");
		return sb.toString();
	}
}
