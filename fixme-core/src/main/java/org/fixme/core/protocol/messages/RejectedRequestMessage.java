package org.fixme.core.protocol.messages;

import org.fixme.core.protocol.AnnotationMessageID;
import org.fixme.core.protocol.ByteArrayBuffer;
import org.fixme.core.protocol.NetworkMessage;

@AnnotationMessageID(RejectedRequestMessage.MESSAGE_ID)
public class RejectedRequestMessage extends NetworkMessage{

	public static final int MESSAGE_ID = 1005;
	
	//##############################
	//@VARIABLES SECTION ---------->
	//##############################
	public int brokerId;
	
	//##############################################################################
	//@CONSTRUCTOR SECTION -------------------------------------------------------->
	//##############################################################################
	
	public RejectedRequestMessage(ByteArrayBuffer buffer) {
		super(buffer);
	}
	
	public RejectedRequestMessage(int brokerId) {
		this.brokerId = brokerId;
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
		return "RejectedRequestMessage";
	}

	@Override
	public void serialize(ByteArrayBuffer buffer) {
		buffer.writeInt(this.brokerId);
	}

	@Override
	public void deserialize(ByteArrayBuffer buffer) {
		this.brokerId = buffer.readInt();
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("BROKERID=").append(this.brokerId);
		
		return str.toString();
	}
	
}
