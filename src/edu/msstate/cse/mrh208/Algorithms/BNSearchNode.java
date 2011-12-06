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
	RandomVariable		randomVariable;
	Set<BNSearchNode> 	successors;
	
	public BNSearchNode(BayesianNetwork bayesianNetwork) {
		this.parent 			= null;
		this.bayesianNetwork 	= bayesianNetwork.clone();
		this.pathCost			= 0;
		this.heuristicValue 	= 0;
		this.randomVariable		= new RandomVariable("NONE");
		this.successors = new HashSet<BNSearchNode>();
	}
	
	public BNSearchNode(BNSearchNode parent, RandomVariable X, BayesianNetwork bayesianNetwork, double parentPathCost) {
		this.parent 			= parent;
		this.randomVariable 	= X.clone();
		this.bayesianNetwork 	= bayesianNetwork.clone();
			this.bayesianNetwork.variablesNotInNetwork	.remove	(X);
			this.bayesianNetwork.variablesInNetwork		.add	(this.randomVariable);
			
		this.pathCost 				= this.bayesianNetwork.pathCost(X, parent.bayesianNetwork.variablesInNetwork) + parentPathCost;
		this.heuristicValue 		= this.bayesianNetwork.heuristic(X);
		this.randomVariable.parents = this.bayesianNetwork.bestParentSet(X);
	}
	
	public double estimatedTotalCost() {
		return this.pathCost + this.heuristicValue;
	}
	
	public boolean isGoal() {
		return this.bayesianNetwork.equals(BayesianNetwork.goalNetwork);
	}
	
	public void expand() {
		for(RandomVariable X : bayesianNetwork.variablesNotInNetwork) {
			BNSearchNode successor = new BNSearchNode(this, X, bayesianNetwork, pathCost);
			successors.add(successor);		
		}
	}
	
	public Set<BNSearchNode> getSuccessors() {
		if(successors.isEmpty()) this.expand();
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
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(super.toShortString(tabDepth));
		}
		catch(NullPointerException ex) { sb.append("BNSearchNode@---"); }
		
		if(this.bayesianNetwork == null) sb.append(newline(tabDepth+1)).append("NULL");
		else sb.append(this.bayesianNetwork.toString(tabDepth + 1));
		sb.append(newline(tabDepth + 1)).append("Heuristic:\t" + Double.toString(this.heuristicValue));
		sb.append(newline(tabDepth + 1)).append("Path cost:\t" + Double.toString(this.pathCost));
		sb.append(newline(tabDepth + 1)).append("Variable:\t");
		if(randomVariable != null) sb.append(randomVariable.toShortString(-1));
		else sb.append("null");
		sb.append(newline(tabDepth + 1)).append("Successors:\t");
		
		return sb.toString();
	}

	//NOTE:
	//This comparator imposes an ordering that is inconsistent
	//with equals()!!!
	@Override
	public int compareTo(BNSearchNode arg0) {
		if(this.estimatedTotalCost() < arg0.estimatedTotalCost()) return -1;
		if(this.estimatedTotalCost() == arg0.estimatedTotalCost()) return 0;
		if(this.estimatedTotalCost() > arg0.estimatedTotalCost()) return 1;
		
		return 0;
	}

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
		
		
		return hash;
	}

}
