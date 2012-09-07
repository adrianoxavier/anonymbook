package com.anonymbook.server.domain;

public class PersonMessage {

	private boolean firstAccess;

	public PersonMessage() {
		this.firstAccess = false;
	}

	public boolean isFirstAccess() {
		return firstAccess;
	}

	public void setFirstAccess(boolean firstAccess) {
		this.firstAccess = firstAccess;
	}

}
