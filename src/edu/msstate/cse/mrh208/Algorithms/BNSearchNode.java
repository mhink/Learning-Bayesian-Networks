package edu.msstate.cse.mrh208.Algorithms;

import java.util.HashSet;
import java.util.Set;

import edu.msstate.cse.mrh208.Bayes.BayesianNetwork;
import edu.msstate.cse.mrh208.Bayes.RandomVariable;
import edu.msstate.cse.mrh208.Loggable;

//NOTE: The search graph is an order graph of variables.
public class BNSearchNode extends Loggable {
	
	BNSearchNode 		parent;
	BayesianNetwork 	bayesianNetwork;
	double 				pathCost;
	double 				heuristicValue;
	RandomVariable		randomVariable;
	Set<BNSearchNode> 	successors;
	
	@Override
	public boolean equals(Object aThat) {
		if(aThat == null) return false;
		if(!(aThat instanceof BNSearchNode)) return false;
		if(this == aThat) return true;
		
		BNSearchNode that = (BNSearchNode) aThat;
		if(this.bayesianNetwork.variablesInNetwork.equals(that.bayesianNetwork.variablesInNetwork)) return true;
		return false;
	}
	
	public BNSearchNode(BayesianNetwork bayesianNetwork) {
		this.parent 			= null;
		this.bayesianNetwork 	= bayesianNetwork.clone();
		this.pathCost			= 0;
		this.heuristicValue 	= 0;
		this.randomVariable		= new RandomVariable("DNE");
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
		this.randomVariable.parents = new HashSet<RandomVariable>(this.bayesianNetwork.bestParentSet(X));
		this.successors = new HashSet<BNSearchNode>();
	}
	
	public double estimatedTotalCost() {
		return this.pathCost + this.heuristicValue;
	}
	
	public boolean isGoal() {
		if(this.bayesianNetwork.variablesInNetwork.equals(BayesianNetwork.goalNetwork.variablesInNetwork)) return true;
		return false;
	}
	
	public void expand() throws Exception{
		for(RandomVariable X : bayesianNetwork.variablesNotInNetwork) {
			BNSearchNode successor = new BNSearchNode(this, X, bayesianNetwork, pathCost);
			successors.add(successor);
		}
		
		for(BNSearchNode successor : successors) {
			int sVIN 	= successor .bayesianNetwork.variablesInNetwork.size();
			int sVNIN 	= successor .bayesianNetwork.variablesNotInNetwork.size();
			int tVIN 	= this		.bayesianNetwork.variablesInNetwork.size();
			int tVNIN 	= this		.bayesianNetwork.variablesNotInNetwork.size();
			if( sVIN + sVNIN != tVIN + tVNIN)
				throw new Exception("A successor hasn't done its variables right.");
				
		}
	}
	
	public Set<BNSearchNode> getSuccessors() throws Exception{
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
		sb.append(newline(tabDepth + 1)).append("Estimate:\t" + Double.toString(this.estimatedTotalCost()));
		sb.append(newline(tabDepth + 1)).append("Variable:\t");
		if(randomVariable != null) sb.append(randomVariable.toShortString(-1));
		else sb.append("null");
		sb.append(newline(tabDepth + 1)).append("Successors:\t");
		
		return sb.toString();
	}
}
