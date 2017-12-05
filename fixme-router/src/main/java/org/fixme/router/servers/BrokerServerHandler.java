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
		logger.info("new broker id({})", ch.getUid());
	}

	@Override
	public void onMessageReceived(SocketChannel ch, NetworkMessage message) {
		logger.info("new message on broker id({}) messageId {}", ch.getUid(), message.messageId());
	}

	@Override
	public void onConnectionClosed(SocketChannel ch) {
		logger.info("Connection broker closed id({})", ch.getUid());
	}
	
}
