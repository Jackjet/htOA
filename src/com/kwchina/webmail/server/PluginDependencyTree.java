package com.kwchina.webmail.server;

import java.util.*;

import com.kwchina.webmail.server.Plugin;
import com.kwchina.webmail.server.PluginDependencyTree;
import com.kwchina.webmail.server.WebMailServer;

/*
 * PluginDependencyTree.java 
 */


public class PluginDependencyTree {

	protected Plugin node;

	protected String meprovides;

	protected Vector children;

	public PluginDependencyTree(Plugin p) {
		this.node = p;
		this.meprovides = p.provides();
		
		children = new Vector();
	}

	public PluginDependencyTree(String s) {
		this.node = null;
		this.meprovides = s;
		
		children = new Vector();
	}

	public boolean provides(String s) {
		return s.equals(meprovides);
	}

	public String provides() {
		String s = meprovides;
		Enumeration e = children.elements();
		while (e.hasMoreElements()) {
			PluginDependencyTree p = (PluginDependencyTree) e.nextElement();
			s += "," + p.provides();
		}
		return s;
	}

	//把所有plugin 按照对应的Tree加入到PluginDependencyTree中
	public boolean addPlugin(Plugin p) {
		if (p.requires().equals(meprovides)) {
			children.addElement(new PluginDependencyTree(p));
			return true;
		} else {
			boolean flag = false;
			Enumeration e = children.elements();
						
			while (e.hasMoreElements()) {
				PluginDependencyTree pt = (PluginDependencyTree) e.nextElement();
				flag = flag || pt.addPlugin(p);
			}
			
			return flag;
		}
	}

	//递归，把Plugin及其对应URL进行登记
	public void register(WebMailServer parent) {
		if (node != null) {
			// System.err.print(node.getName()+" ");
			// System.err.flush();
			node.register(parent);
		}

		/*
		 * Perform depth-first registraion. Breadth-first would be better, but
		 * it will work anyway
		 */
		Enumeration e = children.elements();
		while (e.hasMoreElements()) {
			PluginDependencyTree p = (PluginDependencyTree) e.nextElement();
			p.register(parent);
		}
	}

}
