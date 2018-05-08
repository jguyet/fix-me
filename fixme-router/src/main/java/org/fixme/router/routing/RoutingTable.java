package org.fixme.router.routing;

import java.util.HashMap;
import java.util.Map;

public class RoutingTable {
	
	private Map<Integer, Route> table = new HashMap<Integer, Route>();
	
	public RoutingTable() {
		
	}
	
	public int addRoute(Route r) {
		this.table.put(r.dest.getUid(), r);
		return (r.dest.getUid());
	}
	
	public Route searchRoute(int id) {
		if (this.table.containsKey(id))
			return this.table.get(id);
		return null;
	}
	
	public boolean removeRoute(int id) {
		if (this.table.containsKey(id)) {
			this.table.remove(id);
			return true;
		}
		return false;
	}
}
