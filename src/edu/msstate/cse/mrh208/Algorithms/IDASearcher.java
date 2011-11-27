//package edu.msstate.cse.mrh208.Algorithms;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.List;
//
//import edu.msstate.cse.mrh208.BNSearchNode;
//
//
//public class IDASearcher {
//	
//	public int nodesExpanded = 0;
//	
//	private BNSearchNode bestNode;
//	private int limit;
//	private int bestContour;
//	private int bestNodeCost;
//	
//	public BNSearchNode IDASearch(BNSearchNode start) {
//		bestNode = null;
//		bestNodeCost = Integer.MAX_VALUE;
//		bestContour = Integer.MAX_VALUE;
//		limit = start.compvalue;
//		
//		ArrayList<BNSearchNode> path = new ArrayList<BNSearchNode>();
//		
//		while(bestNode == null) {
//			search(start, path);
//			limit = bestContour;
//			bestContour = Integer.MAX_VALUE;
//		}
//		
//		return bestNode;
//	}
//	
//public void search(BNSearchNode currentNode, List<BNSearchNode> pathToHere) {
//		
//		if(pathToHere.contains(currentNode)) {
//			return; //Don't loop
//		}
//		if(currentNode.costToHere + currentNode.heuristicValue() > limit) {
//			if(currentNode.costToHere + currentNode.heuristicValue() < bestContour) {
//				bestContour = currentNode.costToHere + currentNode.heuristicValue();
//			}
//			return; //Prune the tree
//			}
//		
//		if(currentNode.IsGoal() && currentNode.costToHere < bestNodeCost) 
//			{
//				bestNode = currentNode;
//				bestNodeCost = currentNode.costToHere;
//				return;
//			}
//		
//		pathToHere.add(currentNode); //add myself to "closed list"
//		
//		nodesExpanded++;
//		List<BNSearchNode> successors = currentNode.GenerateSuccessors();
//		java.util.Collections.sort(successors, new Comparator<BNSearchNode>(){
//			@Override
//			public int compare(BNSearchNode arg0, BNSearchNode arg1) {
//				if(arg0.costToHere + arg0.heuristicValue() < arg1.costToHere + arg1.heuristicValue()) return -1;
//				if(arg0.costToHere + arg0.heuristicValue() == arg1.costToHere + arg1.heuristicValue()) return 0;
//				if(arg0.costToHere + arg0.heuristicValue() > arg1.costToHere + arg1.heuristicValue()) return 1;
//				
//				return 0;
//			}
//		});
//		
//		for(BNSearchNode s : successors) {
//			search(s, pathToHere);
//		}
//		
//		pathToHere.remove(pathToHere.size()-1); //remove myself from "closed list"
//	}
//}
