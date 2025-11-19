package de.hsh.dbs2.imdb.persistence;

public class DoesNotExistException extends Exception {
	private static final long serialVersionUID = -319320105599282816L;

	public DoesNotExistException(String msg) {
		super(msg);
	}
}
