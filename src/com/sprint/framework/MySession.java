package com.sprint.framework;

import jakarta.servlet.http.HttpSession;

public class MySession {
	private HttpSession session;
	
	public MySession() {}
	public MySession(HttpSession session) {
		this.session=session;
	}
	
	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}
	public void add(String key , Object obj) {
		this.getSession().setAttribute(key, obj);
	}
	public void delete(String key) {
		this.getSession().setAttribute(key, null);
	}
	public Object get(String key) {
		return this.getSession().getAttribute(key);
	}
	
}
