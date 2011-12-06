package edu.msstate.cse.mrh208.Bayes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import edu.msstate.cse.mrh208.Dataset;
import edu.msstate.cse.mrh208.Loggable;
import edu.msstate.cse.mrh208.Algorithms.AStar;

public class BayesianNetwork extends Loggable{
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
	
	@Override
	public String toString(int tabDepth) {		
		StringBuilder sb = new StringBuilder(tabs(tabDepth) + super.toString());
		sb	.append(tabs(tabDepth + 1)).append("Goal     : ")
			.append(this.goalNetwork.toShortString());
		sb	.append(tabs(tabDepth + 1)).append("Heuristic: ")
			.append(this.heuristic);
		sb	.append(tabs(tabDepth + 1)).append("Variables in network: ");
		for(RandomVariable in : variablesInNetwork)
			sb	.append(tabs(tabDepth + 2))
				.append(in.toShortString());
		sb.append(tabs(tabDepth + 1)).append("Variables not in network: ");
		for(RandomVariable out : variablesNotInNetwork)
			sb	.append(tabs(tabDepth + 2))
				.append(out.toShortString());
		
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return this.toString(0);
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
		if(Double.valueOf(heuristic).equals(Double.NaN)) 
			heuristic = calculateHeuristic(U, variablesNotInNetwork, dataset);
		return heuristic;
	}
	
	public double pathCost(RandomVariable X, Set<RandomVariable> S1) {
		return calculateBestMDL(X, S1, dataset);
	}
	
	public boolean equals(BayesianNetwork other) {
		//TODO: Might be something wrong with this.
		if(this.variablesInNetwork.equals(other.variablesInNetwork)) return true;
		else return false;
	}
	
	public int hashCode() {
		//Might be something wrong with this.
		int hash = 1;
		hash = 31 * hash + this.variablesInNetwork.hashCode();
		hash = 31 * hash + this.variablesNotInNetwork.hashCode();
		
		return hash;
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
		
		for(Set<RandomVariable> parentCandidate : parentCandidatePowerSet) {
			double mdl = MDL(X, parentCandidate, dataset);
			if(mdl < lowest) lowest = mdl;
		}
		
		return lowest;		
	}
	
	private static Set<RandomVariable> calculateBestParentSet(RandomVariable X, Set<RandomVariable> parentCandidates, Dataset dataset) {
		Set<Set<RandomVariable>> parentCandidatePowerSet = Sets.powerSet(parentCandidates);
		double lowest = Double.POSITIVE_INFINITY;
		Set<RandomVariable> bestCandidate = new HashSet<RandomVariable>();
		
		for(Set<RandomVariable> parentCandidate : parentCandidatePowerSet) {
			double mdl = MDL(X, parentCandidate, dataset);
			if(mdl < lowest) {
				bestCandidate = parentCandidate;
				lowest = mdl;
			}
		}
		
		return bestCandidate;		
	}
	
	private static double MDL(RandomVariable X, Set<RandomVariable> parentsOfX, Dataset dataset) {
		double result = 0d;
		result += MDLh( X, parentsOfX, dataset ) + ( (log2(dataset.size()) / 2) * MDLk(X, parentsOfX) );
		
		return result;
	}
	
	private static double MDLh(RandomVariable X, Set<RandomVariable> parentsOfX, Dataset dataset) {
		Set<RandomVariable> randomVariables = new HashSet<RandomVariable>(parentsOfX);
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
