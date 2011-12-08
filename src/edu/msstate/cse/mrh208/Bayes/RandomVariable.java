package edu.msstate.cse.mrh208.Bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

public class RandomVariable {

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RandomVariable))
			return false;
		RandomVariable other = (RandomVariable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Set<RandomVariable> parents;
	public Set<String> states;
	public String name;
	
	public RandomVariable() {
		this.parents = new LinkedHashSet<RandomVariable>();
		this.states = new LinkedHashSet<String>();
	}
	
	public RandomVariable(String name) {
		this.parents = new LinkedHashSet<RandomVariable>();
		this.states = new LinkedHashSet<String>();
		this.name = name;
	}
	
	private RandomVariable(RandomVariable from) {
		this.parents = new LinkedHashSet<RandomVariable>(from.parents);
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
}
