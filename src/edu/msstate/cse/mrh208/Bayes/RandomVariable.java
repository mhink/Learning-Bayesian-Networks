package edu.msstate.cse.mrh208.Bayes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Sets;

public class RandomVariable {
	public String name;
	public Set<String> states;
	public Set<RandomVariable> parents;
	
	public RandomVariable() {
		this.name = new String();
		this.states = new HashSet<String>();
		this.parents = new HashSet<RandomVariable>();
	}
	
	public RandomVariable(String name, Set<String> states) {
		super();
		this.name = new String(name);
		this.states = new HashSet(states);
	}

	public RandomVariable clone() {
		return new RandomVariable(this.name, this.states);
	}
	
	public String randomState() {
		return states.toArray(new String[0])[new Random().nextInt(states.size())];
	}
	
	public void addState(String state) {
		states.add(state);
	}

	public String getName() {
		return name;
	}

	public Set<String> getStates() {
		return states;
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

	@Override
	public String toString() {
		return this.name;
	}
	
	public String toShortString() {
		return this.name + "-" + this.states.toString();
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
}
