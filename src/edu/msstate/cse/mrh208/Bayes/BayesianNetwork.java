package edu.msstate.cse.mrh208.Bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import edu.msstate.cse.mrh208.Dataset;
import edu.msstate.cse.mrh208.Algorithms.AStar;
import edu.msstate.cse.mrh208.Algorithms.BNSearchNode;

public class BayesianNetwork {
	public static BayesianNetwork goalNetwork;
	public Dataset dataset;
	private double heuristic = Double.NaN;
	private Set<RandomVariable> bestParentSet;
	public Set<RandomVariable> variablesInNetwork;
	public Set<RandomVariable> variablesNotInNetwork;
	
	private BayesianNetwork(Dataset dataset) {
		this.dataset = dataset;
		this.variablesNotInNetwork = new HashSet<RandomVariable>(dataset.getVariables());
		this.variablesInNetwork = new HashSet<RandomVariable>();
	}
	
	public static BayesianNetwork learnBayesianNetwork(Dataset dataset) {
		BayesianNetwork bayesianNetwork = new BayesianNetwork(dataset);	
		BayesianNetwork.goalNetwork		= new BayesianNetwork(dataset);
		BayesianNetwork.goalNetwork.variablesInNetwork 		= new HashSet<RandomVariable>(dataset.getVariables());
		BayesianNetwork.goalNetwork.variablesNotInNetwork 	= new HashSet<RandomVariable>();
		
		BayesianNetwork result = AStar.Search(bayesianNetwork);
		
		return result;
	}
	
	public BayesianNetwork clone() {
		BayesianNetwork clone = new BayesianNetwork(this.dataset);
		clone.variablesInNetwork	.addAll(this.variablesInNetwork);
		clone.variablesNotInNetwork	.addAll(this.variablesNotInNetwork);
		return clone;
	}
	
	public Set<RandomVariable> bestParentSet(RandomVariable X) {
		if(bestParentSet == null) bestParentSet = calculateBestParentSet(X, this.variablesNotInNetwork, this.dataset);
		return bestParentSet;
	}
	
	public double heuristic(RandomVariable U) {
		if(heuristic == Double.NaN) heuristic = calculateHeuristic(U, variablesNotInNetwork, dataset);
		return heuristic;
	}
	
	public double pathCost(RandomVariable X, Set<RandomVariable> S1) {
		return calculateBestMDL(X, S1, dataset);
	}
	
	public boolean equals(BayesianNetwork other) {
		if(this.variablesInNetwork.equals(other.variablesInNetwork)) return true;
		else return false;
	}
	
	private static double calculateHeuristic(RandomVariable U, Set<RandomVariable> V, Dataset dataset) {
		double heuristicValue = 0d;
		
		Set<RandomVariable> VwithoutX;
		Set<RandomVariable> VwithoutU = new HashSet<RandomVariable>(V);
		VwithoutU.remove(U);
		
		for(RandomVariable X : VwithoutU) {
			VwithoutX = new HashSet<RandomVariable>(V);
			VwithoutX.remove(X);
			
			heuristicValue += calculateBestMDL(X, V, dataset);
		}
		
		return heuristicValue;
	}
	
	private static double calculateBestMDL(RandomVariable X, Set<RandomVariable> parentCandidates, Dataset dataset) {
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
	
	private static Set<RandomVariable> calculateBestParentSet(RandomVariable X, Set<RandomVariable> parentCandidates, Dataset dataset) {
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
		
		return bestParentCandidate;		
	}
	
	private static double MDL(RandomVariable X, Set<RandomVariable> parentsOfX, Dataset dataset) {
		double result = 0d;
		result += MDLh(X, parentsOfX, dataset);
		result += ((log2(dataset.size()) / 2) * MDLk(X, parentsOfX));
		
		return result;
	}
	
	private static double MDLh(RandomVariable X, Set<RandomVariable> parentsOfX, Dataset dataset) {
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
	
	private static double MDLk(RandomVariable X, Set<RandomVariable> parentsOfX) {
		double result = 1d;
		
		for(RandomVariable p : parentsOfX) {
			result *= p.states.size();
		}
		
		return ( ( X.states.size() - 1 ) * result );
	}
	
	private static double log2(double x) {
		return (Math.log(x) / Math.log(2));
	}
	
	public void print() {
		//TODO: "BN.print" is low priority.
		throw new UnsupportedOperationException();
	}
}
