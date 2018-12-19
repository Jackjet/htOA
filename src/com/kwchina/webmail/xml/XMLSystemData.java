package com.kwchina.webmail.xml;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import com.kwchina.webmail.config.ChoiceConfigParameter;
import com.kwchina.webmail.config.ConfigParameter;
import com.kwchina.webmail.config.ConfigScheme;
import com.kwchina.webmail.config.ConfigStore;
import com.kwchina.webmail.server.WebMailServer;
import com.kwchina.webmail.server.WebMailVirtualDomain;
import com.kwchina.webmail.storage.Storage;

/*
 * XMLSystemData.java 
 */

public class XMLSystemData extends ConfigStore {

	protected Document root;

	protected Element sysdata;

	/*
	 * Save the time when this document has been loaded. Might be used to reload
	 * a document with a higher modification time
	 */
	protected long loadtime;

	public XMLSystemData(Document d, ConfigScheme cs) {
		super(cs);
		root = d;

		// 获得Root元素
		// sysdata = root.getDocumentElement();
		sysdata = root.getRootElement();

		/**
		 * List ls = sysdata.elements(); for(Iterator it =
		 * ls.iterator();it.hasNext();){ Element tpEle = (Element)it.next();
		 * String name = tpEle.getName(); System.out.println(name +
		 * "---------"); } System.out.println(ls.size() + "---------");
		 */

		// List ls_2 = d.elementByID("KEY");

		/** @todo:设置DocumentType */
		// root.setDocType("");
		if (sysdata == null) {
			sysdata = root.addElement("SYSDATA");

			// sysdata = root.createElement("SYSDATA");
			// root.appendChild(sysdata);
		}
		loadtime = System.currentTimeMillis();
	}

	public long getLoadTime() {
		return loadtime;
	}

	public void setLoadTime(long time) {
		loadtime = time;
	}

	public Document getRoot() {
		return root;
	}

	public Element getSysData() {
		return sysdata;
	}

	// 从配置文件webmail.xml中
	protected String getConfigRaw(String key) {
		/** @todo:XPath使用，如果不使用Xpath，如何得到？ */
		String xpath = "/SYSDATA/GROUP/CONFIG/KEY";
		List ecList = root.selectNodes(xpath);

		// List ecList = sysdata.elements("KEY");
		for (Iterator iterator = ecList.iterator(); iterator.hasNext();) {
			Element element = (Element) iterator.next();
			if (element.getTextTrim().equals(key)) {
				Element p = element.getParent();
				Element elementV = p.element("VALUE");
				if (elementV != null) {
					return elementV.getTextTrim();
				}
			}
		}
		/**
		 * NodeList nl = sysdata.getElementsByTagName("KEY"); for (int i = 0; i <
		 * nl.getLength(); i++) { Element e = (Element) nl.item(i); if
		 * (XMLCommon.getElementTextValue(e).equals(key)) { Element p =
		 * (Element) e.getParentNode(); NodeList valuel =
		 * p.getElementsByTagName("VALUE"); if (valuel.getLength() >= 0) {
		 * return XMLCommon.getElementTextValue((Element) valuel.item(0)); } } }
		 */
		return null;
	}

	public void setConfigRaw(String groupName, String key, String value, String type) {
		String curval = getConfigRaw(key);

		if (curval == null || !curval.equals(value)) {
			// System.err.println("XMLSystemData: "+groupname+"/"+key+" =
			// "+value);
			/* Find all GROUP elements */
			Iterator it = sysdata.elementIterator("GROUP");

			boolean findIt = false;
			while (it.hasNext()) {
				Element group = (Element) it.next();

				if (group.attributeValue("name").equals(groupName)) {
					findIt = true;

					/* If the group name matches, find all keys */
					//List ls = group.elements("KEY");
					ArrayList ls = XMLCommon.getElementsByName(group, "KEY");
					

					int j = 0;
					for (j = 0; j < ls.size(); j++) {
						Element keyelem = (Element) ls.get(j);
						String keyName = XMLCommon.getElementTextValue(keyelem);
						if (key.equals(keyName)) {
							/* If the key already exists, replace it */
							Element conf = (Element) keyelem.getParent();
							Element newEle = createConfigElement(key, value, type);

							// detach
							conf.detach();
							group.remove(conf);

							// addnew
							group.add(newEle);

							return;
						}
					}

					/* If the key was not found, append it */
					if (j >= ls.size()) {
						group.add(createConfigElement(key, value, type));
						return;
					}
				}
			}

			if (!findIt) {
				Element group = createConfigGroup(groupName);

				group.add(createConfigElement(key, value, type));
			}

			/**
			 * NodeList groupl = sysdata.getElementsByTagName("GROUP"); int i =
			 * 0; for (i = 0; i < groupl.getLength(); i++) { Element group =
			 * (Element) groupl.item(i); if
			 * (group.getAttribute("name").equals(groupname)) { If the group
			 * name matches, find all keys NodeList keyl =
			 * group.getElementsByTagName("KEY"); int j = 0; for (j = 0; j <
			 * keyl.getLength(); j++) { Element keyelem = (Element)
			 * keyl.item(j); if
			 * (key.equals(XMLCommon.getElementTextValue(keyelem))) { /* If the
			 * key already exists, replace it Element conf = (Element)
			 * keyelem.getParentNode();
			 * group.replaceChild(createConfigElement(key, value, type), conf);
			 * return; } } If the key was not found, append it if (j >=
			 * keyl.getLength()) { group .appendChild(createConfigElement(key,
			 * value, type)); return; } } } if (i >= groupl.getLength()) {
			 * Element group = createConfigGroup(groupname);
			 * group.appendChild(createConfigElement(key, value, type)); }
			 */

		}
	}

	protected Element createConfigGroup(String groupname) {
		Element group = sysdata.addElement("GROUP");
		group.addAttribute("name", groupname);

		// sysdata.add(group);

		/**
		 * Element group = root.createElement("GROUP");
		 * group.setAttribute("name", groupname); sysdata.appendChild(group);
		 */
		return group;
	}

	protected void deleteConfigGroup(String groupName) {
		Iterator it = sysdata.elementIterator("GROUP");
		while (it.hasNext()) {
			Element group = (Element) it.next();

			if (group.attribute("name").equals(groupName)) {
				group.detach();
				sysdata.remove(group);
			}
		}

		/**
		 * NodeList nl = sysdata.getElementsByTagName("GROUP"); for (int i = 0;
		 * i < nl.getLength(); i++) { if (((Element)
		 * nl.item(i)).getAttribute("name").equals(groupname)) {
		 * sysdata.removeChild(nl.item(i)); } }
		 */
	}

	protected Element getConfigElementByKey(String key) {
		Element config = null;

		/**
		Iterator iter = sysdata.elementIterator();
	    while (iter.hasNext()) {
	        Element sub = (Element)iter.next();
	        System.out.println(sub.getName());
	    }
	    */

	    //Element.elementIterator，只能获取其直接子节点
		//Iterator it = sysdata.elementIterator("KEY");
		
		ArrayList keyElements = XMLCommon.getElementsByName(sysdata,"KEY");
		for (Iterator it = keyElements.iterator();it.hasNext();) {
			Element keyelem = (Element) it.next();

			Element parent = (Element) keyelem.getParent();
			if (XMLCommon.getElementTextValue(keyelem).equals(key) && parent.getName().equals("CONFIG")) {
				config = parent;
				break;
			}
		}

		/**
		 * NodeList nl = sysdata.getElementsByTagName("KEY");
		 * 
		 * Element config = null; for (int i = 0; i < nl.getLength(); i++) {
		 * Element keyelem = (Element) nl.item(i); Element parent = (Element)
		 * keyelem.getParentNode(); if
		 * (XMLCommon.getElementTextValue(keyelem).equals(key) &&
		 * parent.getTagName().equals("CONFIG")) { config = parent; break; } }
		 */

		return config;
	}

	public void initChoices() {
		Enumeration enum1 = getConfigKeys();
		while (enum1.hasMoreElements()) {
			initChoices((String) enum1.nextElement());
		}
	}

	public void initChoices(String key) {
		Element config = getConfigElementByKey(key);

		XMLCommon.genericRemoveAll(config, "CHOICE");

		ConfigParameter param = scheme.getConfigParameter(key);
		if (param instanceof ChoiceConfigParameter) {
			Enumeration enum1 = ((ChoiceConfigParameter) param).choices();
			while (enum1.hasMoreElements()) {
				Element choice = new DefaultElement("CHOICE");
				choice.addText((String) enum1.nextElement());

				config.add(choice);

				/**
				 * Element choice = root.createElement("CHOICE");
				 * choice.appendChild(root.createTextNode((String) enum1
				 * .nextElement())); config.appendChild(choice);
				 */
			}
		}
	}

	//
	protected Element createConfigElement(String key, String value, String type) {
		// Element config =
		// ((Element)sysdata.elements("GROUP").get(0)).addElement("CONFIG");
		DefaultElement config = new DefaultElement("CONFIG");
		config.addAttribute("type", type);

		Element keyelem = config.addElement("KEY");
		Element desc = config.addElement("DESCRIPTION");
		Element valueelem = config.addElement("VALUE");

		keyelem.setText(key);
		desc.setText(scheme.getDescription(key));
		valueelem.setText(value);

		/**
		 * keyelem.appendChild(root.createTextNode(key));
		 * desc.appendChild(root.createTextNode(scheme.getDescription(key)));
		 * valueelem.appendChild(root.createTextNode(value));
		 */

		/**
		 * config.add(keyelem); config.add(desc); config.add(valueelem);
		 */

		ConfigParameter param = scheme.getConfigParameter(key);
		if (param instanceof ChoiceConfigParameter) {
			Enumeration enum1 = ((ChoiceConfigParameter) param).choices();
			while (enum1.hasMoreElements()) {
				Element choice = config.addElement("CHOICE");
				// choice.clearContent();
				choice.addText((String) enum1.nextElement());
				// config.add(choice);
			}
		}

		return config;
	}

	//获取所有的VirtualDomain
	public Enumeration getVirtualDomains() {
		final List ls = sysdata.elements("DOMAIN");
		return new Enumeration() {
			int i = 0;

			public boolean hasMoreElements() {
				return i < ls.size();
			}

			public Object nextElement() {
				Element elem = (Element) ls.get(i++);
				String value = XMLCommon.getTagValue(elem, "NAME");
				return value == null ? "unknown" + (i - 1) : value;
			}
		};
	}

	// 获取webmail Domain
	public WebMailVirtualDomain getVirtualDomain(String domname) {
		List nodel = sysdata.elements("DOMAIN");
		Element elem = null;

		int j = 0;
		for (j = 0; j < nodel.size(); j++) {
			elem = (Element) nodel.get(j);
			elem.normalize();

			List namel = elem.elements("NAME");
			if (!namel.isEmpty()) {
				Element templ = (Element) namel.get(0);
				if (XMLCommon.getElementTextValue(templ).equals(domname)) {
					break;
				}
			}
		}

		/**
		 * NodeList nodel = sysdata.getElementsByTagName("DOMAIN"); Element elem =
		 * null; int j; for (j = 0; j < nodel.getLength(); j++) { elem =
		 * (Element) nodel.item(j); elem.normalize(); NodeList namel =
		 * elem.getElementsByTagName("NAME"); if (namel.getLength() > 0) { if
		 * (XMLCommon.getElementTextValue((Element) namel.item(0))
		 * .equals(domname)) { break; } } }
		 */

		// 如果找到配置的Domain,则返回找到的WebMail Virtual Domain
		if (j < nodel.size() && elem != null) {
			final Element domain = elem;
			return new WebMailVirtualDomain() {

				public String getDomainName() {
					String value = XMLCommon.getTagValue(domain, "NAME");
					return value == null ? "unknown" : value;
				}

				public void setDomainName(String name) throws Exception {
					XMLCommon.setTagValue(domain, "NAME", name, true, "Virtual Domain names must be unique!");
				}

				public String getDefaultServer() {
					String value = XMLCommon.getTagValue(domain, "DEFAULT_HOST");
					return value == null ? "unknown" : value;
				}

				public void setDefaultServer(String name) {
					XMLCommon.setTagValue(domain, "DEFAULT_HOST", name);
				}

				public String getAuthenticationHost() {
					String value = XMLCommon.getTagValue(domain, "AUTHENTICATION_HOST");
					return value == null ? "unknown" : value;
				}

				public void setAuthenticationHost(String name) {
					XMLCommon.setTagValue(domain, "AUTHENTICATION_HOST", name);
				}

				public boolean isAllowedHost(String host) {
					if (getHostsRestricted()) {
						Vector v = new Vector();
						v.addElement(getDefaultServer());

						Enumeration e = getAllowedHosts();
						while (e.hasMoreElements()) {
							v.addElement(e.nextElement());
						}

						Enumeration enum1 = v.elements();
						while (enum1.hasMoreElements()) {
							String next = (String) enum1.nextElement();
							if (host.toUpperCase().endsWith(next.toUpperCase())) {
								return true;
							}
						}
						return false;
					} else {
						return true;
					}
				}

				public void setAllowedHosts(String hosts) {
					List ls = domain.elements("ALLOWED_HOST");
					for (int i = 0; i < ls.size(); i++) {
						domain.remove((Element) ls.get(i));
					}

					StringTokenizer tok = new StringTokenizer(hosts, ", ");
					while (tok.hasMoreElements()) {
						Element ahost = new DefaultElement("ALLOWED_HOST");
						XMLCommon.setElementTextValue(ahost, tok.nextToken());
						domain.add(ahost);
					}
					/**
					 * NodeList nl =
					 * domain.getElementsByTagName("ALLOWED_HOST"); for (int i =
					 * 0; i < nl.getLength(); i++) {
					 * domain.removeChild(nl.item(i)); } StringTokenizer tok =
					 * new StringTokenizer(hosts, ", "); while
					 * (tok.hasMoreElements()) { Element ahost =
					 * root.createElement("ALLOWED_HOST");
					 * XMLCommon.setElementTextValue(ahost, tok.nextToken());
					 * domain.appendChild(ahost); }
					 */
				}

				public Enumeration getAllowedHosts() {
					final List nl = domain.elements("ALLOWED_HOST");
					return new Enumeration() {
						int i = 0;

						public boolean hasMoreElements() {
							return i < nl.size();
						}

						public Object nextElement() {
							String value = XMLCommon.getElementTextValue((Element) nl.get(i++));
							return value == null ? "error" : value;
						}
					};
				}

				public void setHostsRestricted(boolean b) {
					List nl = domain.elements("RESTRICTED");
					for (int i = 0; i < nl.size(); i++) {
						domain.remove((Element) nl.get(i));
					}
					if (b) {
						domain.add(root.addElement("RESTRICTED"));
					}
				}

				public boolean getHostsRestricted() {
					List nl = domain.elements("RESTRICTED");
					return nl.size() > 0;
				}
			};
		} else {
			return null;
		}

	}

	/**
	 * This is just completely useless, since you can change virtual domains
	 * directly. It should be removed ASAP
	 */
	public void setVirtualDomain(String name, WebMailVirtualDomain domain) {
		System.err.println("Called useless net.wastl.webmail.xml.XMLSystemData::setVirtualDomain/2");
	}

	//删除VirtualDomain
	public void deleteVirtualDomain(String name) {
		List ls = sysdata.elements("NAME");
		for (Iterator it = ls.iterator(); it.hasNext();) {
			Element ele = (Element) it.next();
			if (ele.getParent().getName().equals("DOMAIN") && XMLCommon.getElementTextValue(ele).equals(name)) {
				sysdata.remove(ele.getParent());
			}
		}

		/**
		 * NodeList nl = sysdata.getElementsByTagName("NAME"); for (int i = 0; i <
		 * nl.getLength(); i++) { if
		 * (nl.item(i).getParentNode().getNodeName().equals("DOMAIN") &&
		 * XMLCommon.getElementTextValue((Element) nl.item(i)) .equals(name)) {
		 * sysdata.removeChild(nl.item(i).getParentNode()); } }
		 */
		WebMailServer.getStorage().log(Storage.LOG_INFO, "XMLSystemData: Deleted WebMail virtual domain " + name);
	}

	//创建新的VirtualDomain
	public void createVirtualDomain(String name) throws Exception {
		WebMailVirtualDomain dom = getVirtualDomain(name);
		if (dom != null) {
			throw new Exception("Domain names must be unique!");
		}

		Element domain = new DefaultElement("DOMAIN");
		sysdata.add(domain);

		domain.add(new DefaultElement("NAME"));
		domain.add(new DefaultElement("DEFAULT_HOST"));
		domain.add(new DefaultElement("AUTHENTICATION_HOST"));
		domain.add(new DefaultElement("RESTRICTED"));
		domain.add(new DefaultElement("ALLOWED_HOST"));
		
		//名称（必须先设置，否则sysdata.getVirtualDomain(name)找不到
		XMLCommon.setTagValue(domain, "NAME", name);	
		
		/**
		XMLCommon.setTagValue(domain, "DEFAULT_HOST", "localhost");
		XMLCommon.setTagValue(domain, "AUTHENTICATION_HOST", "localhost");
		XMLCommon.setTagValue(domain, "ALLOWED_HOST", "localhost");
		*/
		
		WebMailServer.getStorage().log(Storage.LOG_INFO, "XMLSystemData: Created WebMail virtual domain " + name);
	}

}
