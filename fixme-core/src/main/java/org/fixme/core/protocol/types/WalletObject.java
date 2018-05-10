package org.fixme.core.protocol.types;

import org.fixme.core.database.collections.BaseCollection;
import org.fixme.core.protocol.INetworkType;
import org.fixme.core.utils.Json;

import com.google.code.morphia.annotations.Property;

public class WalletObject extends BaseCollection implements INetworkType {

		//##############################
		//@PRIVATE VARIABLES SECTION -->
		//##############################
		
		@Property("wallet")
		public String	wallet;
		
		@Property("instrument")
		public String	instrument;
		
		@Property("quantity")
		public float	quantity;
		
		//##############################################################################
		//@CONTRUCTOR SECTION --------------------------------------------------------->
		//##############################################################################
		
		public WalletObject() { }
		
		public WalletObject(String wallet, String instrument, float quantity) {
			this.wallet = wallet;
			this.instrument = instrument;
			this.quantity = quantity;
		}
		
		//##############################################################################
		//@METHOD SECTION ------------------------------------------------------------->
		//##############################################################################

		@Override
		public void serialize(Json buffer) {
			buffer.put("WALLET", this.wallet);
			buffer.put("INSTRUMENT", instrument);
			buffer.put("QUANTITY", this.quantity);
		}

		@Override
		public void deserialize(Json buffer) {
			this.wallet = buffer.getString("WALLET");
			this.instrument = buffer.getString("INSTRUMENT");
			this.quantity = buffer.getFloat("QUANTITY");
		}
		
		@Override
		public String toString() {
			Json json = new Json();
			this.serialize(json);
			return json.toString();
		}
}
