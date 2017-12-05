package org.fixme.broker.server;

import org.fixme.broker.App;
import org.fixme.core.FixmeSocketChannelManager;
import org.fixme.core.IASynchronousSocketChannelHandler;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;

public class BrokerSocket implements IASynchronousSocketChannelHandler{

	private FixmeSocketChannelManager channel;
	
	public BrokerSocket() {
		this.channel = new FixmeSocketChannelManager("127.0.0.1", 5000, this);
	}
	
	public void intialize() {
		this.channel.initialize();
	}
	
	@Override
	public void onStartConnection(SocketChannel ch) {
		this.channel.startOnReadSocketChannel();
	}

	@Override
	public void onMessageReceived(SocketChannel ch, NetworkMessage message) {
		System.out.println(message.messageId());
	}

	@Override
	public void onConnectionClosed(SocketChannel ch) {
		App.stopped = true;
	}

}
