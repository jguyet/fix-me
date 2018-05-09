package org.fixme.core.protocol.types;

import java.util.Map;

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
	
	@Property("wallet")
	public String	wallet;
	
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
	
	public OrderObject(String order_id, String type, String wallet, String currency, float quantity, float price) {
		this.order_id = order_id;
		this.type = type;
		this.wallet = wallet;
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
		buffer.put("WALLET", this.wallet);
		buffer.put("CURRENCY", this.currency);
		buffer.put("QUANTITY", this.quantity);
		buffer.put("PRICE", this.price);
	}

	@Override
	public void deserialize(Json buffer) {
		this.order_id = buffer.getString("ORDERID");
		this.type = buffer.getString("TYPE");
		this.wallet = buffer.getString("WALLET");
		this.currency = buffer.getString("CURRENCY");
		this.quantity = buffer.getInt("QUANTITY");
		this.price = buffer.getInt("PRICE");
	}
	
	@Override
	public String toString() {
		Json json = new Json();
		this.serialize(json);
		return json.toString();
	}
}
