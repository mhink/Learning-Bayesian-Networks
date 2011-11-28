package edu.msstate.cse.mrh208;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.msstate.cse.mrh208.Algorithms.AStar;
import edu.msstate.cse.mrh208.Bayes.BayesianNetwork;

public class Program {
	
	public static void main(String[] args) {
		Dataset dataset = new Dataset();
		BayesianNetwork bayesianNetwork = BayesianNetwork.learnBayesianNetwork(dataset);
		
		bayesianNetwork.print();
	}
}
