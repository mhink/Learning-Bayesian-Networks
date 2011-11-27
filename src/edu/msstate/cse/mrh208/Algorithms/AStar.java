package edu.msstate.cse.mrh208.Algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import edu.msstate.cse.mrh208.BNPriorityQueue;

import edu.msstate.cse.mrh208.BNSearchNode;
import edu.msstate.cse.mrh208.Bayes.BayesianNetwork;


public class AStar {
	
	public static BNSearchNode Search(BNSearchNode start, BayesianNetwork goal) {
		HashSet			<BNSearchNode> 		closedSet 	= new HashSet<BNSearchNode>();
		BNPriorityQueue	/*<BNSearchNode>*/ 	openSet 	= new BNPriorityQueue();
		
		openSet.add(start);
		
		while(!openSet.isEmpty()) {
			BNSearchNode currentNode = openSet.remove();
			
			if(currentNode.isGoal(goal)) {
				return currentNode;
			}
			
			currentNode.expand();
			closedSet.add(currentNode);
			
			for(BNSearchNode s : currentNode.getSuccessors()) {
				if(closedSet.contains(s)) continue;
				
				//This is problematic.
				BNSearchNode openNode = openSet.findAndRemoveIfEquals(s);
				openSet.add(BNSearchNode.bestPathCost(s, openNode));
			}
		}
		
		return null;
	}
}
