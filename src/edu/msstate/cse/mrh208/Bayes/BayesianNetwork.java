package edu.msstate.cse.mrh208.Bayes;

import java.util.ArrayList;
import java.util.List;

import edu.msstate.cse.mrh208.Dataset;

public class BayesianNetwork {
	List<RandomVariable> variablesInNetwork;
	List<RandomVariable> variablesNotInNetwork;
	
	public BayesianNetwork(Dataset dataset) {
		variablesInNetwork = new ArrayList<RandomVariable>();
		variablesNotInNetwork = new ArrayList<RandomVariable>();
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
	
<<<<<<< HEAD
	public float MDLh(RandomVariable X, List<RandomVariable> parentsOfX, Dataset dataset) {
		throw new UnsupportedOperationException();
=======
	public float MDLh(RandomVariable X, List<RandomVariable> parentsOfX) {
    throw new UnsupportedOperationException();		
>>>>>>> 8e7d08c9a4ea2dcadc907b4cbfa4b60578fb7bd2
	}
	
	public float MDLk(RandomVariable X, List<RandomVariable> parentsOfX) {
		float result = 1f;
		
		for(RandomVariable p : parentsOfX) {
			result *= p.states.size();
		}
		
		return ( ( X.states.size() - 1 ) * result );
	}
}
