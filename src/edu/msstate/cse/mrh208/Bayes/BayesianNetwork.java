package edu.msstate.cse.mrh208.Bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

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
	
	public double heuristic(RandomVariable U, Set<RandomVariable> nonNetworkVariables, Dataset dataset) {
		return 0d;
	}
	
	public double bestMDL(RandomVariable X, Set<RandomVariable> parentCandidates, Dataset dataset) {
		Set<Set<RandomVariable>> parentCandidatePowerSet = Sets.powerSet(parentCandidates);
		double lowest = Double.POSITIVE_INFINITY;
		Set<RandomVariable> bestParentCandidate = new HashSet<RandomVariable>();
		
		for(Set<RandomVariable> parentCandidate : parentCandidatePowerSet) {
			double mdl = MDL(X, parentCandidate, dataset);
			if(mdl < lowest) {
				lowest = mdl;
				bestParentCandidate = parentCandidate;
			}
		}
		
		return lowest;		
	}
	
	public static double MDL(RandomVariable X, Set<RandomVariable> parentsOfX, Dataset dataset) {
		double result = 0d;
		result += MDLh(X, parentsOfX, dataset);
		result += ((log2(dataset.size()) / 2) * MDLk(X, parentsOfX));
		
		return result;
	}
	
	public static double MDLh(RandomVariable X, Set<RandomVariable> parentsOfX, Dataset dataset) {
		Set<RandomVariable> randomVariables = new HashSet(parentsOfX);
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
	
	public static double MDLk(RandomVariable X, Set<RandomVariable> parentsOfX) {
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
