package org.fixme.core.protocol.types;

import java.util.Map;

import org.fixme.core.database.collections.BaseCollection;
import org.fixme.core.protocol.INetworkType;
import org.fixme.core.utils.Json;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;

@Entity
public class InstrumentObject extends BaseCollection implements INetworkType {
	
	//##############################
	//@PRIVATE VARIABLES SECTION -->
	//##############################
	
	@Property("wallet")
	private String	wallet;
	
	@Property("quantity")
	private int		quantity;
	
	@Property("price")
	private int		price;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public InstrumentObject() { }
	
	public InstrumentObject(String wallet, int quantity, int price) {
		this.wallet = wallet;
		this.quantity = quantity;
		this.price = price;
	}
	
	//##############################################################################
	//@GETTER SETTER SECTION ------------------------------------------------------>
	//##############################################################################

	public String getWallet() {
		return wallet;
	}

	public void setWallet(String wallet) {
		this.wallet = wallet;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
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
		StringBuilder str = new StringBuilder();
		
		str.append("WALLET=").append(this.wallet).append("|");
		str.append("QUANTITY=").append(this.quantity).append("|");
		str.append("PRICE=").append(this.price);
		
		return str.toString();
	}
}
