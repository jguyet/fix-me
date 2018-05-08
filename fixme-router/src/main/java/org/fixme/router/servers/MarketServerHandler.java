package org.fixme.router.servers;

import java.util.HashMap;
import java.util.Map;

import org.fixme.core.IASynchronousSocketChannelHandler;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.protocol.messages.AttributeRouterUniqueIdentifiantMessage;
import org.fixme.router.RouterProperties;
import org.fixme.router.routing.Route;
import org.fixme.router.routing.RoutingTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Market Handler
 * @author jguyet
 */
public class MarketServerHandler implements IASynchronousSocketChannelHandler {

	private static Logger						logger = LoggerFactory.getLogger(MarketServerHandler.class);
	
	public static Map<Integer, SocketChannel>	markets = new HashMap<Integer, SocketChannel>();
	public static RoutingTable					marketRoutingTable = new RoutingTable();
	public static int							MARKET_UIDS = 0;
	
	/**
	 * MarketServerHandler constructor
	 */
	public MarketServerHandler() {
		//nothing to do
	}

	@Override
	public void onStartConnection(SocketChannel ch) {
		ch.setUid(++MARKET_UIDS);
		ch.write(new AttributeRouterUniqueIdentifiantMessage(ch.getUid()));
		markets.put(ch.getUid(), ch);
		marketRoutingTable.addRoute(new Route(ch));
		logger.info("{} - Market: Accepte connection from {}", RouterProperties.MODULE_NAME, ch.getRemoteAddress());
	}

	@Override
	public void onMessageReceived(SocketChannel ch, NetworkMessage message) {
		boolean handled = MarketSocketServerMessageHandlerFactory.handleMessage(ch, message);
		
		logger.info("{} - Market: New message MARKETID={}|MSGTYPE={}|MSGCONTENT({})|CHECKSUM={}|HANDLED={}", RouterProperties.MODULE_NAME, ch.getUid(), message.getName(), message.toString(), message.getCheckSum(), handled);
	}

	@Override
	public void onConnectionClosed(SocketChannel ch) {
		logger.error("{} - Market: Disconnection from {}|MARKETID={}", RouterProperties.MODULE_NAME, ch.getRemoteAddress(), ch.getUid());
		if (markets.containsKey(ch.getUid()))
			markets.remove(ch.getUid());
		marketRoutingTable.removeRoute(ch.getUid());
	}

	@Override
	public void onErrorJsonParser(SocketChannel ch) {
		//...
	}
}
