package edu.msstate.cse.mrh208.Algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

import edu.msstate.cse.mrh208.BNPriorityQueue;

import edu.msstate.cse.mrh208.Bayes.BayesianNetwork;


public class AStar {
	
	public static BayesianNetwork Search(BayesianNetwork initialNetwork) {
		HashSet			<BNSearchNode> 		closedSet 	= new HashSet<BNSearchNode>();
		TreeSet		    <BNSearchNode>   	openSet 	= new TreeSet<BNSearchNode>();
		
		openSet.add(new BNSearchNode(initialNetwork));
		
		while(!openSet.isEmpty()) {
			BNSearchNode currentNode = openSet.pollFirst();
			
			if(currentNode.isGoal()) {
				return currentNode.bayesianNetwork;
			}
			
			currentNode.expand();
			closedSet.add(currentNode);
			
			for(BNSearchNode s : currentNode.getSuccessors()) {
				if(closedSet.contains(s)) continue;
				
				BNSearchNode openNode = null;
				
				//The key here is that if the openSet contains an element equal to s, then
				//openSet.ceiling(s) will return that element.  Could probably also do floor(s).
				if(openSet.contains(s)) openNode = openSet.ceiling(s);
				openSet.add(BNSearchNode.bestPathCost(s, openNode));
			}
		}
		
		return null;
	}
}
