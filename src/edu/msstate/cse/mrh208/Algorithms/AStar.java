package edu.msstate.cse.mrh208.Algorithms;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;

import edu.msstate.cse.mrh208.Bayes.BayesianNetwork;
import edu.msstate.cse.mrh208.Bayes.RandomVariable;


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
		
		while(!openSet.isEmpty()) {
			currentNode = Collections.min(openSet.values(), BayesianNetwork.heuristicComparator);
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
		} 
		
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("AStar @ " + hexHashString(this));
		sb.append("\n\t" + "CURRENT NODE");
		sb.append("\n\t\t" + "BayesianNetwork @ " + hexHashString(this.currentNode));
		sb.append("\n\t\t" + "Variables In Network");
		for(RandomVariable in : this.currentNode.variablesInNetwork) {
			sb.append("\n\t\t\t" + in.toShortString() + "(");
			for(RandomVariable p : in.parents) {
				sb.append(p.name + " ");
			}
			sb.append(")");
		}
		sb.append("\n\t\t" + "Variables Not In Network");
		for(RandomVariable out : this.currentNode.variablesNotInNetwork) {
			sb.append("\n\t\t\t" + out.toShortString() + "(");
			for(RandomVariable p : out.parents) {
				sb.append(p.name + " ");
			}
			sb.append(")");
		}
		sb.append("\n\t\t" + "Path Cost To Here");
		sb.append("\n\t\t\t" + this.currentNode.pathCost);
		sb.append("\n\t\t" + "Estimated Cost From Here");
		sb.append("\n\t\t\t" + this.currentNode.heuristic);
		sb.append("\n\t\t" + "Total");
		sb.append("\n\t\t\t" + (this.currentNode.pathCost + this.currentNode.heuristic));
		sb.append("\n\t\t" + "Successors");
		for(BayesianNetwork successor : this.currentNode.successors) {
			sb.append("\n\t\t\t" + "BayesianNetwork @ " + hexHashString(successor) + "(");
			for(RandomVariable variable : successor.variablesInNetwork) {
				sb.append(variable.name + " ");
			}
			sb.append(")(" + (successor.heuristic + successor.pathCost) + ")");
		}
		sb.append("\n\tOPEN SET");
		for(BayesianNetwork openNode : this.openSet.values()) {
			sb.append("\n\t\t\t" + "BayesianNetwork @ " + hexHashString(openNode) + "(");
			for(RandomVariable variable : openNode.variablesInNetwork) {
				sb.append(variable.name + " ");
			}
			sb.append(")(" + (openNode.heuristic + openNode.pathCost) + ")");
		}
		sb.append("\n\tCLOSED SET");
		for(BayesianNetwork closedNode : this.closedSet) {
			sb.append("\n\t\t\t" + "BayesianNetwork @ " + hexHashString(closedNode) + "(");
			for(RandomVariable variable : closedNode.variablesInNetwork) {
				sb.append(variable.name + " ");
			}
			sb.append(")(" + (closedNode.heuristic + closedNode.pathCost) + ")");
		}
		
		
		return sb.toString();
	}
	
	public static String hexHashString(Object obj) {
		return Integer.toHexString(obj.hashCode());
	}
	
	
}
