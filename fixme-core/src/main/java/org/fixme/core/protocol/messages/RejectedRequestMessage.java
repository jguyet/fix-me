package org.fixme.core.protocol.messages;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.utils.Json;

@AnnotationMessageID(RejectedRequestMessage.MESSAGE_ID)
public class RejectedRequestMessage extends NetworkMessage{

	public static final int MESSAGE_ID = 1005;
	
	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	public int brokerId;
	
	public String response;
	
	//##############################################################################
	//@CONSTRUCTOR SECTION -------------------------------------------------------->
	//##############################################################################
	
	public RejectedRequestMessage(Json buffer) {
		super(buffer);
	}
	
	public RejectedRequestMessage(int brokerId, String response) {
		this.brokerId = brokerId;
		this.response = response;
	}
	
	//##############################################################################
	//@METHODS SECTION ------------------------------------------------------------>
	//##############################################################################

	@Override
	public int messageId() {
		return MESSAGE_ID;
	}

	@Override
	public String getName() {
		return "RejectedRequestMessage";
	}

	@Override
	public void serialize(Json buffer) {
		buffer.put("BROKERID", this.brokerId);
		buffer.put("RESPONSE", this.response);
	}

	@Override
	public void deserialize(Json buffer) {
		this.brokerId = buffer.getInt("BROKERID");
		this.response = buffer.getString("RESPONSE");
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("BROKERID=").append(this.brokerId).append("|");
		str.append("RESPONSE=").append(this.response);
		
		return str.toString();
	}
	
}
