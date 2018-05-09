package org.fixme.core.protocol.messages;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.utils.Json;

@AnnotationMessageID(SellOrderMessage.MESSAGE_ID)
public class SellOrderMessage extends NetworkMessage {
	
	public static final int MESSAGE_ID = 1003;
	
	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	
	public int			brokerId;
	
	public int			marketId;
	
	public String		instrument;
	
	public float		quantity;
	
	public float		price;
	
	public String		walletSeller;
	
	public String		walletBuyer;
	
	//##############################################################################
	//@CONSTRUCTOR SECTION -------------------------------------------------------->
	//##############################################################################
	
	public SellOrderMessage(Json buffer) {
		super(buffer);
	}
	
	public SellOrderMessage(int brokerId, int marketId, String instrument, float quantity, float price, String walletSeller, String walletBuyer) {
		this.brokerId = brokerId;
		this.marketId = marketId;
		this.instrument = instrument;
		this.quantity = quantity;
		this.price = price;
		this.walletSeller = walletSeller;
		this.walletBuyer = walletBuyer;
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
		return "SellOrderMessage";
	}

	@Override
	public void serialize(Json buffer) {
		buffer.put("BROKERID", this.brokerId);
		buffer.put("MARKETID", this.marketId);
		buffer.put("INSTRUMENT", this.instrument);
		buffer.put("QUANTITY", this.quantity);
		buffer.put("PRICE", this.price);
		buffer.put("WALLET_SELLER", this.walletSeller);
		buffer.put("WALLET_BUYER", this.walletBuyer);
	}

	@Override
	public void deserialize(Json buffer) {
		this.brokerId = buffer.getInt("BROKERID");
		this.marketId = buffer.getInt("MARKETID");
		this.instrument = buffer.getString("INSTRUMENT");
		this.quantity = buffer.getFloat("QUANTITY");
		this.price = buffer.getFloat("PRICE");
		this.walletSeller = buffer.getString("WALLET_SELLER");
		this.walletBuyer = buffer.getString("WALLET_BUYER");
	}

	@Override
	public String toString() {
		Json json = new Json();
		this.serialize(json);
		return json.toString();
	}

}
