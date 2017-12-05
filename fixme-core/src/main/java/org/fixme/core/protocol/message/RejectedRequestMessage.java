package org.fixme.core.protocol.message;

import java.nio.ByteBuffer;

import org.fixme.core.protocol.NetworkMessage;

public class RejectedRequestMessage extends NetworkMessage{

	public static final int MESSAGE_ID = 1005;
	
	public RejectedRequestMessage(ByteBuffer buffer) {
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
	public ByteBuffer serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deserialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
