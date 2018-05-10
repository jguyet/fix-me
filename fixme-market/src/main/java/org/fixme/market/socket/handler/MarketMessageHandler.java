package org.fixme.market.socket.handler;

import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.BuyOrderMessage;
import org.fixme.core.protocol.messages.CreateWalletMessage;
import org.fixme.core.protocol.messages.ExecutedRequestMessage;
import org.fixme.core.protocol.messages.GetOrdersMessage;
import org.fixme.core.protocol.messages.GetWalletContentMessage;
import org.fixme.core.protocol.messages.MarketDataMessage;
import org.fixme.core.protocol.messages.MarketDataRequestMessage;
import org.fixme.core.protocol.messages.NewWalletMessage;
import org.fixme.core.protocol.messages.OrdersMessage;
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
		WalletObject wallet_from = Market.database.getWalletCollection().findOne("wallet", message.wallet_from);
		WalletObject wallet_to = Market.database.getWalletCollection().findOne("wallet", message.wallet_to);
		
		if (wallet_from == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Wallet of from doesn't exists (" + message.wallet_from + ")"));
			return true;
		}
		if (wallet_to == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Wallet of to doesn't exists (" + message.wallet_to + ")"));
			return true;
		}
		Query<OrderObject> query = Market.database.getOrderCollection().createQuery();
		query.filter("wallet_from =", message.wallet_from);
	    QueryResults<OrderObject> result = Market.database.getOrderCollection().find(query);
	    float in_circulation = 0.0f;
	    for (OrderObject o : result.asList()) {
	    	in_circulation += o.quantity * o.price;
	    }
	    
	    System.out.println("(wallet_from.quantity - in_circulation) -> " + (wallet_from.quantity - in_circulation));
	    System.out.println("(message.quantity * message.price) -> " + (message.quantity * message.price));
		if (wallet_from.quantity - in_circulation < (message.quantity * message.price)) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Insuffisant quantity in your wallet (" + (wallet_from.quantity - in_circulation) + ")"));
			return true;
		}
		if (message.price <= 0) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Do not price equals or negate of zero"));
			return true;
		}
		if (message.instrument.contains("_") == false) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Instrument currency is not available example BTC_ETH for buy BTC with ETH"));
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
		if (wallet_from.instrument.equalsIgnoreCase(buyCoin) == false) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Is a wallet '" + wallet_from.instrument + "' not a wallet '" + buyCoin + "'"));
			return true;
		}
		if (wallet_to.instrument.equalsIgnoreCase(sellCoin) == false) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Is a wallet '" + wallet_to.instrument + "' not a wallet '" + sellCoin + "'"));
			return true;
		}
		
		String orderId = "";
		do {
			orderId = getRandomBase64();
		} while (Market.database.getOrderCollection().findOne("order_id", orderId) != null);
		
		OrderObject buyOrder = new OrderObject(orderId, "buy", message.wallet_from, message.wallet_to, instrument, message.quantity, message.price);
		Market.database.getOrderCollection().save(buyOrder);
		Market.database.getMarketCollection().save(market);
		channel.write(new ExecutedRequestMessage(message.brokerId, "The order are executed OrderId: " + orderId));
		return true;
	}
	
	@MethodMessageHandler(SellOrderMessage.MESSAGE_ID)
	public static boolean SellOrderMessageHandler(SocketChannel channel, SellOrderMessage message) {
		WalletObject wallet_from = Market.database.getWalletCollection().findOne("wallet", message.wallet_from);
		WalletObject wallet_to = Market.database.getWalletCollection().findOne("wallet", message.wallet_to);
		
		if (wallet_from == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Wallet of from doesn't exists (" + message.wallet_from + ")"));
			return true;
		}
		if (wallet_to == null) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Wallet of to doesn't exists (" + message.wallet_to + ")"));
			return true;
		}
		
		Query<OrderObject> query = Market.database.getOrderCollection().createQuery();
		query.filter("wallet_from =", message.wallet_from);
	    QueryResults<OrderObject> result = Market.database.getOrderCollection().find(query);
	    float in_circulation = 0.0f;
	    for (OrderObject o : result.asList()) {
	    	in_circulation += o.quantity;
	    }
		
		if ((wallet_from.quantity - in_circulation) < message.quantity) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Insuffisant quantity in your wallet (" + (wallet_from.quantity - in_circulation) + ")"));
			return true;
		}
		if (message.price <= 0) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Do not price equals or negate of zero"));
			return true;
		}
		if (message.instrument.contains("_") == false) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Instrument currency is not available example BTC_ETH for Sell ETH to BTC"));
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
		if (wallet_from.instrument.equalsIgnoreCase(sellCoin) == false) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Is a wallet '" + wallet_from.instrument + "' not a wallet '" + sellCoin + "'"));
			return true;
		}
		if (wallet_to.instrument.equalsIgnoreCase(buyCoin) == false) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Is a wallet '" + wallet_to.instrument + "' not a wallet '" + buyCoin + "'"));
			return true;
		}
		
		String orderId = "";
		do {
			orderId = getRandomBase64();
		} while (Market.database.getOrderCollection().findOne("order_id", orderId) != null);
		
		OrderObject sellOrder = new OrderObject(orderId, "sell", message.wallet_from, message.wallet_to, instrument, message.quantity, message.price);
		
		Market.database.getOrderCollection().save(sellOrder);
		Market.database.getMarketCollection().save(market);
		channel.write(new ExecutedRequestMessage(message.brokerId, "The order are executed OrderId: " + orderId));
		return true;
	}
	
	@MethodMessageHandler(GetOrdersMessage.MESSAGE_ID)
	public static boolean GetOrdersMessageHandler(SocketChannel channel, GetOrdersMessage message) {
		if (Market.database.getMarketCollection().exists("name", message.market) == false) {
			channel.write(new RejectedRequestMessage(message.brokerId, "Market doesn't exists"));
			return true ;
		}
		
		Query<OrderObject> sellquery = Market.database.getOrderCollection().createQuery();
		sellquery.filter("currency =", message.market);
		sellquery.filter("type =", "sell");
		sellquery.order("price");
		sellquery.getSortObject().put("price", 1);
		sellquery.limit(5);
	    QueryResults<OrderObject> sellresult = Market.database.getOrderCollection().find(sellquery);
	    List<OrderObject> asks = sellresult.asList();
	    
	    Query<OrderObject> buyquery = Market.database.getOrderCollection().createQuery();
	    buyquery.filter("currency =", message.market);
	    buyquery.filter("type =", "buy");
	    buyquery.order("price");
	    buyquery.getSortObject().put("price", -1);
	    buyquery.limit(5);
	    QueryResults<OrderObject> buyresult = Market.database.getOrderCollection().find(buyquery);
	    List<OrderObject> bids = buyresult.asList();
	    
	    OrderObject[] array_asks = new OrderObject[asks.size()];
	    OrderObject[] array_bids = new OrderObject[bids.size()];
	    
	    int i = 0;
	    for (OrderObject o : asks) {
	    	array_asks[i++] = o;
	    }
	    i = 0;
	    for (OrderObject o : bids) {
	    	array_bids[i++] = o;
	    }
	    channel.write(new OrdersMessage(message.brokerId, message.marketId, message.market, array_bids, array_asks));
		return true ;
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
		float in_circulation = 0.0f;
		
		Query<OrderObject> query = Market.database.getOrderCollection().createQuery();
		query.filter("wallet_from =", wallet.wallet);
		query.filter("type =", "buy");
	    QueryResults<OrderObject> result = Market.database.getOrderCollection().find(query);
	    for (OrderObject o : result.asList()) {
	    	in_circulation += o.quantity * o.price;
	    }
	    
	    Query<OrderObject> queryy = Market.database.getOrderCollection().createQuery();
		queryy.filter("wallet_from =", wallet.wallet);
		queryy.filter("type =", "sell");
	    QueryResults<OrderObject> result2 = Market.database.getOrderCollection().find(queryy);
	    for (OrderObject o : result2.asList()) {
	    	in_circulation += o.quantity;
	    }
	    wallet.quantity -= in_circulation;
		channel.write(new WalletContentMessage(message.brokerId, message.marketId, wallet, in_circulation));
		return true;
	}
	
	public static String getRandomBase64() {
		Random random = ThreadLocalRandom.current();
		byte[] randomBytes = new byte[32];
		random.nextBytes(randomBytes);
		return "0x" + Base64.getUrlEncoder().encodeToString(randomBytes);
	}
}
