package org.fixme.core.protocol.messages;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.ByteArrayBuffer;
import org.fixme.core.protocol.NetworkMessage;

@AnnotationMessageID(MarketDataRequestMessage.MESSAGE_ID)
public class MarketDataRequestMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1006;
	
	public String	marketName;
	
	@Override
	public int messageId() {
		return MESSAGE_ID;
	}
	
	public MarketDataRequestMessage(ByteArrayBuffer buffer) {
		super(buffer);
	}
	
	public MarketDataRequestMessage(String marketName) {
		this.marketName = marketName;
	}

	@Override
	public String getName() {
		return "MarketDataRequestMessage";
	}

	@Override
	public void serialize(ByteArrayBuffer buffer) {
		buffer.writeString(this.marketName);
	}

	@Override
	public void deserialize(ByteArrayBuffer buffer) {
		this.marketName = buffer.readString();
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("MARKET=").append(this.marketName);
		
		return str.toString();
	}

}
