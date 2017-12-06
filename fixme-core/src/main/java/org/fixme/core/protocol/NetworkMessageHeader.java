package org.fixme.core.protocol;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * NetworkMessageHeader class
 * @author jguyet
 * @Commented
 */
public class NetworkMessageHeader {

	//##############################
	//@PRIVATE VARIABLES SECTION -->
	//##############################
	
	private int id;
	
	@Max(value=1002)
	@Min(value=1000)
	private int messageId;
	
	@Min(value=0)
	@Max(value=2048)
	private int length;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public NetworkMessageHeader(int id, int messageId, int length) {
		this.id = id;
		this.messageId = messageId;
		this.length = length;
	}
	
	//##############################################################################
	//@GETTER SETTER SECTION ------------------------------------------------------>
	//##############################################################################
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int id) {
		this.messageId = id;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
