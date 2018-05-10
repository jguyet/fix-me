package org.fixme.core.protocol.messages;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.protocol.types.MarketObject;
import org.fixme.core.utils.Json;
import org.json.JSONArray;

@AnnotationMessageID(MarketDataMessage.MESSAGE_ID)
public class MarketDataMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1007;
	
	public int	brokerId;
	public int	marketId;
	public MarketObject[] market_objects;
	
	@Override
	public int messageId() {
		return MESSAGE_ID;
	}
	
	public MarketDataMessage(Json buffer) {
		super(buffer);
	}
	
	public MarketDataMessage(int brokerId, int marketId, MarketObject[] market_objects) {
		this.brokerId = brokerId;
		this.marketId = marketId;
		this.market_objects = market_objects;
	}

	@Override
	public String getName() {
		return "MarketDataMessage";
	}

	@Override
	public void serialize(Json buffer) {
		buffer.put("BROKER_ID", this.brokerId);
		buffer.put("MARKET_ID", this.marketId);
		buffer.put("MARKET_LIST", new JSONArray());
		for (int i = 0; i < market_objects.length; i++) {
			Json j = new Json();
			market_objects[i].serialize(j);
			buffer.append("MARKET_LIST", j.getJSONObject());
		}
	}

	@Override
	public void deserialize(Json buffer) {
		this.brokerId = buffer.getInt("BROKER_ID");
		this.marketId = buffer.getInt("MARKET_ID");
		
		JSONArray array = buffer.getJSONArray("MARKET_LIST");
		market_objects = new MarketObject[array.length()];
		for (int i = 0; i < array.length(); i++) {
			Json j = new Json(array.getJSONObject(i));
			MarketObject m = new MarketObject();
			m.marketID = this.marketId;
			m.deserialize(j);
			market_objects[i] = m;
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
