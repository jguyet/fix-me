package org.fixme.router.servers.broker.handler;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.BuyOrderMessage;
import org.fixme.core.protocol.messages.CreateWalletMessage;
import org.fixme.core.protocol.messages.GetOrdersMessage;
import org.fixme.core.protocol.messages.GetWalletContentMessage;
import org.fixme.core.protocol.messages.MarketDataRequestMessage;
import org.fixme.core.protocol.messages.RejectedRequestMessage;
import org.fixme.core.protocol.messages.SellOrderMessage;
import org.fixme.core.reflection.handler.ClassMessageHandler;
import org.fixme.core.reflection.handler.MethodMessageHandler;
import org.fixme.router.routing.Route;
import org.fixme.router.servers.MarketServerHandler;

@ClassMessageHandler("RouterBrokerMessageHandler")
public class RouterBrokerMessageHandler {

	@MethodMessageHandler(MarketDataRequestMessage.MESSAGE_ID)
	public static boolean MarketDataRequestMessageHandler(SocketChannel client, MarketDataRequestMessage message) {
		
		if (MarketServerHandler.markets.size() == 0) {
			client.write(new RejectedRequestMessage(client.getUid(), "No market online"));
			return true ;
		}
		for (SocketChannel market : MarketServerHandler.markets.values()) {
			market.write(message);
		}
		return true;
	}
	
	@MethodMessageHandler(BuyOrderMessage.MESSAGE_ID)
	public static boolean BuyOrderMessageHandler(SocketChannel client, BuyOrderMessage message) {
		Route route = MarketServerHandler.marketRoutingTable.searchRoute(message.marketId);
		
		if (route == null) {
			client.write(new RejectedRequestMessage(client.getUid(), "Market " + message.marketId + " doesn't exists"));
			return true;
		}
		route.dest.write(message);
		return true;
	}
	
	@MethodMessageHandler(SellOrderMessage.MESSAGE_ID)
	public static boolean SellOrderMessageHandler(SocketChannel client, SellOrderMessage message) {
		Route route = MarketServerHandler.marketRoutingTable.searchRoute(message.marketId);
		
		if (route == null) {
			client.write(new RejectedRequestMessage(client.getUid(), "Market " + message.marketId + " doesn't exists"));
			return true ;
		}
		route.dest.write(message);
		return true;
	}
	
	@MethodMessageHandler(CreateWalletMessage.MESSAGE_ID)
	public static boolean CreateWalletMessageHandler(SocketChannel client, CreateWalletMessage message) {
		Route route = MarketServerHandler.marketRoutingTable.searchRoute(message.marketId);
		
		if (route == null) {
			client.write(new RejectedRequestMessage(client.getUid(), "Market " + message.marketId + " doesn't exists"));
			return true ;
		}
		route.dest.write(message);
		return true;
	}
	
	@MethodMessageHandler(GetWalletContentMessage.MESSAGE_ID)
	public static boolean GetWalletContentMessageHandler(SocketChannel client, GetWalletContentMessage message) {
		Route route = MarketServerHandler.marketRoutingTable.searchRoute(message.marketId);
		
		if (route == null) {
			client.write(new RejectedRequestMessage(client.getUid(), "Market " + message.marketId + " doesn't exists"));
			return true ;
		}
		route.dest.write(message);
		return true;
	}
	
	@MethodMessageHandler(GetOrdersMessage.MESSAGE_ID)
	public static boolean GetOrdersMessageHandler(SocketChannel client, GetOrdersMessage message) {
		Route route = MarketServerHandler.marketRoutingTable.searchRoute(message.marketId);
		
		if (route == null) {
			client.write(new RejectedRequestMessage(client.getUid(), "Market " + message.marketId + " doesn't exists"));
			return true ;
		}
		route.dest.write(message);
		return true;
	}
}
