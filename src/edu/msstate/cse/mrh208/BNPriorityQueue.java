package edu.msstate.cse.mrh208;

import java.util.Iterator;
import java.util.PriorityQueue;

public class BNPriorityQueue extends PriorityQueue<BNSearchNode> {
	public BNSearchNode findAndRemoveIfEquals(BNSearchNode toFind) {
		Iterator<BNSearchNode> iter = this.iterator();
		BNSearchNode thisNode;
		while((thisNode = iter.next()) != null) {
			if(toFind.equals(thisNode)) {
				this.remove(thisNode);
				
				return thisNode;
			}
		}
		
		return null;
	}
}
