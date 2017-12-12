package org.fixme.router;

import java.util.HashMap;
import java.util.Map;

public class RoutingTable {

	private static int	ROUTING_UID = 2;
	
	private Map<Integer, Route> table = new HashMap<Integer, Route>();
	
	public RoutingTable() {
		
	}
	
	public int addRoute(Route r) {
		int id = ROUTING_UID++;
		this.table.put(id, r);
		return (id);
	}
	
	public Route searchRoute(int id) {
		if (this.table.containsKey(id))
			return this.table.get(id);
		return null;
	}
}
