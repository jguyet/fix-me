package org.fixme.core.protocol.types;

import org.fixme.core.database.collections.BaseCollection;
import org.fixme.core.protocol.INetworkType;
import org.fixme.core.utils.Json;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;

@Entity
public class MarketObject extends BaseCollection implements INetworkType {
	
	@Property("name")
	public String				name;
	
	@Property("fullname")
	public String				fullname;
	
	@Property("last")
	public float				last = 0.0f;
	
	@Property("buy")
	public float				buy = 0.0f;
	
	@Property("sell")
	public float				sell = 0.0f;
	
	public int					marketID = -1;
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public MarketObject() {}
	
	public MarketObject(String fullname, String name) {
		this.fullname = fullname;
		this.name = name;
	}
	
	//##############################################################################
	//@METHOD SECTION ------------------------------------------------------------->
	//##############################################################################

	@Override
	public void serialize(Json buffer) {
		buffer.put("FULLNAME", this.fullname);
		buffer.put("NAME", this.name);
		buffer.put("LAST", this.last);
		buffer.put("BUY", this.buy);
		buffer.put("SELL", this.sell);
	}

	@Override
	public void deserialize(Json buffer) {
		this.fullname = buffer.getString("FULLNAME");
		this.name = buffer.getString("NAME");
		this.last = buffer.getFloat("LAST");
		this.buy = buffer.getFloat("BUY");
		this.sell = buffer.getFloat("SELL");
	}
	
	@Override
	public String toString() {
		Json json = new Json();
		this.serialize(json);
		return json.toString();
	}
}
