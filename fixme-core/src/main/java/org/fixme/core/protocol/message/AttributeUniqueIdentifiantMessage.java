package org.fixme.core.protocol.message;

import javax.validation.constraints.Min;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.ByteArrayBuffer;
import org.fixme.core.protocol.NetworkMessage;

@AnnotationMessageID(AttributeUniqueIdentifiantMessage.MESSAGE_ID)
public class AttributeUniqueIdentifiantMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1000;
	
	@Min(value=0)
	public int id;
	
	@Override
	public int messageId() {
		return MESSAGE_ID;
	}
	
	@Override
	public String getName() {
		return "AttributeUniqueIdentifiantMessage";
	}
	
	public AttributeUniqueIdentifiantMessage(ByteArrayBuffer buffer) {
		super(buffer);
	}
	
	public AttributeUniqueIdentifiantMessage(int id) {
		this.id = id;
	}
	
	@Override
	public void serialize(ByteArrayBuffer buffer) {
		buffer.putInt(this.id);
	}

	@Override
	public void deserialize(ByteArrayBuffer buffer) {
		this.id = buffer.readInt();
	}

	@Override
	public String toString() {
		return "ID=" + this.id;
	}

}
