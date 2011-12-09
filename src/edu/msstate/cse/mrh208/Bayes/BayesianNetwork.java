package edu.msstate.cse.mrh208.Bayes;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.msstate.cse.mrh208.ADTree;
import edu.msstate.cse.mrh208.Log;
import edu.msstate.cse.mrh208.Algorithms.AStar;
import edu.msstate.cse.mrh208.Algorithms.MDL;

public class BayesianNetwork {

	public static BayesianNetwork	goalNetwork;
	public BayesianNetwork			parent;
	public ADTree					data;
	public RandomVariable			variable;
	public Set<RandomVariable>		variablesInNetwork;
	public Set<RandomVariable>		variablesNotInNetwork;
	public Set<BayesianNetwork>		successors;

	public double					pathCost;
	public double					heuristic;
	
	public String toShortString() {
		Log.log("AT: ", this);
		StringBuilder sb = new StringBuilder();
		sb.append(Integer.toHexString(this.hashCode()));
		return sb.toString();
	}
	
	private BayesianNetwork(ADTree data) {
		Log.log("AT: ", this);
		this.parent					= null;
		this.variable				= new RandomVariable("DNE", new HashSet<String>());	
		this.data					= data;
		this.variablesInNetwork 	= new LinkedHashSet<RandomVariable>();
		this.variablesNotInNetwork 	= new LinkedHashSet<RandomVariable>(data.getVariables());
		this.successors				= new LinkedHashSet<BayesianNetwork>();
		
		this.heuristic				= Double.POSITIVE_INFINITY;
		this.pathCost				= 0.0;
	}

	public static BayesianNetwork learnBayesianNetwork(ADTree dataset) throws Exception {
		BayesianNetwork bayesianNetwork 		= new BayesianNetwork(dataset);
		BayesianNetwork.goalNetwork				= new BayesianNetwork(dataset);
		
		BayesianNetwork.goalNetwork.variablesInNetwork 		= new LinkedHashSet<RandomVariable>(dataset.getVariables());
		BayesianNetwork.goalNetwork.variablesNotInNetwork 	= new LinkedHashSet<RandomVariable>();
		
		BayesianNetwork result = (new AStar()).Search(bayesianNetwork);
		return result;
	}

	private BayesianNetwork(RandomVariable X, BayesianNetwork parent) {
		Log.log("AT: ", this);
		this.parent					= parent;
		this.variable				= X;	
		this.data 					= parent.data;
		this.variablesInNetwork 	= new LinkedHashSet<RandomVariable>(parent.variablesInNetwork);
		this.variablesNotInNetwork 	= new LinkedHashSet<RandomVariable>(parent.variablesNotInNetwork);
		this.successors				= new LinkedHashSet<BayesianNetwork>();
		
		this.variablesNotInNetwork.remove(this.variable);
		
		generateVariableParents();
		this.heuristic				= this.heuristic();
		this.pathCost				= parent.pathCost + this.pathCost();
		
		this.variablesInNetwork.add(X);
	}
	
	private double heuristic() {
		Log.log("AT: ", this);
		return MDL.heuristic(variablesNotInNetwork, data);
	}
	
	private double pathCost() {
		Log.log("AT: ", this);
		return MDL.bestMDL(variable, variablesInNetwork, data);
	}
	
	private void generateVariableParents() {
		Log.log("AT: ", this);
		variable.parents.addAll(MDL.calculateBestParentSet(variable, variablesInNetwork, data));
	}
	
	public void expand() {
		Log.log("AT: ", this);
		for(RandomVariable rv : variablesNotInNetwork) {
			successors.add(new BayesianNetwork(rv, this));
		}
	}
	
	public static BayesianNetwork bestPathCost(BayesianNetwork bnsn1, BayesianNetwork bnsn2) {
		if(bnsn1 == null && bnsn2 != null) return bnsn2;
		if(bnsn1 != null && bnsn2 == null) return bnsn1;
		
		if(bnsn1.pathCost < bnsn2.pathCost) return bnsn1;
		if(bnsn1.pathCost > bnsn2.pathCost) return bnsn2;
		if(bnsn1.pathCost == bnsn2.pathCost)return bnsn2;
		
		return null;
	}
	
	public int inVarHash() {
		return this.variablesInNetwork.hashCode();
	}
	
	public boolean isGoal() {
		return this.variablesInNetwork.equals(BayesianNetwork.goalNetwork.variablesInNetwork);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((variablesInNetwork == null) ? 0 : variablesInNetwork
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof BayesianNetwork))
			return false;
		BayesianNetwork other = (BayesianNetwork) obj;
		if (variablesInNetwork == null) {
			if (other.variablesInNetwork != null)
				return false;
		} else if (!variablesInNetwork.equals(other.variablesInNetwork))
			return false;
		return true;
	}
	
	public static Comparator<BayesianNetwork> heuristicComparator = new Comparator<BayesianNetwork>() {
		@Override
		public int compare(BayesianNetwork o1, BayesianNetwork o2) {
			double etc1 = o1.pathCost + o1.heuristic;
			double etc2 = o2.pathCost + o2.heuristic;
			
			if(etc1 >  etc2) return -1;
			if(etc1 == etc2) return 0;
			if(etc1 <  etc2) return 1;
			return 0;
		}
	};

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BayesianNetwork\n\tvariablesInNetwork=");
		for(RandomVariable in : this.variablesInNetwork) {
			builder.append("\n\t\t" + in.name + "\t(");
			for(RandomVariable parent : in.parents) 
				builder.append(parent.name + " ");
			builder.append(")");
		}
		builder.append("\n\tpathCost=");
		builder.append(pathCost);
		builder.append("\n\theuristic=");
		builder.append(heuristic);
		return builder.toString();
	}
}
