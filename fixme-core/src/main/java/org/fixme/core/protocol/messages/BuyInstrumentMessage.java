package org.fixme.core.protocol.messages;

import javax.validation.constraints.Min;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.ByteArrayBuffer;
import org.fixme.core.protocol.NetworkMessage;

@AnnotationMessageID(BuyInstrumentMessage.MESSAGE_ID)
public class BuyInstrumentMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1002;

	@Min(value=0)
	public int			buyerUniqueId;
	
	public String		instrument;
	
	public int			quantity;
	
	public String		market;
	
	public int			price;
	
	public BuyInstrumentMessage(ByteArrayBuffer buffer) {
		super(buffer);
	}
	
	public BuyInstrumentMessage(int buyerId, String instrument, int quantity, String market, int price) {
		this.buyerUniqueId = buyerId;
		this.instrument = instrument;
		this.quantity = quantity;
		this.market = market;
		this.price = price;
	}

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
		
		buffer.putInt(this.buyerUniqueId);
		buffer.putString(this.instrument);
		buffer.putInt(this.quantity);
		buffer.putString(this.market);
		buffer.putInt(this.price);
	}

	@Override
	public void deserialize(ByteArrayBuffer buffer) {
		this.buyerUniqueId = buffer.readInt();
		this.instrument = buffer.readString();
		this.quantity = buffer.readInt();
		this.market = buffer.readString();
		this.price = buffer.readInt();
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("SESSIONID=").append(this.buyerUniqueId).append("|");
		str.append("INSTRUMENT=").append(this.instrument).append("|");
		str.append("QUANTITY=").append(this.quantity).append("|");
		str.append("MARKET=").append(this.market).append("|");
		str.append("PRICE=").append(this.price);
		
		return str.toString();
	}

}
