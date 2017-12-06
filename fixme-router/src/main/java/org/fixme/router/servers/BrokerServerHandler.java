package org.fixme.router.servers;

import org.fixme.core.IASynchronousSocketChannelHandler;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.protocol.messages.AttributeUniqueIdentifiantMessage;
import org.fixme.router.RouterProperties;
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
		logger.info("{} - Broker: Accepte connection from {}", RouterProperties.MODULE_NAME, ch.getRemoteAddress());
	}

	@Override
	public void onMessageReceived(SocketChannel ch, NetworkMessage message) {
		logger.info("{} - Broker: BROKERID={}|MSGTYPE={}|MSGCONTENT={}|CHECKSUM={}", RouterProperties.MODULE_NAME, ch.getUid(), message.getName(), message.toString(), message.getCheckSum());
	}

	@Override
	public void onConnectionClosed(SocketChannel ch) {
		logger.info("{} - Broker: Disconnection from {}", RouterProperties.MODULE_NAME, ch.getRemoteAddress());
	}
	
}
