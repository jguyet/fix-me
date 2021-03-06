package org.fixme.core.protocol.messages;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.protocol.types.WalletObject;
import org.fixme.core.utils.Json;

@AnnotationMessageID(WalletContentMessage.MESSAGE_ID)
public class WalletContentMessage extends NetworkMessage {
	
	public static final int MESSAGE_ID = 1009;
	
	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	
	public int			brokerId;
	
	public int			marketId;
	
	public WalletObject	wallet;
	
	public float		in_orders;
	
	//##############################################################################
	//@CONSTRUCTOR SECTION -------------------------------------------------------->
	//##############################################################################
	
	public WalletContentMessage(Json buffer) {
		super(buffer);
	}
	
	public WalletContentMessage(int brokerId, int marketId, WalletObject wallet, float in_orders) {
		this.brokerId = brokerId;
		this.marketId = marketId;
		this.wallet = wallet;
		this.in_orders = in_orders;
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
		return "WalletContentMessage";
	}

	@Override
	public void serialize(Json buffer) {
		buffer.put("BROKERID", this.brokerId);
		buffer.put("MARKETID", this.marketId);
		Json w = new Json();
		this.wallet.serialize(w);
		buffer.put("WALLET", w);
		buffer.put("IN_ORDERS", this.in_orders);
	}

	@Override
	public void deserialize(Json buffer) {
		this.brokerId = buffer.getInt("BROKERID");
		this.marketId = buffer.getInt("MARKETID");
		WalletObject w = new WalletObject();
		w.deserialize(buffer.getJson("WALLET"));
		this.wallet = w;
		this.in_orders = buffer.getFloat("IN_ORDERS");
	}

	@Override
	public String toString() {
		Json json = new Json();
		this.serialize(json);
		json.put("CLASS", this.getName());
		return json.toString();
	}
}
