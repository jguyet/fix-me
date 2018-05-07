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
	private String			name;
	
	public int				marketID = -1;
	
	@Property("bids")
	private List<ObjectId>	bids;
	
	@Property("asks")
	private List<ObjectId>	asks;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public MarketObject() {}
	
	public MarketObject(String name) {
		this.name = name;
		this.bids = new ArrayList<ObjectId>();
		this.asks = new ArrayList<ObjectId>();
	}
	
	//##############################################################################
	//@GETTER SETTER SECTION ------------------------------------------------------>
	//##############################################################################

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ObjectId> getBids() {
		if (bids == null)
			this.bids = new ArrayList<ObjectId>();
		return bids;
	}

	public void setBids(List<ObjectId> bids) {
		this.bids = bids;
	}
	
	public void add_sell_Instrument(InstrumentObject i) {
		this.getBids().add(i.getId());
	}

	public List<ObjectId> getAsks() {
		if (this.asks == null)
			this.asks = new ArrayList<ObjectId>();
		return asks;
	}

	public void setAsks(List<ObjectId> asks) {
		this.asks = asks;
	}
	
	public void add_buy_Instrument(InstrumentObject i) {
		this.getAsks().add(i.getId());
	}
	
	//##############################################################################
	//@METHOD SECTION ------------------------------------------------------------->
	//##############################################################################

	@Override
	public void serialize(Json buffer) {
		buffer.put("NAME", this.name);
		
//		String sales = "";
//		for (ObjectId objId : this.getBids()) {
//			if (sales.length() != 0)
//				sales += ";";
//			sales += objId.toHexString();
//		}
//		buffer.put("SALES_LIST", sales);
//		String purchases = "";
//		for (ObjectId objId : this.getAsks()) {
//			if (purchases.length() != 0)
//				purchases += ";";
//			purchases += objId.toHexString();
//		}
//		buffer.put("PURCHASE_LIST", purchases);
	}

	@Override
	public void deserialize(Json buffer) {
		this.name = buffer.getString("NAME");
		
//		String sales = buffer.get("BIDS");
		this.bids = new ArrayList<ObjectId>();
//		for (String s : sales.split("\\;")) {
//			this.bids.add(new ObjectId(s));
//		}
		
//		String purchases = buffer.get("ASKS");
		this.asks = new ArrayList<ObjectId>();
//		for (String s : purchases.split("\\;")) {
//			this.asks.add(new ObjectId(s));
//		}
		
	}
}
