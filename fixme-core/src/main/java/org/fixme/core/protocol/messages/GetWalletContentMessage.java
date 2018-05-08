package org.fixme.core.protocol.messages;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.utils.Json;

@AnnotationMessageID(GetWalletContentMessage.MESSAGE_ID)
public class GetWalletContentMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1008;
	
	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	public int brokerId;
	
	public int marketId;
	
	public String wallet;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public GetWalletContentMessage(Json buffer) {
		super(buffer);
	}
	
	public GetWalletContentMessage(int brokerId, int marketId, String wallet) {
		this.brokerId = brokerId;
		this.marketId = marketId;
		this.wallet = wallet;
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
		return "GetWalletContentMessage";
	}

	@Override
	public void serialize(Json buffer) {
		buffer.put("BROKERID", this.brokerId);
		buffer.put("MARKETID", this.marketId);
		buffer.put("WALLET", this.wallet);
	}

	@Override
	public void deserialize(Json buffer) {
		this.brokerId = buffer.getInt("BROKERID");
		this.marketId = buffer.getInt("MARKETID");
		this.wallet = buffer.getString("WALLET");
	}

	@Override
	public String toString() {
		Json json = new Json();
		this.serialize(json);
		return json.toString();
	}

}
