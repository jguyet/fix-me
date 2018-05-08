package org.fixme.market.socket.handler;

import java.util.List;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.BuyOrderMessage;
import org.fixme.core.protocol.messages.MarketDataMessage;
import org.fixme.core.protocol.messages.MarketDataRequestMessage;
import org.fixme.core.protocol.messages.RejectedRequestMessage;
import org.fixme.core.protocol.messages.SellOrderMessage;
import org.fixme.core.protocol.types.MarketObject;
import org.fixme.core.reflection.handler.ClassMessageHandler;
import org.fixme.core.reflection.handler.MethodMessageHandler;
import org.fixme.market.Market;

import com.google.code.morphia.query.QueryResults;

@ClassMessageHandler("MarketMessageHandler")
public class MarketMessageHandler {

	@MethodMessageHandler(MarketDataRequestMessage.MESSAGE_ID)
	public static boolean marketDataRequestMessageHandler(SocketChannel channel, MarketDataRequestMessage message) {
		List<MarketObject> market = Market.database.getMarketCollection().find().asList();
	
		MarketDataMessage dataMessage = new MarketDataMessage(message.brokerId, channel.getUid(), new MarketObject[market.size()]);
		int i = 0;
		for (MarketObject m : market) {
			dataMessage.market_objects[i++] = m;
		}
		channel.write(dataMessage);
		return true;
	}
	
	@MethodMessageHandler(BuyOrderMessage.MESSAGE_ID)
	public static boolean buyInstrumentMessageHandler(SocketChannel channel, BuyOrderMessage message) {
		
		return true;
	}
	
	@MethodMessageHandler(SellOrderMessage.MESSAGE_ID)
	public static boolean sellInstrumentMessageHandler(SocketChannel channel, SellOrderMessage message) {
		
		return true;
	}
}
