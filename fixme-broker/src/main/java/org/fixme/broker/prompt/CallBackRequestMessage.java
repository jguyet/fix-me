package org.fixme.broker.prompt;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;

public interface CallBackRequestMessage {

	public void onExecutedRequest(SocketChannel channel, NetworkMessage message);
	
	public void onRejectedRequest(SocketChannel channel, NetworkMessage message);
}
