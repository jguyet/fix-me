package org.fixme.router.servers;

import org.fixme.core.IASynchronousSocketChannelHandler;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.protocol.messages.AttributeUniqueIdentifiantMessage;
import org.fixme.router.RouterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Market Handler
 * @author jguyet
 */
public class MarketServerHandler implements IASynchronousSocketChannelHandler {

	private static Logger					logger = LoggerFactory.getLogger(MarketServerHandler.class);
	
	/**
	 * MarketServerHandler constructor
	 */
	public MarketServerHandler() {
		//nothing to do
	}

	@Override
	public void onStartConnection(SocketChannel ch) {
		ch.write(new AttributeUniqueIdentifiantMessage(ch.getUid()));
		logger.info("{} - Market: Accepte connection from {}", RouterProperties.MODULE_NAME, ch.getRemoteAddress());
	}

	@Override
	public void onMessageReceived(SocketChannel ch, NetworkMessage message) {
		logger.info("{} - Market: MARKETID={}|MSGTYPE={}|MSGCONTENT={}|CHECKSUM={}", RouterProperties.MODULE_NAME, ch.getUid(), message.getName(), message.toString(), message.getCheckSum());
	}

	@Override
	public void onConnectionClosed(SocketChannel ch) {
		logger.info("{} - Market: Disconnection from {}", RouterProperties.MODULE_NAME, ch.getRemoteAddress());
	}
}
