package org.fixme.core.protocol.messages;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.utils.Json;

@AnnotationMessageID(SellOrderMessage.MESSAGE_ID)
public class SellOrderMessage extends NetworkMessage {
	
	public static final int MESSAGE_ID = 1003;
	
	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	
	public int			brokerId;
	
	public int			marketId;
	
	public String		instrument;
	
	public float		quantity;
	
	public float		price;
	
	//##############################################################################
	//@CONSTRUCTOR SECTION -------------------------------------------------------->
	//##############################################################################
	
	public SellOrderMessage(Json buffer) {
		super(buffer);
	}
	
	public SellOrderMessage(int brokerId, int marketId, String instrument, int quantity, String marketName, int price) {
		this.brokerId = brokerId;
		this.marketId = marketId;
		this.instrument = instrument;
		this.quantity = quantity;
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
		return "SellInstrumentMessage";
	}

	@Override
	public void serialize(Json buffer) {
		buffer.put("BORKERID", this.brokerId);
		buffer.put("MARKETID", this.marketId);
		buffer.put("INSTRUMENT", this.instrument);
		buffer.put("QUANTITY", this.quantity);
		buffer.put("PRICE", this.price);
	}

	@Override
	public void deserialize(Json buffer) {
		this.brokerId = buffer.getInt("BROKERID");
		this.marketId = buffer.getInt("MARKETID");
		this.instrument = buffer.getString("INSTRUMENT");
		this.quantity = buffer.getInt("QUANTITY");
		this.price = buffer.getInt("PRICE");
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("BROKERID=").append(this.brokerId).append("|");
		str.append("MARKETID=").append(this.marketId).append("|");
		str.append("INSTRUMENT=").append(this.instrument).append("|");
		str.append("QUANTITY=").append(this.quantity).append("|");
		str.append("PRICE=").append(this.price);
		
		return str.toString();
	}

}
