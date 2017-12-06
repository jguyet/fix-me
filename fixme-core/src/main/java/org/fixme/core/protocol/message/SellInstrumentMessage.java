package org.fixme.core.protocol.message;

import org.fixme.core.protocol.ByteArrayBuffer;
import org.fixme.core.protocol.NetworkMessage;

public class SellInstrumentMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1003;
	
	public SellInstrumentMessage(ByteArrayBuffer buffer) {
		super(buffer);
	}
	
	public SellInstrumentMessage() {
		
	}
	
	@Override
	public int messageId() {
		return MESSAGE_ID;
	}

	@Override
	public String getName() {
		return "SellInstrumentMessage";
	}

	@Override
	public void serialize(ByteArrayBuffer buffer) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deserialize(ByteArrayBuffer buffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
