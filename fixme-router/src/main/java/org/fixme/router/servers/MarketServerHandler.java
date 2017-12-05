package org.fixme.router.servers;

import org.fixme.core.IASynchronousSocketChannelHandler;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
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
		
		logger.info("new market id({})", ch.getUid());
	}

	@Override
	public void onMessageReceived(SocketChannel ch, NetworkMessage message) {
		logger.info("new message on market id({}) messageId {}", ch.getUid(), message.messageId());
	}

	@Override
	public void onConnectionClosed(SocketChannel ch) {
		logger.info("Connection market closed id({})", ch.getUid());
	}
}
