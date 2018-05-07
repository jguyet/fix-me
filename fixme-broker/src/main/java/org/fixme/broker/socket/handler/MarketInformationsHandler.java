package org.fixme.broker.socket.handler;

import org.fixme.broker.Broker;
import org.fixme.broker.prompt.BrokerPrompt;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.MarketDataMessage;
import org.fixme.core.reflection.handler.ClassMessageHandler;
import org.fixme.core.reflection.handler.MethodMessageHandler;

@ClassMessageHandler("MarketInformationsHandler")
public class MarketInformationsHandler {
	
	@MethodMessageHandler(MarketDataMessage.MESSAGE_ID)
	public static boolean MarketDataMessageHandler(BrokerPrompt prompt, SocketChannel ch, MarketDataMessage message) {
		for (int i = 0; i < message.market_objects.length; i++) {
			Broker.markets.add(message.market_objects[i]);
		}
		return true;
	}

}
