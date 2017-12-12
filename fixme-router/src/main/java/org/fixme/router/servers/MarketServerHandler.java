package org.fixme.router.servers;

import org.fixme.core.IASynchronousSocketChannelHandler;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.protocol.messages.AttributeRouterUniqueIdentifiantMessage;
import org.fixme.router.Route;
import org.fixme.router.RouterProperties;
import org.fixme.router.RoutingTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Market Handler
 * @author jguyet
 */
public class MarketServerHandler implements IASynchronousSocketChannelHandler {

	private static Logger					logger = LoggerFactory.getLogger(MarketServerHandler.class);
	
	public static RoutingTable				marketRoutingTable = new RoutingTable();
	
	/**
	 * MarketServerHandler constructor
	 */
	public MarketServerHandler() {
		//nothing to do
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStartConnection(SocketChannel ch) {
		
		//create route for market
		int routeId = marketRoutingTable.addRoute(new Route(ch));
		//set route to market
		ch.setRouteId(routeId);
		ch.write(new AttributeRouterUniqueIdentifiantMessage(ch.getUid()));
		logger.info("{} - Market: Accepte connection from {}", RouterProperties.MODULE_NAME, ch.getRemoteAddress());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onMessageReceived(SocketChannel ch, NetworkMessage message) {
		boolean handled = MarketSocketServerMessageHandlerFactory.handleMessage(ch, message);
		
		logger.info("{} - Market: New message INTERNALID={}|RID={}|MSGTYPE={}|MSGCONTENT({})|CHECKSUM={}|HANDLED={}", RouterProperties.MODULE_NAME, ch.getUid(), ch.getRouteId(), message.getName(), message.toString(), message.getCheckSum(), handled);
	}

	@Override
	public void onConnectionClosed(SocketChannel ch) {
		logger.info("{} - Market: Disconnection from {}", RouterProperties.MODULE_NAME, ch.getRemoteAddress());
	}
}
