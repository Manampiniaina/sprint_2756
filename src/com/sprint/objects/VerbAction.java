package com.sprint.objects;

public class VerbAction {
	String verb;
	String methodname;
	public VerbAction() {}
	public VerbAction(String verb ,String methodname) {
		this.verb=verb;
		this.methodname=methodname;
	}
	
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	
	
}
