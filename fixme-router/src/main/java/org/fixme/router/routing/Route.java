package org.fixme.router.routing;

import org.fixme.core.client.SocketChannel;

public class Route {

	public SocketChannel dest;
	
	public Route(SocketChannel destination) {
		this.dest = destination;
	}
}
