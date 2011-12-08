package edu.msstate.cse.mrh208.Algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import edu.msstate.cse.mrh208.Dataset;
import edu.msstate.cse.mrh208.Bayes.RandomVariable;

public class MDL {
	
	public static double calculateHeuristic(RandomVariable U, Set<RandomVariable> V, Dataset dataset) {
		double heuristicValue = 0d;
		
		Set<RandomVariable> VwithoutX;
		Set<RandomVariable> VwithoutU = new LinkedHashSet<RandomVariable>(V);
		VwithoutU.remove(U);
		
		for(RandomVariable X : VwithoutU) {
			VwithoutX = new LinkedHashSet<RandomVariable>(V);
			VwithoutX.remove(X);
			
			heuristicValue += bestMDL(X, VwithoutX, dataset);
		}
		
		return heuristicValue;
	}
	
	public static double bestMDL(RandomVariable X, Set<RandomVariable> parentCandidates, Dataset dataset) {
		HashSet<HashSet<RandomVariable>> parentCandidatePowerSet = powerset(parentCandidates, calcMax(dataset.size()));
		double lowest = Double.POSITIVE_INFINITY;
		
		for(Set<RandomVariable> parentCandidate : parentCandidatePowerSet) {
			double mdl = MDL.mdl(X, parentCandidate, dataset);
			if(mdl < lowest) lowest = mdl;
		}
		
		return lowest;
	}
	
	public static HashSet<HashSet<RandomVariable>> powerset(Set<RandomVariable> list, int max) {
		  HashSet<HashSet<RandomVariable>> ps = new HashSet<HashSet<RandomVariable>>();
		  ps.add(new HashSet<RandomVariable>());
		  for (RandomVariable item : list) {
		    HashSet<HashSet<RandomVariable>> newPs = new HashSet<HashSet<RandomVariable>>();
		 
		    for (HashSet<RandomVariable> subset : ps) {
		      newPs.add(subset);
		 
		      if(subset.size() < max) {
			      HashSet<RandomVariable> newSubset = new HashSet<RandomVariable>(subset);
			      newSubset.add(item);
			      newPs.add(newSubset);
		      }
		    }
		    ps = newPs;
		  }
		  return ps;
		}
	
	public static int calcMax(int N) {
		double logN = log2((double)N);
		double N2divLogN = ((2 * N) / logN);
		
		return (int) Math.ceil(log2(N2divLogN));
	}
	
	public static Set<RandomVariable> calculateBestParentSet(RandomVariable X, HashSet<RandomVariable> parentCandidates, Dataset dataset) {
		HashSet<HashSet<RandomVariable>> parentCandidatePowerSet = powerset(parentCandidates, calcMax(dataset.size()));
		double lowest = Double.POSITIVE_INFINITY;
		Set<RandomVariable> bestCandidate = new LinkedHashSet<RandomVariable>();
		
		for(Set<RandomVariable> parentCandidate : parentCandidatePowerSet) {
			double mdl = MDL.mdl(X, parentCandidate, dataset);
			if(mdl < lowest) {
				bestCandidate = parentCandidate;
				lowest = mdl;
			}
		}
		
		return bestCandidate;
	}
	
	public static double mdl(RandomVariable X, Set<RandomVariable> parentsOfX, Dataset dataset) {
		double result = 0d;
		result += mdlh( X, parentsOfX, dataset ) + ( (log2(dataset.size()) / 2) * mdlk(X, parentsOfX) );
		
		return result;
	}
	
	public static double mdlh(RandomVariable X, Set<RandomVariable> parentsOfX, Dataset dataset) {
		Set<RandomVariable> randomVariables = new LinkedHashSet<RandomVariable>(parentsOfX);
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
	public static double mdlk(RandomVariable X, Set<RandomVariable> parentsOfX) {
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
