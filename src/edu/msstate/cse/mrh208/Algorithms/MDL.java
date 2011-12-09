package edu.msstate.cse.mrh208.Algorithms;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.google.common.collect.Sets;

import edu.msstate.cse.mrh208.ADTree;
import edu.msstate.cse.mrh208.Log;
import edu.msstate.cse.mrh208.Bayes.RandomVariable;

public class MDL {
	
	public static double heuristic(Set<RandomVariable> variablesNotInNetwork, ADTree dataset) {
		Log.log("AT: ", dataset);
		double heuristicValue = 0d;
		
		for(RandomVariable X : variablesNotInNetwork) {
			Set<RandomVariable> VwithoutX = new LinkedHashSet<RandomVariable>(dataset.getVariables());
			VwithoutX.remove(X);
			
			heuristicValue += MDL.bestMDL(X, VwithoutX, dataset);
		}
		
		return heuristicValue;
	}
	
	public static double bestMDL(RandomVariable X, Set<RandomVariable> parentCandidates, ADTree dataset) {
		Log.log("AT: ", X);
		int max = calcMax(dataset.size());
		HashSet<HashSet<RandomVariable>> parentCandidatePowerSet = powerset(parentCandidates, max);
		if(!parentCandidates.isEmpty()) parentCandidatePowerSet.remove(new HashSet<RandomVariable>());
		double lowest = Double.POSITIVE_INFINITY;
		
		for(Set<RandomVariable> parentCandidate : parentCandidatePowerSet) {
			double mdl = MDL.mdl(X, parentCandidate, dataset);
			if(mdl < lowest) lowest = mdl;
		}
		
		return lowest;
	}
	
	public static HashSet<HashSet<RandomVariable>> powerset(Set<RandomVariable> list, int max) {
		Log.log("AT: ", list);
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
	
	
	public static Set<RandomVariable> calculateBestParentSet(RandomVariable X, Set<RandomVariable> parentCandidates, ADTree dataset) {
		Log.log("AT: ", X);
		HashSet<HashSet<RandomVariable>> parentCandidatePowerSet = powerset(parentCandidates, calcMax(dataset.size()));
		if(!parentCandidates.isEmpty()) parentCandidatePowerSet.remove(new HashSet<RandomVariable>());
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
	
	public static double mdl(RandomVariable X, Set<RandomVariable> parentsOfX, ADTree dataset) {
		Log.log("AT: ", X);
		double result = 0d;
		result += mdlh( X, parentsOfX, dataset ) + ( (log2(dataset.size()) / 2) * mdlk(X, parentsOfX) );
		
		return result;
	}
	
	public static double mdlh(RandomVariable X, Set<RandomVariable> parentsOfX, ADTree dataset) {
		Log.log("AT: ", X);
		Set<RandomVariable> randomVariables = new LinkedHashSet<RandomVariable>(parentsOfX);
		randomVariables.add(X);
		
		List<Map<RandomVariable, String>> constraintsList = RandomVariable.combineVariables(randomVariables);
		
		double sum = 0d;
		
		for(Map<RandomVariable, String> constraints : constraintsList) {
			Map<RandomVariable, String> parentConstraints = new HashMap<RandomVariable, String>(constraints);
			parentConstraints.remove(X);
			
			int Nxipai = dataset.query(constraints);
			int Npai = dataset.query(parentConstraints);
			if(Nxipai > 0) sum += Nxipai * (log2( Nxipai ) - log2(Npai));
		}
		
		return (sum * -1.0);
	}
	public static double mdlk(RandomVariable X, Set<RandomVariable> parentsOfX) {
		Log.log("AT: ", X);
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
