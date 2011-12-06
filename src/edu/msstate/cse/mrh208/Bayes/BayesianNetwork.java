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
import edu.msstate.cse.mrh208.Algorithms.BNSearchNode;

public class BayesianNetwork extends Loggable{
	public static BayesianNetwork goalNetwork;
	public Dataset dataset;
	public Set<RandomVariable> variablesInNetwork;
	public Set<RandomVariable> variablesNotInNetwork;
	
	public static BayesianNetwork learnBayesianNetwork(Dataset dataset) throws Exception {
		BayesianNetwork.goalNetwork				= new BayesianNetwork(dataset);
		BayesianNetwork.goalNetwork.variablesInNetwork 		= new HashSet<RandomVariable>(dataset.getVariables());
		BayesianNetwork.goalNetwork.variablesNotInNetwork 	= new HashSet<RandomVariable>();
		
		BayesianNetwork bayesianNetwork 		= new BayesianNetwork(dataset);
		bayesianNetwork.variablesInNetwork 		= new HashSet<RandomVariable>();
		bayesianNetwork.variablesNotInNetwork 	= new HashSet<RandomVariable>(dataset.getVariables());
		
		BayesianNetwork result = (new AStar()).Search(bayesianNetwork);
		return result;
	}

	private BayesianNetwork(Dataset dataset) {
		this.dataset 				= dataset;
		this.variablesInNetwork 	= new HashSet<RandomVariable>();
		this.variablesNotInNetwork 	= new HashSet<RandomVariable>();
	}

	public BayesianNetwork clone() {
		BayesianNetwork clone = new BayesianNetwork(this.dataset);
		clone.variablesInNetwork	.addAll(this.variablesInNetwork);
		clone.variablesNotInNetwork	.addAll(this.variablesNotInNetwork);
		return clone;
	}

	public Set<RandomVariable> bestParentSet(RandomVariable X) {
		return calculateBestParentSet(X, this.variablesNotInNetwork, this.dataset);
	}
	
	public double heuristic(RandomVariable U) {
		return calculateHeuristic(U, this.variablesNotInNetwork, this.dataset);
	}
	
	public double pathCost(RandomVariable X, Set<RandomVariable> S1) {
		return calculateBestMDL(X, S1, this.dataset);
	}
	
	private static double calculateHeuristic(RandomVariable U, Set<RandomVariable> V, Dataset dataset) {
		double heuristicValue = 0d;
		
		Set<RandomVariable> VwithoutX;
		Set<RandomVariable> VwithoutU = new HashSet<RandomVariable>(V);
		VwithoutU.remove(U);
		
		for(RandomVariable X : VwithoutU) {
			VwithoutX = new HashSet<RandomVariable>(V);
			VwithoutX.remove(X);
			
			heuristicValue += calculateBestMDL(X, VwithoutX, dataset);
		}
		
		return heuristicValue;
	}
	
	private static double calculateBestMDL(RandomVariable X, Set<RandomVariable> parentCandidates, Dataset dataset) {
		Set<Set<RandomVariable>> parentCandidatePowerSet = Sets.powerSet(parentCandidates);
		double lowest = Double.POSITIVE_INFINITY;
		Set<RandomVariable> bestCandidate = new HashSet<RandomVariable>();
		
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
	
	@Override
	public String toString() {
		return this.toString(0);
	}

	@Override
	public String toString(int tabDepth) {		
		StringBuilder sb = new StringBuilder(super.toString(tabDepth));
		sb.append(newline(tabDepth + 1)).append("Goal:\t\t\t");
		if(goalNetwork != null) 
			sb.append("BayesianNetwork@" + Integer.toHexString(goalNetwork.hashCode()));
		else 
			sb.append("NULL");
		sb.append(newline(tabDepth + 1)).append("Variables in:\t");
		if(variablesInNetwork.isEmpty()) sb.append("NONE");
		else for(RandomVariable in : variablesInNetwork) {
			sb.append(in.toShortString(tabDepth + 2)).append(" PARENTS: { ");
			for(RandomVariable inParents : in.parents) sb.append(in.toShortString(-1));
			sb.deleteCharAt(sb.length()-1).append(" }");
		}

		sb.append(newline(tabDepth + 1)).append("Variables out:\t");
		if(variablesNotInNetwork.isEmpty()) sb.append("NONE");
		else for(RandomVariable out : variablesNotInNetwork)
			sb.append(out.toShortString(-1).replaceAll("\t", " "));
		
		return sb.toString();
	}

	public void print() {
		System.out.println(this);
	}
}
