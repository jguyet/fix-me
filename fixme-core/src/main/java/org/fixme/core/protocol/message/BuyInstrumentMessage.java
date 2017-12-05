package org.fixme.core.protocol.message;

import java.nio.ByteBuffer;

import javax.validation.constraints.Min;

import org.fixme.core.ByteBufferUtils;
import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;

@AnnotationMessageID(BuyInstrumentMessage.MESSAGE_ID)
public class BuyInstrumentMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1002;

	@Min(value=0)
	public int			buyerUniqueId;
	
	public String		instrument;
	
	@Min(value=1)
	public int			quantity;
	
	public String		market;
	
	@Min(value=1)
	public int			price;
	
	public BuyInstrumentMessage(ByteBuffer buffer) {
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
	public ByteBuffer serialize() {
		ByteBuffer buffer = ByteBuffer.allocate(2048);
		
		buffer.putInt(this.buyerUniqueId);
		ByteBufferUtils.putString(buffer, this.instrument);
		buffer.putInt(this.quantity);
		ByteBufferUtils.putString(buffer, this.market);
		buffer.putInt(this.price);
		return buffer;
	}

	@Override
	public void deserialize() {
		this.buyerUniqueId = this.buffer.getInt();
		this.instrument = ByteBufferUtils.getString(this.buffer);
		this.quantity = this.buffer.getInt();
		this.market = ByteBufferUtils.getString(this.buffer);
		this.price = this.buffer.getInt();
	}

	@Override
	public String toString() {
		return "SESSIONID=" + this.buyerUniqueId + "|INSTRUMENT=" + this.instrument + "|QUANTITY=" + this.quantity + "|MARKET=" + this.market + "|PRICE=" + this.price;
	}

}
