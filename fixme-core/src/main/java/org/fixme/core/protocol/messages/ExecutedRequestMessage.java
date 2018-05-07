package org.fixme.core.protocol.messages;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.utils.Json;

@AnnotationMessageID(ExecutedRequestMessage.MESSAGE_ID)
public class ExecutedRequestMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1004;
	
	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	public int brokerId;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public ExecutedRequestMessage(Json buffer) {
		super(buffer);
	}
	
	public ExecutedRequestMessage(int brokerId) {
		this.brokerId = brokerId;
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
		return "ExecutedRequestMessage";
	}

	@Override
	public void serialize(Json buffer) {
		buffer.put("BROKERID", this.brokerId);
	}

	@Override
	public void deserialize(Json buffer) {
		this.brokerId = buffer.getInt("BROKERID");
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("BROKERID=").append(this.brokerId);
		
		return str.toString();
	}

}
