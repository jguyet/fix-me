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
	
	@Property("wallet")
	public String	wallet;
	
	@Property("quantity")
	public float	quantity;
	
	@Property("price")
	public float	price;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public OrderObject() { }
	
	public OrderObject(String wallet, float quantity, float price) {
		this.wallet = wallet;
		this.quantity = quantity;
		this.price = price;
	}
	
	//##############################################################################
	//@METHOD SECTION ------------------------------------------------------------->
	//##############################################################################

	@Override
	public void serialize(Json buffer) {
		buffer.put("WALLET", this.wallet);
		buffer.put("QUANTITY", this.quantity);
		buffer.put("PRICE", this.price);
	}

	@Override
	public void deserialize(Json buffer) {
		this.wallet = buffer.getString("WALLET");
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
