package com.kwchina.webmail.server;

import java.util.Enumeration;
import java.util.Vector;

import com.kwchina.webmail.exception.ErrorHandler;

/*
 * ConnectionTimer.java
 */

public class ConnectionTimer extends Thread {

	private Vector connections;

	private static final long sleep_interval = 1000;

	public ConnectionTimer() {
		connections = new Vector();
		this.start();
	}

	public void printStatus() {
		System.err.println(" Vulture: " + connections.size()
				+ " connections in queue");
	}

	public void addTimeableConnection(TimeableConnection c) {
		synchronized (connections) {
			connections.addElement(c);
		}
	}

	public void removeTimeableConnection(TimeableConnection c) {
		synchronized (connections) {
			connections.removeElement(c);
		}
	}

	public void removeAll() {
		Enumeration e;
		synchronized (connections) {
			e = connections.elements();
		}
		while (e.hasMoreElements()) {
			TimeableConnection t = (TimeableConnection) e.nextElement();
			t.timeoutOccured();
		}
	}

	public void run() {
		Enumeration e;
		while (true) {
			synchronized (connections) {
				e = connections.elements();
			}
			while (e.hasMoreElements()) {
				TimeableConnection t = (TimeableConnection) e.nextElement();
				if (System.currentTimeMillis() - t.getLastAccess() > t.getTimeout()) {
					t.timeoutOccured();
				}
			}
			try {
				this.sleep(sleep_interval);
			} catch (InterruptedException ex) {
				new ErrorHandler(ex);
			}
		}
	}
}
