package edu.msstate.cse.mrh208;

public class Log {
	public static void log(String logMsg, Object obj) {
		StringBuilder sb = new StringBuilder();
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		for(int i = 0; i < stackTraceElements.length-5; i++) {
			sb.append("\t");
		}
        sb.append((stackTraceElements[2]
        			.getClassName() 
        			+ ": " 
        			+ stackTraceElements[2]
        					.getMethodName())
        		.replace("edu.msstate.cse.mrh208.", "")
        		.replace("Bayes.", " ")
        		.replace("Algorithms.", ""));
        sb.append(" " + logMsg);
	}
}
