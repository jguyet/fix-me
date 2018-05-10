package org.fixme.core.protocol.messages;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.utils.Json;

@AnnotationMessageID(BuyOrderMessage.MESSAGE_ID)
public class BuyOrderMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1002;

	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	
	public int			brokerId;
	
	public int			marketId;
	
	public String		instrument;
	
	public float		quantity;
	
	public float		price;
	
	public String		wallet_from;
	
	public String		wallet_to;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public BuyOrderMessage(Json buffer) {
		super(buffer);
	}
	
	public BuyOrderMessage(int brokerId, int marketId, String instrument, float quantity, float price, String wallet_from, String wallet_to) {
		this.brokerId = brokerId;
		this.marketId = marketId;
		this.instrument = instrument;
		this.quantity = quantity;
		this.price = price;
		this.wallet_from = wallet_from;
		this.wallet_to = wallet_to;
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
		return "BuyOrderMessage";
	}

	@Override
	public void serialize(Json buffer) {
		
		buffer.put("BROKERID", this.brokerId);
		buffer.put("MARKETID", this.marketId);
		buffer.put("INSTRUMENT", this.instrument);
		buffer.put("QUANTITY", this.quantity);
		buffer.put("PRICE", this.price);
		buffer.put("WALLET_FROM", this.wallet_from);
		buffer.put("WALLET_TO", this.wallet_to);
	}

	@Override
	public void deserialize(Json buffer) {
		
		this.brokerId = buffer.getInt("BROKERID");
		this.marketId = buffer.getInt("MARKETID");
		this.instrument = buffer.getString("INSTRUMENT");
		this.quantity = buffer.getFloat("QUANTITY");
		this.price = buffer.getFloat("PRICE");
		this.wallet_from = buffer.getString("WALLET_FROM");
		this.wallet_to = buffer.getString("WALLET_TO");
	}

	@Override
	public String toString() {
		Json json = new Json();
		this.serialize(json);
		json.put("CLASS", this.getName());
		return json.toString();
	}

}
