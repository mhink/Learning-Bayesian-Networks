package edu.msstate.cse.mrh208.Algorithms;

import java.util.HashSet;
import java.util.TreeSet;

import edu.msstate.cse.mrh208.Bayes.BayesianNetwork;
import edu.msstate.cse.mrh208.Loggable;


public class AStar extends Loggable{
	HashSet<BNSearchNode>	closedSet;
	TreeSet<BNSearchNode> 	openSet;
	BNSearchNode 			currentNode;
	BNSearchNode			openNode;
	BNSearchNode			s;
	
	public BayesianNetwork Search(BayesianNetwork initialNetwork) {
		closedSet 	= new HashSet<BNSearchNode>();
		openSet 	= new TreeSet<BNSearchNode>();
		
		openSet.add(new BNSearchNode(initialNetwork));
		
		while(!openSet.isEmpty()) {
			currentNode = openSet.pollFirst();
			if(currentNode.isGoal()) {
				return currentNode.bayesianNetwork;
			}
			
			currentNode.expand();
			closedSet.add(currentNode);
			
			for(BNSearchNode s : currentNode.getSuccessors()) { this.s = s;
				if(closedSet.contains(s)) continue;
				
				openNode = null;
				
				//The key here is that if the openSet contains an element equal to s, then
				//openSet.ceiling(s) will return that element.  Could probably also do floor(s).
				if(openSet.contains(s)) openNode = openSet.ceiling(s);
				openSet.add(BNSearchNode.bestPathCost(s, openNode));
			}
			this.s = null;
			this.openNode = null;
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString(0));
		sb.append(newline(1)).append("CURRENT NODE");
		if(currentNode == null) 
			sb.append(newline(2)).append("null");
		else {
			sb.append(currentNode.toString(2));
			if(this.currentNode.successors.isEmpty()) 
				sb.append(" NONE");
			else 
				for(BNSearchNode succ : this.currentNode.successors)
					if(s != null && s.equals(succ)) sb.append(newline(3) + "   *").append(succ.toShortString(-1));
					else sb.append(newline(4)).append(succ.toShortString(-1));
		}
		
		sb.append("\n" + newline(1)).append("OPEN NODE");
		if(openNode == null)
			sb.append(newline(2)).append("null");
		else {
			sb.append(openNode.toString(2));
			if(openNode.randomVariable != null) 
				sb.append(" " + openNode.randomVariable.name);
			else sb.append("no variable");
		}
		
		sb.append("\n" + newline(1)).append("OPEN SET\t");
		//sb.append(Loggable.toString(openSet, -1));
		if(openSet.isEmpty()) sb.append(newline(2)).append("NONE");
		else for(BNSearchNode bnsn : openSet) {
			sb.append(bnsn.toString(2));
		}
		
		sb.append("\n" + newline(1)).append("CLOSED SET\t");
		//sb.append(Loggable.toString(closedSet, -1));
		if(closedSet.isEmpty()) sb.append(newline(2)).append("NONE");
		else for(BNSearchNode bnsn : closedSet) {
			sb.append(bnsn.toString(2));
		}
		
		return sb.toString();
	}
}
