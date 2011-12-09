package edu.msstate.cse.mrh208;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import edu.msstate.cse.mrh208.Algorithms.MDL;
import edu.msstate.cse.mrh208.Bayes.BayesianNetwork;
import edu.msstate.cse.mrh208.Bayes.RandomVariable;

public class Program {
	
	public static void main(String[] args) throws Exception {
		ADTree dataset;
		BayesianNetwork bayesianNetwork;
		
		System.out.println("Testing on 2variables, 2states...");
		dataset = testData(2, 2, 1000);
		bayesianNetwork = BayesianNetwork.learnBayesianNetwork(dataset);
		System.out.println(bayesianNetwork.toString());
		
		System.out.println("Testing on 3variables, 2states...");
		dataset = testData(3, 2, 1000);
		bayesianNetwork = BayesianNetwork.learnBayesianNetwork(dataset);
		System.out.println(bayesianNetwork.toString());
		
		System.out.println("Testing on 4variables, 3states...");
		dataset = testData(4, 3, 1000);
		bayesianNetwork = BayesianNetwork.learnBayesianNetwork(dataset);
		System.out.println(bayesianNetwork.toString());
		
		System.out.println("Testing on 5variables, 4states...");
		dataset = testData(5, 4, 1000);
		bayesianNetwork = BayesianNetwork.learnBayesianNetwork(dataset);
		System.out.println(bayesianNetwork.toString());
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter a number of variables, or 0 to quit");
		int numVars = sc.nextInt();
		if(numVars == 0) return;
		System.out.println("Enter a number of states per variable, or 0 to quit");
		int numStates = sc.nextInt();
		if(numStates == 0) return;
		
		System.out.println("Testing on "+numVars+"variables, "+numStates+"states...");
		dataset = testData(numVars, numStates, 1000);
		bayesianNetwork = BayesianNetwork.learnBayesianNetwork(dataset);
		System.out.println(bayesianNetwork.toString());
		
	}
	
	public static ADTree testData(int NUM_VARIABLES, int NUM_STATES,  int NUM_DATA) {
		List<RandomVariable> rvs = new ArrayList<RandomVariable>();
		
		for(int i = 0; i < NUM_VARIABLES; i++) {
			RandomVariable rv = new RandomVariable();
			rv.name = "rv" + i;
			for(int j = 0; j < NUM_STATES; j++) {
				rv.states.add(Integer.toString(j));
			}
			rvs.add(rv);
		}
		
		List<List<String>> data = new ArrayList<List<String>>();
		for(int i = 0; i < NUM_DATA; i++) {
			List<String> entry = new ArrayList<String>();
			for(RandomVariable rv : rvs) 
				entry.add(rv.randomState());
			data.add(entry);
		}
		
		return new ADTree(rvs, data);
	}
	
	public static ADTree fromData(String file) throws Exception {
		Scanner sc = new Scanner(new File(file));
		
		List<RandomVariable> rvs = new ArrayList<RandomVariable>();

		RandomVariable buying = new RandomVariable();
		buying.name = "buying";
		buying.addState("vhigh");
		buying.addState("high");
		buying.addState("med");
		buying.addState("low");
		rvs.add(buying);

		RandomVariable maint = new RandomVariable();
		maint.name = "maint";
		maint.addState("vhigh");
		maint.addState("high");
		maint.addState("med");
		maint.addState("low");
		rvs.add(maint);

		RandomVariable doors = new RandomVariable();
		doors.name = "doors";
		doors.addState("2");
		doors.addState("3");
		doors.addState("4");
		doors.addState("5more");
		rvs.add(doors);

		RandomVariable persons = new RandomVariable();
		persons.name = "persons";
		persons.addState("2");
		persons.addState("4");
		persons.addState("more");
		rvs.add(persons);

		RandomVariable lug_boot = new RandomVariable();
		lug_boot.name = "lug_boot";
		lug_boot.addState("small");
		lug_boot.addState("med");
		lug_boot.addState("big");
		rvs.add(lug_boot);

		RandomVariable safety = new RandomVariable();
		safety.name = "safety";
		safety.addState("low");
		safety.addState("med");
		safety.addState("high");
		rvs.add(safety);
		
		RandomVariable carClass = new RandomVariable();
		carClass.name = "carClass";
		carClass.addState("unacc");
		carClass.addState("acc");
		carClass.addState("good");
		carClass.addState("vgood");
		rvs.add(carClass);
		
		List<List<String>> data = new ArrayList<List<String>>();
		
		while(sc.hasNext()) {
			data.add(Arrays.asList(sc.nextLine().split(",")));			
		}
		
		ADTree test = new ADTree(rvs, data);
		
		return test;
	}
}
