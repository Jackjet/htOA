package com.kwchina.webmail.ui.html;

import java.util.Locale;

import com.kwchina.webmail.exception.WebMailException;
import com.kwchina.webmail.misc.ByteStore;
import com.kwchina.webmail.storage.Storage;

/*
 * HTMLImage.java
 * 
 */
/**
 * A HTML Document that is actually an image.:-)
 */

public class HTMLImage extends HTMLDocument {

	public ByteStore cont;

	public HTMLImage(ByteStore content) {
		this.cont = content;
	}

	public HTMLImage(Storage store, String name, Locale locale, String theme)
			throws WebMailException {
		cont = new ByteStore(store.getBinaryFile(name, locale, theme));
		cont.setContentType(store.getMimeType(name));
		cont.setContentEncoding("BINARY");
	}

	public int size() {
		if (cont == null) {
			return 0;
		} else {
			return cont.getSize();
		}
	}

	public String getContentEncoding() {
		return cont.getContentEncoding();
	}

	public String getContentType() {
		return cont.getContentType();
	}

	public byte[] toBinary() {
		return cont.getBytes();
	}

} 
