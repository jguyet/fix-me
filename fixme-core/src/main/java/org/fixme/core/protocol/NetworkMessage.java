package org.fixme.core.protocol;

import java.nio.ByteBuffer;

import org.fixme.core.protocol.utils.CheckSum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class NetworkMessage<br>
 * ... TODO write
 * @author jguyet
 * @Commented
 */
public abstract class NetworkMessage {
	
	//##############################
	//@PRIVATE VARIABLES SECTION -->
	//##############################
	
	private ByteArrayBuffer	buffer;
	private int				mindId;
	private String			checkSum;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public NetworkMessage(ByteArrayBuffer buffer) {
		this.buffer = buffer;
	}
	
	public NetworkMessage() {
		this.buffer = new ByteArrayBuffer();
	}
	
	//##############################################################################
	//@GETTER SETTER SECTION ------------------------------------------------------>
	//##############################################################################
	
	public void setmindId(int id) {
		this.mindId = id;
	}
	
	public int getmindId() {
		return mindId;
	}
	
	public String getCheckSum() {
		return this.checkSum;
	}
	
	//##############################################################################
	//@BUILD METHODS SECTION ------------------------------------------------------>
	//##############################################################################
	
	public void serialize_message() {
		
		//WRITE HEADER
		NetworkMessageHeader header = new NetworkMessageHeader(this.mindId, this.messageId(), this.buffer.size());
		ByteArrayBuffer serializedHeader = NetworkProtocolMessage.writeHeader(header);
		this.buffer.write(serializedHeader);
		
		serialize(this.buffer);
		
		//WRITE CHECKSUM
		this.buffer.writeString(CheckSum.get(this.buffer.array()));
	}
	
	public void deserialize_message() {
		deserialize(this.buffer);
		
		//READ CHECKSUM
		this.checkSum = this.buffer.readString();
	}
	
	/**
	 * Get on buffer all message properties out of Header and checksum<br>
	 * and generate checksum for futur check
	 * @return String
	 */
	public String buildCheckSum() {
		int endoffset = ByteArrayBuffer.BYTE_INT_SIZE + this.checkSum.length();
		
		String checksum = CheckSum.get(this.buffer.array(0, this.buffer.size() - endoffset));
		return checksum;
	}
	
	public ByteBuffer array() {
		this.buffer.flip();
		return this.buffer.getByteBuffer();
	}
	
	//##############################################################################
	//@ABSTRACT METHODS SECTION --------------------------------------------------->
	//##############################################################################
	
	/**
	 * Getter of static message Identity
	 * @return int
	 */
	public abstract int messageId();
	
	/**
	 * Getter of static message Name
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * Message serializer method<br>
	 * write on buffer all message properties
	 * @param buffer
	 */
	public abstract void serialize(ByteArrayBuffer buffer);
	
	/**
	 * Message deserializer method<br>
	 * read buffer content for load message properties
	 * @param buffer
	 */
	public abstract void deserialize(ByteArrayBuffer buffer);
	
	/**
	 * Message toString method for watch properties of message 
	 */
	public abstract String toString();
}
