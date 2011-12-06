package edu.msstate.cse.mrh208;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import edu.msstate.cse.mrh208.Bayes.RandomVariable;

public class Dataset extends Loggable{
	Set<RandomVariable>		variables;
	private Multiset<Entry> entries;
	
	public Dataset() {
		variables 	= new HashSet<RandomVariable>();
		entries		= HashMultiset.create();
	}
	
	public static Dataset fromData(List<Map<RandomVariable, String>> data, Set<RandomVariable> variables) {
		Dataset toReturn = new Dataset();
		toReturn.variables = variables;
		for(Map<RandomVariable, String> datapoint : data) {
			toReturn.entries.add(new Entry(toReturn, datapoint));
		}
		return toReturn;
	}
	
	public static Dataset fromData(String file) throws Exception {
		Dataset dataset = new Dataset();
		Scanner sc = new Scanner(new File(file));
		RandomVariable carClass = new RandomVariable();
		carClass.name = "carClass";
		carClass.states.add("unacc");
		carClass.states.add("acc");
		carClass.states.add("good");
		carClass.states.add("vgood");
		dataset.variables.add(carClass);
		
		RandomVariable buying = new RandomVariable();
		buying.name = "buying";
		buying.states.add("unacc");
		buying.states.add("acc");
		buying.states.add("good");
		buying.states.add("vgood");
		dataset.variables.add(buying);
		
		RandomVariable maint = new RandomVariable();
		maint.name = "maint";
		maint.states.add("unacc");
		maint.states.add("acc");
		maint.states.add("good");
		maint.states.add("vgood");
		dataset.variables.add(maint);
		
		RandomVariable doors = new RandomVariable();
		doors.name = "doors";
		doors.states.add("2");
		doors.states.add("3");
		doors.states.add("4");
		doors.states.add("5more");
		dataset.variables.add(doors);
		
		RandomVariable persons = new RandomVariable();
		persons.name = "persons";
		persons.states.add("2");
		persons.states.add("3");
		persons.states.add("4more");
		dataset.variables.add(persons);
		
		RandomVariable lug_boot = new RandomVariable();
		lug_boot.name = "lug_boot";
		lug_boot.states.add("small");
		lug_boot.states.add("med");
		lug_boot.states.add("big");
		dataset.variables.add(lug_boot);
		
		RandomVariable safety = new RandomVariable();
		safety.name = "safety";
		safety.states.add("low");
		safety.states.add("med");
		safety.states.add("high");
		dataset.variables.add(safety);
		
		while(sc.hasNext()) {
			String[] strings = sc.next().split(",");
			Map<RandomVariable, String> entry = new HashMap<RandomVariable, String>();
			entry.put(buying, strings[0]);
			//entry.put(maint, strings[1]);
			//entry.put(doors, strings[2]);
			//entry.put(persons, strings[3]);
			entry.put(lug_boot, strings[4]);
			entry.put(safety, strings[5]);
			entry.put(carClass, strings[6]);
			
			dataset.entries.add(new Entry(dataset, entry));
		}
		
		return dataset;
	}
	
	public Set<RandomVariable> getVariables() {
		return variables;
	}
	
	public Dataset filter(Map<RandomVariable, String> constraints) {
		//I gotta say... I'm pleased with how elegant this code is.
		Dataset consistent = new Dataset();
		
		entryloop:
		for(Entry entry : entries) {
			for(Map.Entry<RandomVariable, String> constraint : constraints.entrySet()) {
				if(!entry.isConsistentWith(
						constraint.getKey(), 
						constraint.getValue())){
					continue entryloop;
				}
			}
			consistent.entries.add(entry);
		}
		
		return consistent;
	}
	
	public int size() {
		return entries.size();
	}
	
	@Override
	public String toString() {
		return this.toString(0);
	}
	
	@Override
	public String toString(int tabDepth) {
		StringBuilder sb = new StringBuilder(super.toString(tabDepth));
		for(Entry entry : entries) sb.append(entry.toString(tabDepth + 1));
		return sb.toString();
	}
}
