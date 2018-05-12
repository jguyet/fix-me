package org.fixme.market;

import org.fixme.core.database.Database;
import org.fixme.core.protocol.types.InstrumentObject;
import org.fixme.core.protocol.types.MarketObject;

public class Seed {

	public static void SeedMongoDB(Database database) {
		
		//INSTRUMENTS
    	if (database.getInstrumentCollection().exists("name", "USDT") == false)
    		database.getInstrumentCollection().save(new InstrumentObject("USDT", "USDT"));
    	if (database.getInstrumentCollection().exists("name", "XRP") == false)
    		database.getInstrumentCollection().save(new InstrumentObject("Ripple", "XRP"));
    	if (database.getInstrumentCollection().exists("name", "BTC") == false)
    		database.getInstrumentCollection().save(new InstrumentObject("BitCoin", "BTC"));
    	if (database.getInstrumentCollection().exists("name", "ETH") == false)
    		database.getInstrumentCollection().save(new InstrumentObject("Ethereum", "ETH"));
    	if (database.getInstrumentCollection().exists("name", "LTC") == false)
    		database.getInstrumentCollection().save(new InstrumentObject("Litecoin", "LTC"));
    	if (database.getInstrumentCollection().exists("name", "NEO") == false)
    		database.getInstrumentCollection().save(new InstrumentObject("Neo", "NEO"));
		
		//BTC MARKETS
		if (database.getMarketCollection().exists("name", "BTC_ETH") == false)
    		database.getMarketCollection().save(new MarketObject("BitCoin", "BTC_ETH"));
    	if (database.getMarketCollection().exists("name", "BTC_LTC") == false)
    		database.getMarketCollection().save(new MarketObject("BitCoin", "BTC_LTC"));
    	if (database.getMarketCollection().exists("name", "BTC_NEO") == false)
    		database.getMarketCollection().save(new MarketObject("BitCoin", "BTC_NEO"));
    	
    	//ETH MARKETS
    	if (database.getMarketCollection().exists("name", "ETH_LTC") == false)
    		database.getMarketCollection().save(new MarketObject("Ethereum", "ETH_LTC"));
    	if (database.getMarketCollection().exists("name", "ETH_NEO") == false)
    		database.getMarketCollection().save(new MarketObject("Ethereum", "ETH_NEO"));
    	
    	//USDT MARKETS
    	if (database.getMarketCollection().exists("name", "USDT_BTC") == false)
    		database.getMarketCollection().save(new MarketObject("USDT", "USDT_BTC"));
	}
	
}
