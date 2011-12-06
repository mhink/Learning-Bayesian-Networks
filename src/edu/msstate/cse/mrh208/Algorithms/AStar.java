package edu.msstate.cse.mrh208.Algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.msstate.cse.mrh208.Bayes.BayesianNetwork;
import edu.msstate.cse.mrh208.Bayes.RandomVariable;
import edu.msstate.cse.mrh208.Loggable;


public class AStar extends Loggable{
	HashSet<BNSearchNode>	closedSet;
	ArrayList<BNSearchNode> openSet;
	BNSearchNode 			currentNode;
	BNSearchNode			openNode;
	BNSearchNode			s;
	
	public BayesianNetwork Search(BayesianNetwork initialNetwork) throws Exception{
		closedSet 	= new HashSet<BNSearchNode>();
		openSet 	= new ArrayList<BNSearchNode>();
		
		openSet.add(new BNSearchNode(initialNetwork));
		
		while(!openSet.isEmpty()) {
			Collections.sort(openSet, new Comparator<BNSearchNode>(){
				@Override
				public int compare(BNSearchNode o1, BNSearchNode o2) {
					if(o1.estimatedTotalCost() < o2.estimatedTotalCost()) return -1;
					if(o1.estimatedTotalCost() == o2.estimatedTotalCost()) return 0;
					if(o1.estimatedTotalCost() > o2.estimatedTotalCost()) return 1;
					return 0;
				}
			});
			System.out.println(this);
			Scanner sc = new Scanner(System.in);
			sc.nextLine();
			
			currentNode = openSet.get(0);
			openSet.remove(0);
			
			if(currentNode.isGoal()) {
				return currentNode.bayesianNetwork;
			}
			
			currentNode.expand();
			closedSet.add(currentNode);
			
			for(BNSearchNode s : currentNode.getSuccessors()) { this.s = s;
				if(closedSet.contains(s)) continue;
				
				openNode = null;
				
				if(openSet.indexOf(s) != -1)
					openNode = openSet.get(openSet.indexOf(s));
				
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
			if(s != null) {
				sb.append(newline(3)).append("  S:").append(s.toShortString(-1)).append("EHC: " + s.estimatedTotalCost());
				for(RandomVariable rv : s.bayesianNetwork.variablesInNetwork)
					sb.append(rv.toShortString(5));
			}
			else sb.append(newline(3)).append("  S: null");
			
			sb.append(newline(0));
			
			if(this.currentNode.successors == null) 
				sb.append("null");
			else {
				for(BNSearchNode succ : this.currentNode.successors) {
					sb.append(succ.toShortString(4)).append("EHC: " + succ.estimatedTotalCost());
					for(RandomVariable rv : succ.bayesianNetwork.variablesInNetwork)
						sb.append(rv.toShortString(5));
				}
			}
		}
		
		sb.append("\n" + newline(1)).append("OPEN NODE");
		if(openNode == null)
			sb.append(newline(2)).append("null");
		else {
			sb.append(openNode.toShortString(2));
			if(openNode.randomVariable != null) 
				sb.append(" " + openNode.randomVariable.name);
			else sb.append("no variable");
		}
		
		sb.append("\n" + newline(1)).append("OPEN SET\t");
		//sb.append(Loggable.toString(openSet, -1));
		if(openSet.isEmpty()) 
			sb.append(newline(2)).append("NONE");
		else 
			for(BNSearchNode bnsn : openSet) {
				sb.append(bnsn.toShortString(4)).append("EHC: " + bnsn.estimatedTotalCost());
				for(RandomVariable rv : bnsn.bayesianNetwork.variablesInNetwork)
					sb.append(rv.toShortString(5));
			}
		
		sb.append("\n" + newline(1)).append("CLOSED SET\t");
		//sb.append(Loggable.toString(closedSet, -1));
		if(closedSet.isEmpty()) sb.append(newline(2)).append("NONE");
		else for(BNSearchNode bnsn : closedSet) {
			sb.append(bnsn.toShortString(2));
		}
		
		return sb.toString();
	}
}
