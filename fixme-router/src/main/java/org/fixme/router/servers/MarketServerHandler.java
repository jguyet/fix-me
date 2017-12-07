package org.fixme.router.servers;

import org.fixme.core.IASynchronousSocketChannelHandler;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.protocol.messages.AttributeRouterUniqueIdentifiantMessage;
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
		ch.write(new AttributeRouterUniqueIdentifiantMessage(ch.getUid()));
		logger.info("{} - Market: Accepte connection from {}", RouterProperties.MODULE_NAME, ch.getRemoteAddress());
	}

	@Override
	public void onMessageReceived(SocketChannel ch, NetworkMessage message) {
		boolean handled = MarketSocketServerMessageHandlerFactory.handleMessage(ch, message);
		
		logger.info("{} - Market: New message RID={}|MSGTYPE={}|MSGCONTENT({})|CHECKSUM={}|HANDLED={}", RouterProperties.MODULE_NAME, ch.getUid(), message.getName(), message.toString(), message.getCheckSum(), handled);
	}

	@Override
	public void onConnectionClosed(SocketChannel ch) {
		logger.info("{} - Market: Disconnection from {}", RouterProperties.MODULE_NAME, ch.getRemoteAddress());
	}
}
