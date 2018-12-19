package com.kwchina.webmail.misc;

import java.util.*;

/**
 * Queue.java
 * 
 */

public class Queue  {

    Vector contents;

    public Queue() {
    	contents=new Vector();
    }
    

    public void queue(Object o) {
    	contents.addElement(o);
    }

    public boolean isEmpty() {
    	return contents.isEmpty();
    }

    public Object next() {
		Object o=contents.firstElement();
		
		contents.removeElementAt(0);
		return o;
    }
}
