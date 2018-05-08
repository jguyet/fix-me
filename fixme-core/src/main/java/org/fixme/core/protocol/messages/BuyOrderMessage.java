package org.fixme.core.protocol.messages;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.utils.Json;

@AnnotationMessageID(BuyOrderMessage.MESSAGE_ID)
public class BuyOrderMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1002;

	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	
	public String		instrument;
	
	public int			quantity;
	
	public int			market;
	
	public float		price;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public BuyOrderMessage(Json buffer) {
		super(buffer);
	}
	
	public BuyOrderMessage(String instrument, int quantity, int market, float price) {
		this.instrument = instrument;
		this.quantity = quantity;
		this.market = market;
		this.price = price;
	}
	
	//##############################################################################
	//@METHODS SECTION ------------------------------------------------------------>
	//##############################################################################

	@Override
	public int messageId() {
		return MESSAGE_ID;
	}

	@Override
	public String getName() {
		return "BuyInstrumentMessage";
	}

	@Override
	public void serialize(Json buffer) {
		
		buffer.put("INSTRUMENT", this.instrument);
		buffer.put("QUANTITY", this.quantity + "");
		buffer.put("MARKET", this.market);
		buffer.put("PRICE", this.price + "");
	}

	@Override
	public void deserialize(Json buffer) {
		
		this.instrument = buffer.getString("INSTRUMENT");
		this.quantity = buffer.getInt("QUANTITY");
		this.market = buffer.getInt("MARKET");
		this.price = buffer.getFloat("PRICE");
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("INSTRUMENT=").append(this.instrument).append("|");
		str.append("QUANTITY=").append(this.quantity).append("|");
		str.append("MARKET=").append(this.market).append("|");
		str.append("PRICE=").append(this.price);
		
		return str.toString();
	}

}
