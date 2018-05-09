package org.fixme.market.socket.handler;

import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.BuyOrderMessage;
import org.fixme.core.protocol.messages.CreateWalletMessage;
import org.fixme.core.protocol.messages.ExecutedRequestMessage;
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

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryResults;
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
		System.out.println(dataMessage.toString());
		return true;
	}
	
	@MethodMessageHandler(BuyOrderMessage.MESSAGE_ID)
	public static boolean BuyOrderMessageHandler(SocketChannel channel, BuyOrderMessage message) {
		if (Market.database.getWalletCollection().findOne("wallet", message.walletSeller) == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Wallet of sell doesn't exists (" + message.walletSeller + ")"));
			return true;
		}
		WalletObject wallet = Market.database.getWalletCollection().findOne("wallet", message.walletBuyer);
		
		if (wallet == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Wallet doesn't exists (" + message.walletBuyer + ")"));
			return true;
		}
		if (wallet.quantity < message.quantity) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Insuffisant quantity in your wallet (" + wallet.quantity + ")"));
			return true;
		}
		if (message.price <= 0) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Do not price equals or negate of zero"));
			return true;
		}
		if (message.instrument.contains("_") == false) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Instrument currency is not available example BTC_ETH for buy BTC with ETH currency"));
			return true;
		}
		String instrument = message.instrument.toUpperCase();
		String buyCoin = instrument.split("_")[0].toUpperCase();
		String sellCoin = instrument.split("_")[1].toUpperCase();
		
		MarketObject market = Market.database.getMarketCollection().findOne("name", instrument);
		if (market == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Instrument doesn't exists"));
			return true;
		}
		if (wallet.instrument.equalsIgnoreCase(buyCoin) == false) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Is a wallet '" + wallet.instrument + "' not a wallet '" + buyCoin + "'"));
			return true;
		}
		
		String orderId = "";
		do {
			orderId = getRandomBase64();
		} while (Market.database.getOrderCollection().findOne("order_id", orderId) != null);
		
		OrderObject buyOrder = new OrderObject(orderId, "buy", wallet.wallet, instrument, message.quantity, message.price);
		Market.database.getOrderCollection().save(buyOrder);
		Market.database.getMarketCollection().save(market);
		channel.write(new ExecutedRequestMessage(message.brokerId, "The order are executed OrderId: " + orderId));
		return true;
	}
	
	@MethodMessageHandler(SellOrderMessage.MESSAGE_ID)
	public static boolean SellOrderMessageHandler(SocketChannel channel, SellOrderMessage message) {
		if (Market.database.getWalletCollection().findOne("wallet", message.walletBuyer) == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Wallet of buy doesn't exists (" + message.walletBuyer + ")"));
			return true;
		}
		WalletObject wallet = Market.database.getWalletCollection().findOne("wallet", message.walletSeller);
		
		if (wallet == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Wallet doesn't exists (" + message.walletSeller + ")"));
			return true;
		}
		if (wallet.quantity < message.quantity) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Insuffisant quantity in your wallet (" + wallet.quantity + ")"));
			return true;
		}
		if (message.price <= 0) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Do not price equals or negate of zero"));
			return true;
		}
		if (message.instrument.contains("_") == false) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Instrument currency is not available example BTC_ETH for BTC to ETH"));
			return true;
		}
		String instrument = message.instrument.toUpperCase();
		String buyCoin = instrument.split("_")[0].toUpperCase();
		String sellCoin = instrument.split("_")[1].toUpperCase();
		
		MarketObject market = Market.database.getMarketCollection().findOne("name", instrument);
		if (market == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Instrument doesn't exists"));
			return true;
		}
		if (wallet.instrument.equalsIgnoreCase(sellCoin) == false) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Is a wallet '" + wallet.instrument + "' not a wallet '" + sellCoin + "'"));
			return true;
		}
		
		String orderId = "";
		do {
			orderId = getRandomBase64();
		} while (Market.database.getOrderCollection().findOne("order_id", orderId) != null);
		
		OrderObject sellOrder = new OrderObject(orderId, "sell", wallet.wallet, instrument, message.quantity, message.price);
		Market.database.getOrderCollection().save(sellOrder);
		Market.database.getMarketCollection().save(market);
		channel.write(new ExecutedRequestMessage(message.brokerId, "The order are executed OrderId: " + orderId));
		return true;
	}
	
	@MethodMessageHandler(CreateWalletMessage.MESSAGE_ID)
	public static boolean CreateWalletMessageHandler(SocketChannel channel, CreateWalletMessage message) {
		System.out.println("CREATE WALLET MESSAGE");
		
		if (Market.database.getInstrumentCollection().findOne("name", message.instrument) == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Error instrument doesn't exists"));
			return true;
		}
		if (message.quantity < 0) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Error negative quantity"));
			return true;
		}
		String walletId = "";
		do {
			walletId = getRandomBase64();
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
	
	public static String getRandomBase64() {
		Random random = ThreadLocalRandom.current();
		byte[] randomBytes = new byte[32];
		random.nextBytes(randomBytes);
		return "0x" + Base64.getUrlEncoder().encodeToString(randomBytes);
	}
}
