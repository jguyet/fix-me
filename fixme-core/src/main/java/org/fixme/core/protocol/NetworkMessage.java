package org.fixme.core.protocol;

import org.fixme.core.protocol.utils.CheckSum;
import org.fixme.core.utils.Json;

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
	
	private Json 				jsonMessage;
	private String				checkSum;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public NetworkMessage(Json jsonMessage) {
		this.jsonMessage = jsonMessage;
	}
	
	public NetworkMessage() {
		this.jsonMessage = new Json();
	}
	
	//##############################################################################
	//@GETTER SETTER SECTION ------------------------------------------------------>
	//##############################################################################

	public String getCheckSum() {
		return this.checkSum;
	}
	
	//##############################################################################
	//@BUILD METHODS SECTION ------------------------------------------------------>
	//##############################################################################
	
	public void serialize_message() {
		
		this.jsonMessage = new Json();
		//WRITE HEADER
		NetworkMessageHeader header = new NetworkMessageHeader(this.messageId());
		NetworkProtocolMessage.writeHeader(header, this.jsonMessage);

		serialize(this.jsonMessage);
		
		//WRITE CHECKSUM
		this.jsonMessage.put("CHECKSUM", buildCheckSum());
	}
	
	public void deserialize_message() {
		//READ CHECKSUM
		this.checkSum = this.jsonMessage.getString("CHECKSUM");
		deserialize(this.jsonMessage);
	}
	
	/**
	 * Get on buffer all message properties out of Header and checksum<br>
	 * and generate checksum for futur check
	 * @return String
	 */
	public String buildCheckSum() {
		
		this.jsonMessage.remove("CHECKSUM");
		
		byte[] buffer = this.jsonMessage.toString().getBytes();
		
		return CheckSum.get(buffer);
	}
	
	public byte[] array() {
		
		return this.jsonMessage.toString().getBytes();
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
	public abstract void serialize(Json buffer);
	
	/**
	 * Message deserializer method<br>
	 * read buffer content for load message properties
	 * @param buffer
	 */
	public abstract void deserialize(Json buffer);
	
	/**
	 * Message toString method for watch properties of message 
	 */
	public abstract String toString();
}
