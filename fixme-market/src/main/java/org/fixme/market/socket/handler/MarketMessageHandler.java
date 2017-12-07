package org.fixme.market.socket.handler;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.BuyInstrumentMessage;
import org.fixme.core.protocol.messages.MarketDataRequestMessage;
import org.fixme.core.protocol.messages.RejectedRequestMessage;
import org.fixme.core.protocol.messages.SellInstrumentMessage;
import org.fixme.core.protocol.types.Market;
import org.fixme.core.reflection.handler.ClassMessageHandler;
import org.fixme.core.reflection.handler.MethodMessageHandler;
import org.fixme.market.App;

@ClassMessageHandler("MarketMessageHandler")
public class MarketMessageHandler {

	public boolean marketDataRequestMessageHandler(SocketChannel channel, MarketDataRequestMessage message) {
		Market market = App.database.getMarketCollection().findOne("name", message.marketName);
		
		if (market == null) {
			channel.write(new RejectedRequestMessage(channel.getUid()));
		}
		return true;
	}
	
	@MethodMessageHandler(BuyInstrumentMessage.MESSAGE_ID)
	public boolean buyInstrumentMessageHandler(SocketChannel channel, BuyInstrumentMessage message) {
		
		return true;
	}
	
	@MethodMessageHandler(SellInstrumentMessage.MESSAGE_ID)
	public boolean sellInstrumentMessageHandler(SocketChannel channel, SellInstrumentMessage message) {
		
		return true;
	}
}
