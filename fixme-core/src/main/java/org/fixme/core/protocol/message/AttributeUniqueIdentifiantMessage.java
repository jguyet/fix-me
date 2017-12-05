package org.fixme.core.protocol.message;

import java.nio.ByteBuffer;

import javax.validation.constraints.Min;

import org.fixme.core.protocol.AnnotationMessageID;
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
	
	public AttributeUniqueIdentifiantMessage(int id) {
		super(null);
		this.id = id;
	}
	
	public AttributeUniqueIdentifiantMessage(ByteBuffer buffer) {
		super(buffer);
	}
	
	@Override
	public ByteBuffer serialize() {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		
		buffer.putInt(this.id);
		return buffer;
	}

	@Override
	public void deserialize() {
		this.id = this.buffer.getInt();
	}

}
