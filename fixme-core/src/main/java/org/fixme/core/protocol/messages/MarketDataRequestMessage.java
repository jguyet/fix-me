package org.fixme.core.protocol.messages;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.utils.Json;

@AnnotationMessageID(MarketDataRequestMessage.MESSAGE_ID)
public class MarketDataRequestMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1006;
	
	public int	brokerId;
	
	@Override
	public int messageId() {
		return MESSAGE_ID;
	}
	
	public MarketDataRequestMessage(Json buffer) {
		super(buffer);
	}
	
	public MarketDataRequestMessage(int brokerId) {
		this.brokerId = brokerId;
	}

	@Override
	public String getName() {
		return "MarketDataRequestMessage";
	}

	@Override
	public void serialize(Json buffer) {
		buffer.put("BROKER_ID", this.brokerId);
	}

	@Override
	public void deserialize(Json buffer) {
		this.brokerId = buffer.getInt("BROKER_ID");
	}

	@Override
	public String toString() {
		Json json = new Json();
		this.serialize(json);
		return json.toString();
	}

}
