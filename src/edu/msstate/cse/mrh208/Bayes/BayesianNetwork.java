package edu.msstate.cse.mrh208.Bayes;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.msstate.cse.mrh208.Dataset;
import edu.msstate.cse.mrh208.Algorithms.AStar;
import edu.msstate.cse.mrh208.Algorithms.MDL;

public class BayesianNetwork {

	public static BayesianNetwork	goalNetwork;
	public BayesianNetwork			parent;
	public Dataset					dataset;
	public RandomVariable			variableAdded;
	public Set<RandomVariable>		variablesInNetwork;
	public Set<RandomVariable>		variablesNotInNetwork;
	public Set<BayesianNetwork>		successors;

	public double					pathCost;
	public double					heuristic;
	
	private BayesianNetwork(Dataset dataset) {
		this.dataset = dataset;
		variablesInNetwork 		= new LinkedHashSet<RandomVariable>();
		variablesNotInNetwork 	= new LinkedHashSet<RandomVariable>(dataset.getVariables());
	}

	public static BayesianNetwork learnBayesianNetwork(Dataset dataset) throws Exception {
		BayesianNetwork bayesianNetwork 		= new BayesianNetwork(dataset);
		BayesianNetwork.goalNetwork				= new BayesianNetwork(dataset);
		
		BayesianNetwork.goalNetwork.variablesInNetwork 		= new LinkedHashSet<RandomVariable>(dataset.getVariables());
		BayesianNetwork.goalNetwork.variablesNotInNetwork 	= new LinkedHashSet<RandomVariable>();
		
		BayesianNetwork result = (new AStar()).Search(bayesianNetwork);
		return result;
	}

	private BayesianNetwork(RandomVariable X, BayesianNetwork parent) {
		this.parent					= parent;
		this.variableAdded			= X;
		this.dataset 				= parent.dataset;
		this.variablesInNetwork 	= new LinkedHashSet<RandomVariable>(parent.variablesInNetwork);
		this.variablesNotInNetwork 	= new LinkedHashSet<RandomVariable>(parent.variablesNotInNetwork);
		this.successors				= new LinkedHashSet<BayesianNetwork>();
		
		this.heuristic				= MDL.calculateHeuristic(this.variableAdded, this.variablesNotInNetwork, this.dataset);
		this.pathCost				= parent.pathCost + MDL.bestMDL(this.variableAdded, parent.variablesInNetwork, this.dataset);
		
		this.variablesInNetwork.add(variableAdded);
		this.variablesNotInNetwork.remove(variableAdded);
	}
	
	public void expand() {
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
			
			if(etc1 <  etc2) return -1;
			if(etc1 == etc2) return 0;
			if(etc1 >  etc2) return 1;
			return 0;
		}
	};
}
