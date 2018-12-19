package com.kwchina.webmail.server;

import java.util.*;

/*
 * URLHandlerTree.java
 */
/**
 * A tree structure to improve (speed up) access to URLs
 * 存储URL的树状结构，以便于得到URL
 */

public class URLHandlerTree implements URLHandlerTreeNode {

	URLHandler handler;

	String url;

	Hashtable nodes;

	StringTokenizer t;

	public URLHandlerTree(String url) {
		nodes = new Hashtable();
		this.url = url;
	}

	public String getURL() {
		return url;
	}

	public void addHandler(String url, URLHandler h) {
		if (url.equals("/") || url.equals("") || url == null) {
			handler = h;
		} else {
			t = new StringTokenizer(url, "/");
			String subtree_name = t.nextToken();
			URLHandlerTree subtree = (URLHandlerTree) nodes.get(subtree_name);
			if (subtree == null) {
				if (this.url.endsWith("/")) {
					subtree = new URLHandlerTree(this.url + subtree_name);
				} else {
					subtree = new URLHandlerTree(this.url + "/" + subtree_name);
				}
				nodes.put(subtree_name, subtree);
			}
			subtree.addHandler(url.substring(subtree_name.length() + 1, url.length()), h);
		}
	}

	public URLHandler getHandler(String url) {
		if (url.equals("/") || url.equals("") || url == null) {
			/* We are the handler */
			return handler;
		} else {
			t = new StringTokenizer(url, "/");
			String subtree_name = t.nextToken();
			URLHandlerTree subtree = (URLHandlerTree) nodes.get(subtree_name);
			if (subtree == null) {
				/* If there is no subtree, we are the handler! */
				return handler;
			} else {
				/* Else there is a subtree */
				URLHandler uh = subtree.getHandler(url.substring(subtree_name
						.length() + 1, url.length()));
				if (uh != null) {
					/* It has a handler */
					return uh;
				} else {
					/* It has no handler, we are handler */
					return handler;
				}
			}
		}
	}

	public String toString() {
		return nodes.toString();
	}

} // URLHandlerTree
