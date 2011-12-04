package edu.msstate.cse.mrh208.Algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import edu.msstate.cse.mrh208.Bayes.BayesianNetwork;
import edu.msstate.cse.mrh208.Bayes.RandomVariable;

//NOTE: The search graph is an order graph of variables.
public class BNSearchNode implements Comparable<BNSearchNode>{
	
	BNSearchNode 		parent;
	BayesianNetwork 	bayesianNetwork;
	double 				pathCost;
	double 				heuristicValue;
	Set<BNSearchNode> 	successors;
	
	
	@Override
	public boolean equals(Object aThat) {
		if(this == aThat) return true;
		if( !(aThat instanceof BNSearchNode) ) return false;
		BNSearchNode that = (BNSearchNode)aThat;
		
		return this.bayesianNetwork.equals(that.bayesianNetwork);
	}
	
	@Override
	public int hashCode() {
		//TODO: Figure out a hashing function for BNSearchNodes. (Maybe based on underlying BN hashcodes?)
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int compareTo(BNSearchNode arg0) {
		if(this.estimatedTotalCost() < arg0.estimatedTotalCost()) return -1;
		if(this.estimatedTotalCost() == arg0.estimatedTotalCost()) return 0;
		if(this.estimatedTotalCost() > arg0.estimatedTotalCost()) return 1;
		
		return 0;
	}
	
	public BNSearchNode(BayesianNetwork bayesianNetwork) {
		this.parent = null;
		this.bayesianNetwork = bayesianNetwork.clone();
		this.pathCost = 0;
		this.heuristicValue = 0;
	}
	
	public BNSearchNode(BNSearchNode parent, RandomVariable X, BayesianNetwork bayesianNetwork, double parentPathCost) {
		this.parent = parent;
		this.bayesianNetwork 	= bayesianNetwork.clone();
		this.bayesianNetwork.variablesNotInNetwork	.remove	(X);
		this.bayesianNetwork.variablesInNetwork		.add	(X);
		
		this.pathCost 			= parentPathCost + bayesianNetwork.pathCost(X, parent.bayesianNetwork.variablesNotInNetwork);
		
		this.heuristicValue 	= bayesianNetwork.heuristic(X);
	}
	
	public double estimatedTotalCost() {
		return this.pathCost + this.heuristicValue;
	}
	
	public boolean isGoal() {
		return this.bayesianNetwork.equals(BayesianNetwork.goalNetwork);
	}
	
	public void expand() {
		successors = new HashSet<BNSearchNode>();
		for(RandomVariable X : this.bayesianNetwork.variablesNotInNetwork) {
			successors.add(new BNSearchNode(this, X, this.bayesianNetwork, this.pathCost));			
		}
	}
	
	public Set<BNSearchNode> getSuccessors() {
		if(successors == null) this.expand();
		return successors;
	}
	
	public static BNSearchNode bestPathCost(BNSearchNode bnsn1, BNSearchNode bnsn2) {
		if(bnsn1 == null && bnsn2 != null) return bnsn2;
		if(bnsn1 != null && bnsn2 == null) return bnsn1;
		
		if(bnsn1.pathCost < bnsn2.pathCost) return bnsn1;
		if(bnsn1.pathCost > bnsn2.pathCost) return bnsn2;
		if(bnsn1.pathCost == bnsn2.pathCost)return bnsn2;
		
		return null;
	}

}
