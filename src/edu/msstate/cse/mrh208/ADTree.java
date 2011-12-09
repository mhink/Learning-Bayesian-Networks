package edu.msstate.cse.mrh208;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.TreeBasedTable;

import edu.msstate.cse.mrh208.Bayes.RandomVariable;

public class ADTree {
	private class ADNode {
		boolean opened;
		int count;
		Map<RandomVariable, VaryNode> children;
		
		private Set<VaryNode> childrenRaw;
		
		public ADNode(Set<RandomVariable> variables, Set<Map<RandomVariable, String>> entries) {
			Log.log("AT: ", this);
			children = new HashMap<RandomVariable, VaryNode>();
			count = entries.size();
			for(RandomVariable variable : variables) {
				children.put(variable, null);
			}
		}
		
		public void expand(RandomVariable toExpand, Set<Map<RandomVariable, String>> entries) {
			Log.log("AT: " + toExpand.toShortString(), this);
			
			children.put(toExpand, new VaryNode(toExpand, variables, entries));
			
			childrenRaw = new HashSet<VaryNode>(children.values());
		}
	}
	private class VaryNode {
		Map<String, ADNode> children;
		Set<ADNode> childrenRaw;
		
		public Set<Map<RandomVariable, String>> expand(RandomVariable variable, String state, Set<Map<RandomVariable, String>> entries) {
			Log.log("AT: " + variable.toShortString() + ", " + state, this);
			Set<Map<RandomVariable, String>> partition = new HashSet<Map<RandomVariable, String>>();
			Set<RandomVariable> newVars = new HashSet<RandomVariable>(variables);
			newVars.remove(variable);
			
			for(Map<RandomVariable, String> entry : entries) {
				String thisstate = entry.get(variable);
				
				if(thisstate.equals(state)) partition.add(entry);
			}
			
			children.put(state, new ADNode(newVars, partition));
			
			childrenRaw = new HashSet<ADNode>(children.values());
			
			return partition;
		}
		
		public VaryNode(RandomVariable variable, Set<RandomVariable> variables, Set<Map<RandomVariable, String>> entries) {
			Log.log("AT: ", this);
			this.children = new HashMap<String, ADNode>();
			for(String str : variable.states) {
				this.children.put(str, null);
			}
			childrenRaw = new HashSet<ADNode>(children.values());
		}
	}
	
	private ADNode root;
	private Set<RandomVariable> variables;
	private Set<Map<RandomVariable, String>> entries;
	private Map<Map<RandomVariable, String>, ADNode> instantiations;
	
	public int size() {
		return entries.size();
	}
	
	public Set<RandomVariable> getVariables() {
		return this.variables;
	}
	
	public ADTree(List<RandomVariable> randomVariables, List<List<String>> entries) {
		Log.log("AT: ", this);
		this.instantiations = new HashMap<Map<RandomVariable, String>, ADNode>();
		this.variables = new HashSet<RandomVariable>(randomVariables);
		Set<Map<RandomVariable, String>> entrySet = new HashSet<Map<RandomVariable, String>>();
		for(List<String> entry : entries) {
			Iterator<RandomVariable> it1 = randomVariables.iterator();
			Iterator<String> it2 = entry.iterator();
			Map<RandomVariable, String> entryMap = new HashMap<RandomVariable, String>();
			while(it1.hasNext() && it2.hasNext()) {
				entryMap.put(it1.next(), it2.next());
			}
			entrySet.add(entryMap);
		}
		this.entries = entrySet;
		
		root = new ADNode(this.variables, entrySet);
	}
	
	public int query(Map<RandomVariable, String> query) {
		Log.log("AT: ", this);
		return queryCount(query, root, entries);
	}
	
	private int queryCount(Map<RandomVariable, String> currentQuery, ADNode currentNode, Set<Map<RandomVariable, String>> entries) {
		Log.log("AT: ", this);
		if(currentQuery.isEmpty()) return currentNode.count;
		if(this.instantiations.containsKey(currentQuery)) return this.instantiations.get(currentQuery).count;
		
		RandomVariable queryVariable = null;
		String queryState = null;
		Map<RandomVariable, String> nextQuery = new HashMap<RandomVariable, String>(currentQuery);
		
		Iterator<Map.Entry<RandomVariable, String>> it1 = currentQuery.entrySet().iterator();
		while(it1.hasNext()) {
			Map.Entry<RandomVariable, String> pair = it1.next();
			queryVariable = pair.getKey();
			queryState = pair.getValue();
			nextQuery.remove(queryVariable);
			if(queryState != null) break;
		}
		
		if(currentNode.children.get(queryVariable)==null) currentNode.expand(queryVariable, entries);
		VaryNode varyNode = currentNode.children.get(queryVariable);
		entries = varyNode.expand(queryVariable, queryState, entries);
		ADNode nextADNode = varyNode.children.get(queryState);
		
		if(nextADNode.count == 0) return 0;
		
		int queryCount = queryCount(nextQuery, nextADNode, entries);
		
		instantiations.put(currentQuery, currentNode);
		return queryCount;
	}
}
