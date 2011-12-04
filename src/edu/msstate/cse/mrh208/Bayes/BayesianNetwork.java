package edu.msstate.cse.mrh208.Bayes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
	
	public float MDLh(RandomVariable X, List<RandomVariable> parentsOfX, Dataset dataset) {
		throw new UnsupportedOperationException();
	}
	
	public float MDLk(RandomVariable X, List<RandomVariable> parentsOfX) {
		float result = 1f;
		
		for(RandomVariable p : parentsOfX) {
			result *= p.states.size();
		}
		
		return ( ( X.states.size() - 1 ) * result );
	}
}
