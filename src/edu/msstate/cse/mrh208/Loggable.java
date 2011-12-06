package edu.msstate.cse.mrh208;


public abstract class Loggable {
	public static void log(String logMsg, Object obj) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String callingFunction 
        	= (stackTraceElements[2]
        			.getClassName() 
        			+ ": " 
        			+ stackTraceElements[2]
        					.getMethodName())
        		.replace("edu.msstate.cse.mrh208.", "")
        		.replace("Bayes.", " ")
        		.replace("Algorithms.", "");
        System.out.println(callingFunction);
        
        if(obj != null) {
        	String objName = obj.toString()
        			.replace("edu.msstate.cse.mrh208.", "")
        			.replace("Bayes.", "")
        			.replace("Algorithms.", "");
        	
        	System.out.println("###\t" + objName + "\n\t"+ logMsg);
        }
        else {
        	System.out.println("\t" + logMsg);
        }
	}
	
	@Override
	public String toString() {
		return toString(0);
	}
	
	public String toString(int tabDepth) {		
		StringBuilder sb = new StringBuilder(tabs(tabDepth));
		sb.append(super.toString()
				.replace("edu.msstate.cse.mrh208.", "")
				.replace("Algorithms.", "")
				.replace("Bayes.", ""));
		
		String test = sb.toString();
		int sbNameOnly = test.replaceAll("@.*", "").length() + 1;
		int sbHashOnly = test.replaceAll(".*@", "").length();
		
		int ln = ((sbNameOnly % 4) + sbHashOnly);
		if(ln < 8) sb.append("\t");
		sb.append("\t");
		return sb.toString();
	}

	protected String tabs(int tabDepth) {
		StringBuilder tabSB = new StringBuilder();
		for(int i = 0; i < tabDepth; i++) tabSB.append("\t");
		String tabs = tabSB.toString();
		return tabs;
	}
	
	public String toShortString() {
		return toShortString(0);
	}
	
	public String toShortString(int tabDepth) {
		return toString(tabDepth);
	}
}
