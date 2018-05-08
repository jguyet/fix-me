package org.fixme.market.socket.handler;

import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.BuyOrderMessage;
import org.fixme.core.protocol.messages.CreateWalletMessage;
import org.fixme.core.protocol.messages.GetWalletContentMessage;
import org.fixme.core.protocol.messages.MarketDataMessage;
import org.fixme.core.protocol.messages.MarketDataRequestMessage;
import org.fixme.core.protocol.messages.NewWalletMessage;
import org.fixme.core.protocol.messages.RejectedRequestMessage;
import org.fixme.core.protocol.messages.SellOrderMessage;
import org.fixme.core.protocol.messages.WalletContentMessage;
import org.fixme.core.protocol.types.MarketObject;
import org.fixme.core.protocol.types.OrderObject;
import org.fixme.core.protocol.types.WalletObject;
import org.fixme.core.reflection.handler.ClassMessageHandler;
import org.fixme.core.reflection.handler.MethodMessageHandler;
import org.fixme.market.Market;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

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
	public static boolean BuyOrderMessageHandler(SocketChannel channel, BuyOrderMessage message) {
		WalletObject wallet = Market.database.getWalletCollection().findOne("wallet", message.wallet);
		
		if (wallet == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Wallet doesn't exists"));
			return true;
		}
		if (wallet.quantity < message.quantity) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Insuffisant quantity in your wallet (" + wallet.quantity + ")"));
			return true;
		}
		if (message.instrument.contains("_") == false) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Instrument currency is not available example BTC_ETH for BTC to ETH"));
			return true;
		}
		MarketObject market = Market.database.getMarketCollection().findOne("name", message.instrument);
		if (market == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Instrument doesn't exists"));
			return true;
		}
		OrderObject o = new OrderObject(wallet.wallet, message.quantity, message.price);
		market.addBuyOrder(o);
		Market.database.getInstrumentCollection().save(o);
		Market.database.getMarketCollection().save(market);
		
//		for (; market.sell >= market.buy; ) {
//			DBObject query = new BasicDBObject();
//			BasicDBObject fields = new BasicDBObject();
//			fields.put("name", 1);
//			DBObject dbObj = Market.database.getInstrumentCollection().find(query, fields);
//			
//		}
		//TODO kjkkkkk
		
		System.out.println("BUY ORDER MESSAGE");
		return true;
	}
	
	@MethodMessageHandler(SellOrderMessage.MESSAGE_ID)
	public static boolean SellOrderMessageHandler(SocketChannel channel, SellOrderMessage message) {
		System.out.println("SELL ORDER MESSAGE");
		return true;
	}
	
	@MethodMessageHandler(CreateWalletMessage.MESSAGE_ID)
	public static boolean CreateWalletMessageHandler(SocketChannel channel, CreateWalletMessage message) {
		System.out.println("CREATE WALLET MESSAGE");
		
		if (Market.database.getMarketCollection().findOne("name", message.instrument) == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Error on instrument doesn't exists"));
			return true;
		}
		if (message.quantity < 0) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Error negative quantity"));
			return true;
		}
		String walletId = "";
		do {
			Random random = ThreadLocalRandom.current();
			byte[] randomBytes = new byte[32];
			random.nextBytes(randomBytes);
			walletId = "0x" + Base64.getUrlEncoder().encodeToString(randomBytes);
		} while (Market.database.getWalletCollection().findOne("wallet", walletId) != null);
		
		if (walletId.isEmpty()) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Error on Wallet generation"));
			return true;
		}
		WalletObject wallet = new WalletObject(walletId, message.instrument, message.quantity);
		
		Market.database.getWalletCollection().save(wallet);
		
		channel.write(new NewWalletMessage(message.brokerId, message.marketId, wallet));
		return true;
	}
	
	@MethodMessageHandler(GetWalletContentMessage.MESSAGE_ID)
	public static boolean GetWalletContentMessageHandler(SocketChannel channel, GetWalletContentMessage message) {
		WalletObject wallet = Market.database.getWalletCollection().findOne("wallet", message.wallet);
		
		if (wallet == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Wallet doesn't exists"));
			return true;
		}
		channel.write(new WalletContentMessage(message.brokerId, message.marketId, wallet));
		return true;
	}
}
