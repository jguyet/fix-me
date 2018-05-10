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
	
	public String response;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public ExecutedRequestMessage(Json buffer) {
		super(buffer);
	}
	
	public ExecutedRequestMessage(int brokerId, String response) {
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
		return "ExecutedRequestMessage";
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
		Json json = new Json();
		this.serialize(json);
		json.put("CLASS", this.getName());
		return json.toString();
	}

}
