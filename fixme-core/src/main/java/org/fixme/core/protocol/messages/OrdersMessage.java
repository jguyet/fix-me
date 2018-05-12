package org.fixme.core.protocol.messages;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.protocol.types.OrderObject;
import org.fixme.core.utils.Json;
import org.json.JSONArray;

@AnnotationMessageID(OrdersMessage.MESSAGE_ID)
public class OrdersMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1013;
	
	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	public int brokerId;
	
	public int marketId;
	
	public String market;
	
	public OrderObject[] bids;
	
	public OrderObject[] asks;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public OrdersMessage(Json buffer) {
		super(buffer);
	}
	
	public OrdersMessage(int brokerId, int marketId, String market, OrderObject[] bids, OrderObject[] asks) {
		this.brokerId = brokerId;
		this.marketId = marketId;
		this.market = market;
		this.bids = bids;
		this.asks = asks;
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
		return "OrdersMessage";
	}

	@Override
	public void serialize(Json buffer) {
		buffer.put("BROKERID", this.brokerId);
		buffer.put("MARKETID", this.marketId);
		buffer.put("MARKET", this.market);
		buffer.put("BIDS", new JSONArray());
		buffer.put("ASKS", new JSONArray());
		for (OrderObject o : this.bids) {
			Json b = new Json();
			o.serialize(b);
			buffer.append("BIDS", b.getJSONObject());
		}
		for (OrderObject o : this.asks) {
			Json b = new Json();
			o.serialize(b);
			buffer.append("ASKS", b.getJSONObject());
		}
	}

	@Override
	public void deserialize(Json buffer) {
		this.brokerId = buffer.getInt("BROKERID");
		this.marketId = buffer.getInt("MARKETID");
		this.market = buffer.getString("MARKET");
		
		JSONArray arraybids = buffer.getJSONArray("BIDS");
		this.bids = new OrderObject[arraybids.length()];
		for (int i = 0; i < arraybids.length(); i++) {
			Json j = new Json(arraybids.getJSONObject(i));
			OrderObject m = new OrderObject();
			m.deserialize(j);
			this.bids[i] = m;
		}
		
		JSONArray arrayasks = buffer.getJSONArray("ASKS");
		this.asks = new OrderObject[arrayasks.length()];
		for (int i = 0; i < arrayasks.length(); i++) {
			Json j = new Json(arrayasks.getJSONObject(i));
			OrderObject m = new OrderObject();
			m.deserialize(j);
			this.asks[i] = m;
		}
	}

	@Override
	public String toString() {
		Json json = new Json();
		this.serialize(json);
		json.put("CLASS", this.getName());
		return json.toString();
	}

}
