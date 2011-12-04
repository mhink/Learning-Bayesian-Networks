package edu.msstate.cse.mrh208;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import com.google.common.collect.Multiset;

import edu.msstate.cse.mrh208.Algorithms.AStar;
import edu.msstate.cse.mrh208.Bayes.BayesianNetwork;
import edu.msstate.cse.mrh208.Bayes.RandomVariable;

public class Program {
	
	public static void main(String[] args) {
		
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
		
		List<Map<RandomVariable, String>> testData = new ArrayList<Map<RandomVariable, String>>();
		for(int i = 0; i < 20; i++) {
			Map<RandomVariable, String> constraints = new HashMap<RandomVariable, String>();
			constraints.put(rv1, rv1.randomState());
			constraints.put(rv2, rv2.randomState());
			constraints.put(rv3, rv3.randomState());
			testData.add(constraints);
		}
		
		List<RandomVariable> variables = new ArrayList<RandomVariable>();
		variables.add(rv1);
		variables.add(rv2);
		variables.add(rv3);
		List<RandomVariable> parents = new ArrayList<RandomVariable>();
		parents.add(rv1);
		parents.add(rv2);
		
		Dataset testDataset		 = Dataset.fromData(testData);
		
		double mdlh = BayesianNetwork.MDLh(rv3, parents, testDataset);
		
		System.out.println("Done");
		
//		Dataset dataset = new Dataset();
//		BayesianNetwork bayesianNetwork = BayesianNetwork.learnBayesianNetwork(dataset);
//		
//		bayesianNetwork.print();
	}
}
