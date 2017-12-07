package org.fixme.core.protocol.types;

import org.fixme.core.database.collections.BaseCollection;
import org.fixme.core.protocol.ByteArrayBuffer;
import org.fixme.core.protocol.INetworkType;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;

@Entity
public class Instrument extends BaseCollection implements INetworkType {
	
	//##############################
	//@PRIVATE VARIABLES SECTION -->
	//##############################
	
	@Property("name")
	private String	name;
	
	@Property("quantity")
	private int		quantity;
	
	@Property("price")
	private int		price;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public Instrument() { }
	
	public Instrument(String name, int quantity, int price) {
		this.name = name;
		this.quantity = quantity;
		this.price = price;
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
	public void serialize(ByteArrayBuffer buffer) {
		buffer.writeString(this.name);
		buffer.writeInt(this.quantity);
		buffer.writeInt(this.price);
	}

	@Override
	public void deserialize(ByteArrayBuffer buffer) {
		this.name = buffer.readString();
		this.quantity = buffer.readInt();
		this.price = buffer.readInt();
	}
}
