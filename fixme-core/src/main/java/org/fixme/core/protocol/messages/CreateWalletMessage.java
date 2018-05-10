package org.fixme.core.protocol.messages;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.utils.Json;

@AnnotationMessageID(CreateWalletMessage.MESSAGE_ID)
public class CreateWalletMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1010;
	
	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	public int		brokerId;
	
	public int		marketId;
	
	public String	instrument;
	
	public float	quantity;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public CreateWalletMessage(Json buffer) {
		super(buffer);
	}
	
	public CreateWalletMessage(int brokerId, int marketId, String instrument, float quantity) {
		this.brokerId = brokerId;
		this.marketId = marketId;
		this.instrument = instrument;
		this.quantity = quantity;
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
		return "CreateWalletMessage";
	}

	@Override
	public void serialize(Json buffer) {
		buffer.put("BROKERID", this.brokerId);
		buffer.put("MARKETID", this.marketId);
		buffer.put("INSTRUMENT", this.instrument);
		buffer.put("QUANTITY", this.quantity);
	}

	@Override
	public void deserialize(Json buffer) {
		this.brokerId = buffer.getInt("BROKERID");
		this.marketId = buffer.getInt("MARKETID");
		this.instrument = buffer.getString("INSTRUMENT");
		this.quantity = buffer.getFloat("QUANTITY");
	}

	@Override
	public String toString() {
		Json json = new Json();
		this.serialize(json);
		json.put("CLASS", this.getName());
		return json.toString();
	}

}
