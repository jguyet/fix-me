package org.fixme.core.protocol.messages;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.utils.Json;

@AnnotationMessageID(GetOrdersMessage.MESSAGE_ID)
public class GetOrdersMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1012;
	
	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	public int brokerId;
	
	public int marketId;
	
	public String market;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public GetOrdersMessage(Json buffer) {
		super(buffer);
	}
	
	public GetOrdersMessage(int brokerId, int marketId, String market) {
		this.brokerId = brokerId;
		this.marketId = marketId;
		this.market = market;
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
		return "GetOrdersMessage";
	}

	@Override
	public void serialize(Json buffer) {
		buffer.put("BROKERID", this.brokerId);
		buffer.put("MARKETID", this.marketId);
		buffer.put("MARKET", this.market);
	}

	@Override
	public void deserialize(Json buffer) {
		this.brokerId = buffer.getInt("BROKERID");
		this.marketId = buffer.getInt("MARKETID");
		this.market = buffer.getString("MARKET");
	}

	@Override
	public String toString() {
		Json json = new Json();
		this.serialize(json);
		json.put("CLASS", this.getName());
		return json.toString();
	}

}