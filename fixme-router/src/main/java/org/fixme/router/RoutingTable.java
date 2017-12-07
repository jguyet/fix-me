package org.fixme.router;

import java.util.HashMap;
import java.util.Map;

public class RoutingTable {

	private Map<Integer, Route> table = new HashMap<Integer, Route>();
	
	public RoutingTable() {
		
	}
	
	public void addRoute(int id, Route r) {
		this.table.put(id, r);
	}
	
	public Route searchRoute(int id) {
		if (this.table.containsKey(id))
			return this.table.get(id);
		return null;
	}
}
