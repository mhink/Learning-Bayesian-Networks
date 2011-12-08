package edu.msstate.cse.mrh208.Algorithms;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;

import edu.msstate.cse.mrh208.Bayes.BayesianNetwork;


public class AStar {
	HashSet<BayesianNetwork>				closedSet;
	LinkedHashMap<Integer, BayesianNetwork>	openSet;
	BayesianNetwork 						currentNode;
	BayesianNetwork							successor;
	
	public BayesianNetwork Search(BayesianNetwork initialNetwork) throws Exception{
		closedSet 	= new HashSet<BayesianNetwork>();
		openSet 	= new LinkedHashMap<Integer, BayesianNetwork>();
		
		openSet.put(initialNetwork.inVarHash(), initialNetwork);
		currentNode = initialNetwork;
		
		do {
			openSet.remove(currentNode.inVarHash());
			
			if(currentNode.isGoal()) 
				return currentNode;
			
			currentNode.expand();
			closedSet.add(currentNode);
			
			for(BayesianNetwork s : currentNode.successors) {
				if(closedSet.contains(s)) continue;
				
				BayesianNetwork openNode = BayesianNetwork.bestPathCost(s, openSet.get(s.inVarHash()));
				openSet.put(openNode.inVarHash(), openNode);
			}
			
			currentNode = Collections.min(openSet.values(), BayesianNetwork.heuristicComparator);
			
		} while(!openSet.isEmpty());
		
		return null;
	}
	
	
}
