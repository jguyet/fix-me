package org.fixme.core.protocol.messages;

import org.fixme.core.protocol.ByteArrayBuffer;
import org.fixme.core.protocol.NetworkMessage;

public class RejectedRequestMessage extends NetworkMessage{

	public static final int MESSAGE_ID = 1005;
	
	public RejectedRequestMessage(ByteArrayBuffer buffer) {
		super(buffer);
	}
	
	public RejectedRequestMessage() {
		
	}

	@Override
	public int messageId() {
		// TODO Auto-generated method stub
		return MESSAGE_ID;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "RejectedRequestMessage";
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
