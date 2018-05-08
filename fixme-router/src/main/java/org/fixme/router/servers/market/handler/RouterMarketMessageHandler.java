package org.fixme.router.servers.market.handler;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.MarketDataMessage;
import org.fixme.core.reflection.handler.ClassMessageHandler;
import org.fixme.core.reflection.handler.MethodMessageHandler;
import org.fixme.router.routing.Route;
import org.fixme.router.servers.BrokerServerHandler;

@ClassMessageHandler("RouterMarketMessageHandler")
public class RouterMarketMessageHandler {
	
	@MethodMessageHandler(MarketDataMessage.MESSAGE_ID)
	public static boolean MarketDataMessageHandler(SocketChannel ch, MarketDataMessage message) {
		Route r = BrokerServerHandler.brokerRoutingTable.searchRoute(message.brokerId);
		
		if (r == null) {
			return true ;
		}
		r.dest.write(message);
		return true;
	}

}
