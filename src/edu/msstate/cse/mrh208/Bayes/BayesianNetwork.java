package edu.msstate.cse.mrh208.Bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.msstate.cse.mrh208.Dataset;

public class BayesianNetwork {
	Set<RandomVariable> variablesInNetwork;
	Set<RandomVariable> variablesNotInNetwork;
	
	public BayesianNetwork(Dataset dataset) {
		variablesInNetwork = new HashSet<RandomVariable>();
		variablesNotInNetwork = new HashSet<RandomVariable>();
	}
	
	public static BayesianNetwork learnBayesianNetwork(Dataset dataset) {
		//TODO: "BN.learn" should be calling the AStar.Search method.
		throw new UnsupportedOperationException();
	}
	
	
	public void print() {
		//TODO: "BN.print" is low priority.
		throw new UnsupportedOperationException();
	}
	
	public boolean hasSameVariables(BayesianNetwork other) {
		//TODO: "BN.hasSameRVs" is medium priority.  It's gonna be necessary for A* goal state evaluation.
		throw new UnsupportedOperationException();
	}
	
	public float bestMDL(RandomVariable X, List<RandomVariable> VwithoutX, Dataset dataset) {
		throw new UnsupportedOperationException();
	}
	
	public float MDL(RandomVariable X, List<RandomVariable> parentsOfX, Dataset dataset) {
		throw new UnsupportedOperationException();
	}
	
	public static double MDLh(RandomVariable X, List<RandomVariable> parentsOfX, Dataset dataset) {
		List<RandomVariable> randomVariables = new ArrayList(parentsOfX);
		randomVariables.add(X);
		
		List<Map<RandomVariable, String>> constraintsList = RandomVariable.combineVariables(randomVariables);
		
		double sum = 0d;
		
		for(Map<RandomVariable, String> constraints : constraintsList) {
			Map<RandomVariable, String> parentConstraints = new HashMap<RandomVariable, String>(constraints);
			parentConstraints.remove(X);
			
			int Nxipai = dataset.filter(constraints).size();
			int Npai = dataset.filter(parentConstraints).size();
			if(Nxipai > 0) sum += Nxipai * (log2( Nxipai ) - log2(Npai));
		}
		
		return (sum * -1.0);
	}
	
	public double MDLk(RandomVariable X, List<RandomVariable> parentsOfX) {
		double result = 1d;
		
		for(RandomVariable p : parentsOfX) {
			result *= p.states.size();
		}
		
		return ( ( X.states.size() - 1 ) * result );
	}
	
	public static double log2(double x) {
		return (Math.log(x) / Math.log(2));
	}
}
