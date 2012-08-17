package org.ralapanawa.mobile.res;

import java.util.List;

public class ExpandableSyncListGroup {

	private String name;
	
	private List<ExpandableSyncListChild> items;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ExpandableSyncListChild> getItems() {
		return items;
	}

	public void setItems(List<ExpandableSyncListChild> items) {
		this.items = items;
	}
	
	
}
