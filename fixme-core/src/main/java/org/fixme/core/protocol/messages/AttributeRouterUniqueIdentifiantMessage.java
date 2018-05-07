package org.fixme.core.protocol.messages;

import java.util.Map;

import javax.validation.constraints.Min;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.ByteArrayBuffer;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.utils.Json;

@AnnotationMessageID(AttributeRouterUniqueIdentifiantMessage.MESSAGE_ID)
public class AttributeRouterUniqueIdentifiantMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1000;
	
	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	
	@Min(value=0)
	public int id;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public AttributeRouterUniqueIdentifiantMessage(Json buffer) {
		super(buffer);
	}
	
	public AttributeRouterUniqueIdentifiantMessage(int id) {
		this.id = id;
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
		return "AttributeUniqueIdentifiantMessage";
	}
	
	@Override
	public void serialize(Json buffer) {
		buffer.put("ID", this.id);
	}

	@Override
	public void deserialize(Json buffer) {
		this.id = buffer.getInt("ID");
	}

	@Override
	public String toString() {
		return "ID=" + this.id;
	}

}
