package org.fixme.router.servers.market.handler;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.ExecutedRequestMessage;
import org.fixme.core.protocol.messages.MarketDataMessage;
import org.fixme.core.protocol.messages.NewWalletMessage;
import org.fixme.core.protocol.messages.OrdersMessage;
import org.fixme.core.protocol.messages.RejectedRequestMessage;
import org.fixme.core.protocol.messages.WalletContentMessage;
import org.fixme.core.reflection.handler.ClassMessageHandler;
import org.fixme.core.reflection.handler.MethodMessageHandler;
import org.fixme.router.routing.Route;
import org.fixme.router.servers.BrokerServerHandler;

@ClassMessageHandler("RouterMarketMessageHandler")
public class RouterMarketMessageHandler {
	
	@MethodMessageHandler(ExecutedRequestMessage.MESSAGE_ID)
	public static boolean ExecutedRequestMessageHandler(SocketChannel ch, ExecutedRequestMessage message) {
		Route r = BrokerServerHandler.brokerRoutingTable.searchRoute(message.brokerId);
		
		if (r == null) {
			return true ;
		}
		r.dest.write(message);
		return true;
	}
	
	@MethodMessageHandler(RejectedRequestMessage.MESSAGE_ID)
	public static boolean RejectedRequestMessageHandler(SocketChannel ch, RejectedRequestMessage message) {
		Route r = BrokerServerHandler.brokerRoutingTable.searchRoute(message.brokerId);
		
		if (r == null) {
			return true ;
		}
		r.dest.write(message);
		return true;
	}
	
	@MethodMessageHandler(MarketDataMessage.MESSAGE_ID)
	public static boolean MarketDataMessageHandler(SocketChannel ch, MarketDataMessage message) {
		Route r = BrokerServerHandler.brokerRoutingTable.searchRoute(message.brokerId);
		
		if (r == null) {
			return true ;
		}
		r.dest.write(message);
		return true;
	}
	
	@MethodMessageHandler(WalletContentMessage.MESSAGE_ID)
	public static boolean WalletContentMessageHandler(SocketChannel ch, WalletContentMessage message) {
		Route r = BrokerServerHandler.brokerRoutingTable.searchRoute(message.brokerId);
		
		if (r == null) {
			return true ;
		}
		r.dest.write(message);
		return true;
	}
	
	@MethodMessageHandler(NewWalletMessage.MESSAGE_ID)
	public static boolean NewWalletMessageHandler(SocketChannel ch, NewWalletMessage message) {
		Route r = BrokerServerHandler.brokerRoutingTable.searchRoute(message.brokerId);
		
		if (r == null) {
			return true ;
		}
		r.dest.write(message);
		return true;
	}
	
	@MethodMessageHandler(OrdersMessage.MESSAGE_ID)
	public static boolean OrdersMessageHandler(SocketChannel client, OrdersMessage message) {
		Route r = BrokerServerHandler.brokerRoutingTable.searchRoute(message.brokerId);
		
		if (r == null) {
			return true ;
		}
		r.dest.write(message);
		return true;
	}

}
