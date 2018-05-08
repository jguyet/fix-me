package org.fixme.core.protocol.types;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.fixme.core.database.collections.BaseCollection;
import org.fixme.core.protocol.INetworkType;
import org.fixme.core.utils.Json;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;

@Entity
public class MarketObject extends BaseCollection implements INetworkType {
	
	@Property("name")
	public String			name;
	
	@Property("bids")
	public List<ObjectId>	bids = new ArrayList<ObjectId>();
	
	@Property("asks")
	public List<ObjectId>	asks = new ArrayList<ObjectId>();
	
	@Property("last")
	public float			last = 0.0f;
	
	@Property("buy")
	public float			buy = 0.0f;
	
	@Property("sell")
	public float			sell = 0.0f;
	
	public int				marketID = -1;
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public MarketObject() {}
	
	public MarketObject(String name) {
		this.name = name;
	}
	
	//##############################################################################
	//@GETTER SETTER SECTION ------------------------------------------------------>
	//##############################################################################
	
	public void addSellOrder(OrderObject i) {
		if (i.price < this.sell) {
			this.sell = i.price;
		}
		this.bids.add(i.getId());
	}
	
	public void addBuyOrder(OrderObject i) {
		if (i.price > this.buy) {
			this.buy = i.price;
		}
		this.asks.add(i.getId());
	}
	
	//##############################################################################
	//@METHOD SECTION ------------------------------------------------------------->
	//##############################################################################

	@Override
	public void serialize(Json buffer) {
		buffer.put("NAME", this.name);
		buffer.put("LAST", this.last);
		buffer.put("BUY", this.buy);
		buffer.put("SELL", this.sell);
	}

	@Override
	public void deserialize(Json buffer) {
		this.name = buffer.getString("NAME");
		this.last = buffer.getFloat("LAST");
		this.buy = buffer.getFloat("BUY");
		this.sell = buffer.getFloat("SELL");
	}
}
