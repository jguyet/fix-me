package org.fixme.router.servers.broker.handler;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.MarketDataRequestMessage;
import org.fixme.core.reflection.handler.ClassMessageHandler;
import org.fixme.core.reflection.handler.MethodMessageHandler;
import org.fixme.router.servers.MarketServerHandler;

@ClassMessageHandler("RouterBrokerMessageHandler")
public class RouterBrokerMessageHandler {

	@MethodMessageHandler(MarketDataRequestMessage.MESSAGE_ID)
	public static boolean MarketDataRequestMessageHandler(SocketChannel client, MarketDataRequestMessage message) {
		
		if (MarketServerHandler.markets.size() == 0) {
			//TODO no market online
			return true ;
		}
		for (SocketChannel market : MarketServerHandler.markets.values()) {
			market.write(message);
		}
		System.out.println("COUCOUCOUCOUCOUC");
		return true;
	}
}
