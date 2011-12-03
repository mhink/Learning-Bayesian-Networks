package edu.msstate.cse.mrh208;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
		
		List<RandomVariable> toCombine = new ArrayList<RandomVariable>();
		toCombine.add(rv1);
		toCombine.add(rv2);
		toCombine.add(rv3);
		
		Dataset combined = Dataset.allCombosOfStates(toCombine);
		
		System.out.println("Done");
		
//		Dataset dataset = new Dataset();
//		BayesianNetwork bayesianNetwork = BayesianNetwork.learnBayesianNetwork(dataset);
//		
//		bayesianNetwork.print();
	}
}
