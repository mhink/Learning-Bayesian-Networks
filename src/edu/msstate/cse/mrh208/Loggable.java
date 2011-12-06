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
	
	public String toShortString(int tabDepth) {
		StringBuilder sb = new StringBuilder(newline(tabDepth) + "(");
		sb.append(super.toString()
				.replace("edu.msstate.cse.mrh208.", "")
				.replace("Algorithms.", "")
				.replace("Bayes.", "")).append(")");
		
		String test = sb.toString();
		String sbNameOnly = test.replaceAll("\n\t*", "").replaceAll("@.*", "");
		String sbHashOnly = test.replaceAll("\n\t*", "").replaceAll(".*@", "");
		
		int ln = (((sbNameOnly.length() + 1) % 4) + sbHashOnly.length());
		if(ln < 8) sb.append("\t");
		sb.append("\t");
		return sb.toString().replaceAll("@", " @ ");
	}
	
	public String toString(int tabDepth) {		
		StringBuilder sb = new StringBuilder(newline(tabDepth) + "(");
		sb.append(super.toString()
				.replace("edu.msstate.cse.mrh208.", "")
				.replace("Algorithms.", "")
				.replace("Bayes.", "") + ")");
		
		String test = sb.toString();
		String sbNameOnly = test.replaceAll("\n\t*", "").replaceAll("@.*", "");
		String sbHashOnly = test.replaceAll("\n\t*", "").replaceAll(".*@", "");
		
		int ln = (((sbNameOnly.length() + 1) % 4) + sbHashOnly.length());
		if(ln < 8) sb.append("\t");
		sb.append("\t");
		return sb.toString().replaceAll("@", " @ ");
	}
	
	public static String toShortString(Object object, int tabDepth) {
		StringBuilder sb = new StringBuilder(newline(tabDepth));
		sb.append("(" + object.getClass().getSimpleName() + "@" + Integer.toHexString(object.hashCode()) + ")");
		
		String test = sb.toString();
		String sbNameOnly = test.replaceAll("\n\t*", "").replaceAll("@.*", "");
		String sbHashOnly = test.replaceAll("\n\t*", "").replaceAll(".*@", "");
		
		int ln = (((sbNameOnly.length() + 1) % 4) + sbHashOnly.length());
		if(ln < 8) sb.append("\t");
		sb.append("\t");
		return sb.toString().replaceAll("@", " @ ");
	}

	protected static String newline(int tabDepth) {
		StringBuilder tabSB = new StringBuilder();
			if(tabDepth > -1) tabSB.append("\n"); {
			for(int i = 0; i < tabDepth; i++) tabSB.append("\t");
		}
		return tabSB.toString();
	}
}
