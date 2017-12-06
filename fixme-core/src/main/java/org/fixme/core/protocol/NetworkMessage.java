package org.fixme.core.protocol;

import java.nio.ByteBuffer;

import org.fixme.core.protocol.utils.CheckSum;

public abstract class NetworkMessage {
	
	private ByteArrayBuffer	buffer;
	private String			checkSum;
	
	public NetworkMessage(ByteArrayBuffer buffer) {
		this.buffer = buffer;
	}
	
	public NetworkMessage() {
		this.buffer = new ByteArrayBuffer();
	}
	
	public String getCheckSum() {
		return this.checkSum;
	}
	
	public void serialize_message() {
		serialize(this.buffer);
		
		//WRITE CHECKSUM
		this.buffer.putString(CheckSum.get(this.buffer.array()));
	}
	
	public void deserialize_message() {
		deserialize(this.buffer);
		
		//READ CHECKSUM
		this.checkSum = this.buffer.readString();
	}
	
	public ByteBuffer array() {
		
		ByteArrayBuffer serializedMessage =  new ByteArrayBuffer();
		
		NetworkMessageHeader header = new NetworkMessageHeader(this.messageId(), this.buffer.getSize());
		ByteArrayBuffer serializedHeader = NetworkProtocolMessage.writeHeader(header);
		
		serializedMessage.put(serializedHeader);
		serializedMessage.put(this.buffer);
		
		serializedMessage.flip();
		return serializedMessage.getByteBuffer();
	}
	
	public abstract int messageId();
	
	public abstract String getName();
	
	public abstract void serialize(ByteArrayBuffer buffer);
	
	public abstract void deserialize(ByteArrayBuffer buffer);
	
	public abstract String toString();
}
