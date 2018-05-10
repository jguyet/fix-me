package org.fixme.broker.socket.handler;

import org.fixme.broker.Broker;
import org.fixme.broker.prompt.BrokerPrompt;
import org.fixme.broker.prompt.CallBackRequestMessage;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.MarketDataMessage;
import org.fixme.core.protocol.messages.NewWalletMessage;
import org.fixme.core.protocol.messages.OrdersMessage;
import org.fixme.core.protocol.messages.WalletContentMessage;
import org.fixme.core.reflection.handler.ClassMessageHandler;
import org.fixme.core.reflection.handler.MethodMessageHandler;

@ClassMessageHandler("MarketInformationsHandler")
public class MarketInformationsHandler {
	
	@MethodMessageHandler(MarketDataMessage.MESSAGE_ID)
	public static boolean MarketDataMessageHandler(BrokerPrompt prompt, SocketChannel ch, MarketDataMessage message) {
		for (int i = 0; i < message.market_objects.length; i++) {
			Broker.markets.put(message.market_objects[i].name, message.market_objects[i]);
		}
		if (Broker.callback != null) {
			CallBackRequestMessage callback = Broker.callback;
			Broker.callback = null;
			callback.onExecutedRequest(ch, message);
		}
		return true;
	}
	
	@MethodMessageHandler(WalletContentMessage.MESSAGE_ID)
	public static boolean WalletContentMessageHandler(BrokerPrompt prompt, SocketChannel ch, WalletContentMessage message) {
		
		if (Broker.callback != null) {
			CallBackRequestMessage callback = Broker.callback;
			Broker.callback = null;
			callback.onExecutedRequest(ch, message);
		}
		return true;
	}
	
	@MethodMessageHandler(NewWalletMessage.MESSAGE_ID)
	public static boolean NewWalletMessageHandler(BrokerPrompt prompt, SocketChannel ch, NewWalletMessage message) {
		
		if (Broker.callback != null) {
			CallBackRequestMessage callback = Broker.callback;
			Broker.callback = null;
			callback.onExecutedRequest(ch, message);
		}
		return true;
	}
	
	@MethodMessageHandler(OrdersMessage.MESSAGE_ID)
	public static boolean OrdersMessageHandler(BrokerPrompt prompt, SocketChannel ch, OrdersMessage message) {
		if (Broker.callback != null) {
			CallBackRequestMessage callback = Broker.callback;
			Broker.callback = null;
			callback.onExecutedRequest(ch, message);
		}
		return true;
	}

}
