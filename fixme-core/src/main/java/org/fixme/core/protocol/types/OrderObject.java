package org.fixme.core.protocol.types;

import org.fixme.core.database.collections.BaseCollection;
import org.fixme.core.protocol.INetworkType;
import org.fixme.core.utils.Json;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;

@Entity
public class OrderObject extends BaseCollection implements INetworkType {
	
	//##############################
	//@PRIVATE VARIABLES SECTION -->
	//##############################
	
	@Property("order_id")
	public String	order_id;
	
	@Property("wallet_from")
	public String	wallet_from;
	
	@Property("wallet_to")
	public String	wallet_to;
	
	@Property("currency")
	public String	currency;
	
	@Property("quantity")
	public float	quantity;
	
	@Property("price")
	public float	price;
	
	@Property("type")
	public String	type;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public OrderObject() { }
	
	public OrderObject(String order_id, String type, String wallet_from, String wallet_to, String currency, float quantity, float price) {
		this.order_id = order_id;
		this.type = type;
		this.wallet_from = wallet_from;
		this.wallet_to = wallet_to;
		this.currency = currency;
		this.quantity = quantity;
		this.price = price;
	}
	
	//##############################################################################
	//@METHOD SECTION ------------------------------------------------------------->
	//##############################################################################

	@Override
	public void serialize(Json buffer) {
		buffer.put("ORDERID", this.order_id);
		buffer.put("TYPE", this.type);
		buffer.put("CURRENCY", this.currency);
		buffer.put("QUANTITY", this.quantity);
		buffer.put("PRICE", this.price);
	}

	@Override
	public void deserialize(Json buffer) {
		this.order_id = buffer.getString("ORDERID");
		this.type = buffer.getString("TYPE");
		this.currency = buffer.getString("CURRENCY");
		this.quantity = buffer.getFloat("QUANTITY");
		this.price = buffer.getFloat("PRICE");
	}
	
	@Override
	public String toString() {
		Json json = new Json();
		this.serialize(json);
		return json.toString();
	}
}
