package com.kwchina.webmail.misc;

import java.io.*;

/**
 * StreamConnector.java
 *
 
 */
/**
 * Used to write to a OutputStream in a separate Thread to avoid blocking.
 * 
 */
public class StreamConnector extends Thread {

	InputStream in;

	ByteStore b;

	int size;

	boolean ready = false;

	public StreamConnector(InputStream sin, int size) {
		super();
		in = sin;
		this.size = size;
		b = null;
		this.start();
	}

	public void run() {
		b = ByteStore.getBinaryFromIS(in, size);
		ready = true;
	}

	public ByteStore getResult() {
		while (!ready) {
			try {
				sleep(500);
				System.err.print(".");
			} catch (InterruptedException ex) {
			}
		}
		System.err.println();
		return b;
	}

} 
