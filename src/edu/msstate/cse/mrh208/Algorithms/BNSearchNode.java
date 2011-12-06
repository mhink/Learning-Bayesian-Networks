package edu.msstate.cse.mrh208.Algorithms;

import java.util.HashSet;
import java.util.Set;

import edu.msstate.cse.mrh208.Bayes.BayesianNetwork;
import edu.msstate.cse.mrh208.Bayes.RandomVariable;
import edu.msstate.cse.mrh208.Loggable;

//NOTE: The search graph is an order graph of variables.
public class BNSearchNode extends Loggable implements Comparable<BNSearchNode>{
	
	BNSearchNode 		parent;
	BayesianNetwork 	bayesianNetwork;
	double 				pathCost;
	double 				heuristicValue;
	Set<BNSearchNode> 	successors;
	RandomVariable		randomVariable;
	
	
	@Override
	public boolean equals(Object aThat) {
		if(this == aThat) return true;
		if( !(aThat instanceof BNSearchNode) ) return false;
		BNSearchNode that = (BNSearchNode)aThat;
		
		return this.bayesianNetwork.equals(that.bayesianNetwork);
	}
	
	@Override
	public int hashCode() {
		//TODO: make sure this actually works as intended.
		//In other words, ensure that
		//BNSN1 == BNSN2 => BNSN1.hash == BNSN3.hash
		int hash = 1;
		hash = hash * 31 + bayesianNetwork.hashCode();
		hash = hash * 31 + Double.valueOf(pathCost).hashCode();
		hash = hash * 31 + Double.valueOf(heuristicValue).hashCode();
		
		return hash;
	}
	
	@Override
	public int compareTo(BNSearchNode arg0) {
		if(this.estimatedTotalCost() < arg0.estimatedTotalCost()) return -1;
		if(this.estimatedTotalCost() == arg0.estimatedTotalCost()) return 0;
		if(this.estimatedTotalCost() > arg0.estimatedTotalCost()) return 1;
		
		return 0;
	}
	
	public BNSearchNode(BayesianNetwork bayesianNetwork) {
		this.parent 			= null;
		this.bayesianNetwork 	= bayesianNetwork.clone();
		this.pathCost			= 0;
		this.heuristicValue 	= 0;
	}
	
	public BNSearchNode(BNSearchNode parent, RandomVariable X, BayesianNetwork bayesianNetwork, double parentPathCost) {
		this.parent = parent;
		this.randomVariable 	= X.copyWithoutParents();
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
		for(RandomVariable X : bayesianNetwork.variablesNotInNetwork) {
			successors.add(new BNSearchNode(this, X, bayesianNetwork, pathCost));			
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
	
	@Override
	public String toString() {
		return this.toString(0);
	}
	
	@Override
	public String toString(int tabDepth) {
		StringBuilder sb = new StringBuilder(super.toString(tabDepth));
		sb.append(this.bayesianNetwork.toString(tabDepth + 1));
		sb.append(newline(tabDepth + 1)).append("Heuristic:\t" + Double.toString(this.heuristicValue));
		sb.append(newline(tabDepth + 1)).append("Path cost:\t" + Double.toString(this.pathCost));
		sb.append(newline(tabDepth + 1)).append("Variable:\t");
		if(randomVariable != null) sb.append(randomVariable.toShortString(-1));
		else sb.append("null");
		
		return sb.toString();
	}

}
