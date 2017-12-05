package org.fixme.router.servers;

import org.fixme.core.IASynchronousSocketChannelHandler;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.protocol.message.AttributeUniqueIdentifiantMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Broker server handler
 * @author jguyet
 */
public class BrokerServerHandler implements IASynchronousSocketChannelHandler {
	
	private static Logger					logger = LoggerFactory.getLogger(BrokerServerHandler.class);

	/**
	 * BrokerServerHandler constructor
	 */
	public BrokerServerHandler() {
		//nothing to do
	}
	
	@Override
	public void onStartConnection(SocketChannel ch) {
		ch.write(new AttributeUniqueIdentifiantMessage(ch.getUid()));
		logger.info("Router - Accepte connection from Broker from {}", ch.getRemoteAddress());
	}

	@Override
	public void onMessageReceived(SocketChannel ch, NetworkMessage message) {
		logger.info("Router - Broker: BROKERID={}|MSGTYPE={}|MSGCONTENT={}", ch.getUid(), message.getName(), message.toString());
	}

	@Override
	public void onConnectionClosed(SocketChannel ch) {
		logger.info("Router - Disconnection Broker from {}", ch.getRemoteAddress());
	}
	
}
