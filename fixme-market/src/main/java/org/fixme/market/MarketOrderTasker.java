package org.fixme.market;

import java.util.List;

import org.fixme.core.database.Database;
import org.fixme.core.protocol.types.MarketObject;
import org.fixme.core.protocol.types.OrderObject;
import org.fixme.core.protocol.types.WalletObject;

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryResults;

import ch.qos.logback.core.db.BindDataSourceToJNDIAction;

public class MarketOrderTasker implements Runnable {

	private Thread _t;
	
	public MarketOrderTasker() {
		
		this._t = new Thread(this);
		this._t.setPriority(Thread.MAX_PRIORITY);
		this._t.start();
	}

	@Override
	public void run() {
		
		while (Market.stopped == false) {
			checkDatabaseOrders();
			try { Thread.sleep(100); } catch (Exception e) {}
		}
	}
	
	private void checkDatabaseOrders() {
		Database database = Market.database;
		
		QueryResults<MarketObject> markets = database.getMarketCollection().find();
		
		for (MarketObject market : markets.asList()) {
			checkMarketOrders(database, market);
		}
	}
	
	private void checkMarketOrders(Database database, MarketObject market) {
		Query<OrderObject> sellquery = Market.database.getOrderCollection().createQuery();
		sellquery.filter("currency =", market.name);
		sellquery.filter("type =", "sell");
		sellquery.order("price");
		sellquery.getSortObject().put("price", 1);
		sellquery.limit(10);
	    QueryResults<OrderObject> sellresult = database.getOrderCollection().find(sellquery);
	    List<OrderObject> asks = sellresult.asList();
	    
	    Query<OrderObject> buyquery = Market.database.getOrderCollection().createQuery();
	    buyquery.filter("currency =", market.name);
	    buyquery.filter("type =", "buy");
	    buyquery.order("price");
	    buyquery.getSortObject().put("price", -1);
	    buyquery.limit(10);
	    QueryResults<OrderObject> buyresult = database.getOrderCollection().find(buyquery);
	    List<OrderObject> bids = buyresult.asList();
	    
	    if (asks.size() == 0 && bids.size() == 0)
	    	return ;
	    
	    buyOrders(market, bids, asks);
	}
	
	private void buyOrders(MarketObject market, List<OrderObject> bids, List<OrderObject> asks) {
		System.out.println("MARKET " + market);
	    System.out.println("ASK -> ");
	    for (OrderObject o : asks) {
	    	System.out.println(o.toString());
	    }
	    System.out.println("BID -> ");
	    for (OrderObject o : bids) {
	    	System.out.println(o.toString());
	    }
	    for (OrderObject buyOrder : bids) {
	    	
	    	for (OrderObject sellOrder : asks) {
	    		if (buyOrder.quantity == 0)
	    			break ;
	    		if (sellOrder.price > buyOrder.price)
	    			return ;
	    		float price = sellOrder.price;
	    		float quantity = sellOrder.quantity;
	    		if (sellOrder.quantity > buyOrder.quantity) {
	    			quantity = buyOrder.quantity;
	    		}
	    		//Ask : Q(0.5) PU(0.002) = 0.001 price
	    		//Bid : Q(0.5) PU(0.002) = 0.001 price
	    		
	    		//float totalAsk = price * quantity;
	    		//float totalBid = price * 
	    		
	    		WalletObject buyerWallet = Market.database.getWalletCollection().findOne("wallet", buyOrder.wallet);
	    		WalletObject sellerWallet = Market.database.getWalletCollection().findOne("wallet", sellOrder.wallet);
	    		
	    		//sellerWallet.quantity += totalAsk;
	    		buyerWallet.quantity += quantity;
	    		
	    		buyOrder.quantity -= quantity;
	    		//sellOrder.quantity -= quantity;
	    		
	    		if (sellerWallet.quantity <= 0)
	    			Market.database.getOrderCollection().deleteById(sellOrder.getId());
	    		if (buyOrder.quantity == 0)
		    		Market.database.getOrderCollection().deleteById(buyOrder.getId());
	    		return ;
	    	}
	    }
	}
	
}
