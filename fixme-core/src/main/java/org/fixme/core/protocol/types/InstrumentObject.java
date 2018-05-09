package org.fixme.core.protocol.types;

import org.fixme.core.database.collections.BaseCollection;
import org.fixme.core.protocol.INetworkType;
import org.fixme.core.utils.Json;

import com.google.code.morphia.annotations.Property;

public class InstrumentObject extends BaseCollection implements INetworkType {
	
	@Property("name")
	public String				name;
	
	@Property("fullname")
	public String				fullname;

	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public InstrumentObject() {}
	
	public InstrumentObject(String fullname, String name) {
		this.fullname = fullname;
		this.name = name;
	}
	
	//##############################################################################
	//@METHOD SECTION ------------------------------------------------------------->
	//##############################################################################

	@Override
	public void serialize(Json buffer) {
		buffer.put("FULLNAME", this.fullname);
		buffer.put("NAME", this.name);
	}

	@Override
	public void deserialize(Json buffer) {
		this.fullname = buffer.getString("FULLNAME");
		this.name = buffer.getString("NAME");
	}
	
	@Override
	public String toString() {
		Json json = new Json();
		this.serialize(json);
		return json.toString();
	}
}
