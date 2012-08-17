package org.ralapanawa.mobile.helpers;

import java.io.Serializable;
import java.util.Map;
import java.util.Stack;

public class DataStack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Stack<Map<String, String>> maps;

	public Stack<Map<String, String>> getMaps() {
		if (maps == null) {
			maps = new Stack<Map<String, String>>();
		}
		return maps;
	}

	public void setMaps(Stack<Map<String, String>> maps) {
		this.maps = maps;
	}

}
