package org.fixme.core.protocol.messages;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.protocol.types.WalletObject;
import org.fixme.core.utils.Json;

@AnnotationMessageID(NewWalletMessage.MESSAGE_ID)
public class NewWalletMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1011;
	
	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	public int brokerId;
	
	public int marketId;
	
	public WalletObject wallet;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public NewWalletMessage(Json buffer) {
		super(buffer);
	}
	
	public NewWalletMessage(int brokerId, int marketId, WalletObject wallet) {
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
		return "NewWalletMessage";
	}

	@Override
	public void serialize(Json buffer) {
		buffer.put("BROKERID", this.brokerId);
		buffer.put("MARKETID", this.marketId);
		Json w = new Json();
		this.wallet.serialize(w);
		buffer.put("WALLET", w);
	}

	@Override
	public void deserialize(Json buffer) {
		this.brokerId = buffer.getInt("BROKERID");
		this.marketId = buffer.getInt("MARKETID");
		WalletObject w = new WalletObject();
		w.deserialize(buffer.getJson("WALLET"));
		this.wallet = w;
	}

	@Override
	public String toString() {
		Json json = new Json();
		this.serialize(json);
		json.put("CLASS", this.getName());
		return json.toString();
	}

}
