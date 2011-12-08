package edu.msstate.cse.mrh208;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.msstate.cse.mrh208.Bayes.BayesianNetwork;
import edu.msstate.cse.mrh208.Bayes.RandomVariable;

public class Program {
	
	public static void main(String[] args) throws Exception {
		
		RandomVariable rv1 = new RandomVariable();
		rv1.name = "rv1";
		rv1.states.add("A");
		rv1.states.add("B");
		rv1.states.add("C");
		
		RandomVariable rv2 = new RandomVariable();
		rv2.name = "rv2";
		rv2.states.add("1");
		rv2.states.add("2");
		
		RandomVariable rv3 = new RandomVariable();
		rv3.name = "rv3";
		rv3.states.add("x");
		rv3.states.add("y");
		
		RandomVariable rv4 = new RandomVariable();
		rv4.name = "rv4";
		rv4.states.add("M");
		rv4.states.add("N");
		
		List<Map<RandomVariable, String>> testData = new ArrayList<Map<RandomVariable, String>>();
		for(int i = 0; i < 20; i++) {
			Map<RandomVariable, String> constraints = new HashMap<RandomVariable, String>();
			constraints.put(rv1, rv1.randomState());
			constraints.put(rv2, rv2.randomState());
			constraints.put(rv3, rv3.randomState());
			constraints.put(rv4, rv4.randomState());
			testData.add(constraints);
		}
		
		Set<RandomVariable> vars = new LinkedHashSet<RandomVariable>();
		vars.add(rv1);
		vars.add(rv2);
		vars.add(rv3);
		vars.add(rv4);
	
		Dataset dataset = Dataset.fromData("data/car.data");
		
		BayesianNetwork bayesianNetwork = BayesianNetwork.learnBayesianNetwork(dataset);
		
		System.out.println(bayesianNetwork);
	}
}
