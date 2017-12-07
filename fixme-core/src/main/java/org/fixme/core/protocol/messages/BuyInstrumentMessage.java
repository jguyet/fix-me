package org.fixme.core.protocol.messages;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.ByteArrayBuffer;
import org.fixme.core.protocol.NetworkMessage;

@AnnotationMessageID(BuyInstrumentMessage.MESSAGE_ID)
public class BuyInstrumentMessage extends NetworkMessage {

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
	
	public BuyInstrumentMessage(ByteArrayBuffer buffer) {
		super(buffer);
	}
	
	public BuyInstrumentMessage(String instrument, int quantity, String marketName, int price) {
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
	public void serialize(ByteArrayBuffer buffer) {
		
		buffer.writeString(this.instrument);
		buffer.writeInt(this.quantity);
		buffer.writeString(this.marketName);
		buffer.writeInt(this.price);
	}

	@Override
	public void deserialize(ByteArrayBuffer buffer) {
		
		this.instrument = buffer.readString();
		this.quantity = buffer.readInt();
		this.marketName = buffer.readString();
		this.price = buffer.readInt();
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
