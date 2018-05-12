package org.fixme.market;

import java.util.List;

import org.fixme.core.database.Database;
import org.fixme.core.protocol.types.MarketObject;
import org.fixme.core.protocol.types.OrderObject;
import org.fixme.core.protocol.types.WalletObject;

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryResults;

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
			try  {
				checkDatabaseOrders();
			} catch (Exception e) {
				
			}
			try { Thread.sleep(100); } catch (Exception e) {}
		}
	}
	
	public void stop() {
		
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
	    
	    if (bids.size() > 0 && market.buy != bids.get(0).price) {
	    	market.buy = bids.get(0).price;
	    	Market.database.getMarketCollection().save(market);
	    } else if (bids.size() == 0 && market.buy != 0) {
	    	market.buy = 0;
	    	Market.database.getMarketCollection().save(market);
	    }
	    
	    if (asks.size() > 0 && market.sell != asks.get(0).price) {
	    	market.sell = asks.get(0).price;
	    	Market.database.getMarketCollection().save(market);
	    } else if (asks.size() == 0 && market.sell != 0) {
	    	market.sell = 0;
	    	Market.database.getMarketCollection().save(market);
	    }
	    
	    if (asks.size() == 0 && bids.size() == 0)
	    	return ;
	    
	    buyOrders(market, bids, asks);
	}
	
	private void buyOrders(MarketObject market, List<OrderObject> bids, List<OrderObject> asks) {
	    for (OrderObject buyOrder : bids) {
	    	
	    	for (OrderObject sellOrder : asks) {
	    		if (buyOrder.quantity <= 0)
	    			break ;
	    		if (sellOrder.price > buyOrder.price)
	    			break ;
	    		float price = sellOrder.price;
	    		float quantity = sellOrder.quantity;
	    		if (sellOrder.quantity > buyOrder.quantity) {
	    			quantity = buyOrder.quantity;
	    		}
	    		//Ask : Q(0.5) PU(0.002) = 0.001 price
	    		//Bid : Q(0.5) PU(0.002) = 0.001 price
	    		
	    		float totalAsk = price * quantity;
	    		
	    		WalletObject buyerWallet_from = Market.database.getWalletCollection().findOne("wallet", buyOrder.wallet_from);
	    		WalletObject sellerWallet_from = Market.database.getWalletCollection().findOne("wallet", sellOrder.wallet_from);
	    		
	    		WalletObject buyerWallet_to = Market.database.getWalletCollection().findOne("wallet", buyOrder.wallet_to);
	    		WalletObject sellerWallet_to = Market.database.getWalletCollection().findOne("wallet", sellOrder.wallet_to);
	    		
	    		sellerWallet_to.quantity += totalAsk;
	    		buyerWallet_to.quantity += quantity;
	    		
	    		buyOrder.quantity -= quantity;
	    		sellOrder.quantity -= quantity;
	    		
	    		sellerWallet_from.quantity -= quantity;
	    		buyerWallet_from.quantity -= totalAsk;
	    		
	    		Market.database.getOrderCollection().save(sellOrder);
	    		Market.database.getOrderCollection().save(buyOrder);
	    		Market.database.getWalletCollection().save(buyerWallet_from);
	    		Market.database.getWalletCollection().save(sellerWallet_from);
	    		Market.database.getWalletCollection().save(buyerWallet_to);
	    		Market.database.getWalletCollection().save(sellerWallet_to);
	    		
	    		market.last = price;
	    		Market.database.getMarketCollection().save(market);
	    		
	    		System.out.println("ORDER executed " + market.name + " " + quantity + " at " + totalAsk);
	    		
	    		if (sellOrder.quantity <= 0)
	    			Market.database.getOrderCollection().deleteById(sellOrder.getId());
	    	}
	    	if (buyOrder.quantity <= 0)
	    		Market.database.getOrderCollection().deleteById(buyOrder.getId());
	    }
	}
	
}
