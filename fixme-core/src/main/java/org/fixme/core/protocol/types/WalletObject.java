package org.fixme.core.protocol.types;

import org.fixme.core.database.collections.BaseCollection;
import org.fixme.core.protocol.INetworkType;
import org.fixme.core.utils.Json;

import com.google.code.morphia.annotations.Property;

public class WalletObject extends BaseCollection implements INetworkType {

	@Property("currency")
	private String			currency;
	
	@Property("quantity")
	private float			quantity = 0.0f;
	
	public WalletObject() {
	}
	
	@Override
	public void serialize(Json buffer) {
		
	}

	@Override
	public void deserialize(Json buffer) {
		
	}

}
