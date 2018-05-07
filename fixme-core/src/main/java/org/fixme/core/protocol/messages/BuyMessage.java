package org.fixme.core.protocol.messages;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.utils.Json;

@AnnotationMessageID(BuyMessage.MESSAGE_ID)
public class BuyMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1002;

	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	
	@Size(min=3)
	public String		instrument;
	
	@Min(value=1)
	public int			quantity;
	
	@Size(min=3, max=3)
	public String		marketName;
	
	@Min(value=1)
	public int			price;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public BuyMessage(Json buffer) {
		super(buffer);
	}
	
	public BuyMessage(String instrument, int quantity, String marketName, int price) {
		this.instrument = instrument;
		this.quantity = quantity;
		this.marketName = marketName;
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
		buffer.put("MARKET", this.marketName);
		buffer.put("PRICE", this.price + "");
	}

	@Override
	public void deserialize(Json buffer) {
		
		this.instrument = buffer.getString("INSTRUMENT");
		this.quantity = buffer.getInt("QUANTITY");
		this.marketName = buffer.getString("MARKET");
		this.price = buffer.getInt("PRICE");
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("INSTRUMENT=").append(this.instrument).append("|");
		str.append("QUANTITY=").append(this.quantity).append("|");
		str.append("MARKET=").append(this.marketName).append("|");
		str.append("PRICE=").append(this.price);
		
		return str.toString();
	}

}
