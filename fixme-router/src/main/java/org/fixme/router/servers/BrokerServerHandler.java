package org.fixme.router.servers;

import org.fixme.core.IASynchronousSocketChannelHandler;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.protocol.messages.AttributeRouterUniqueIdentifiantMessage;
import org.fixme.core.protocol.messages.RejectedRequestMessage;
import org.fixme.router.RouterProperties;
import org.fixme.router.routing.Route;
import org.fixme.router.routing.RoutingTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Broker server handler
 * @author jguyet
 */
public class BrokerServerHandler implements IASynchronousSocketChannelHandler {
	
	private static Logger					logger = LoggerFactory.getLogger(BrokerServerHandler.class);

	public static RoutingTable				brokerRoutingTable = new RoutingTable();
	public static int						BROKER_UIDS = 0;

	/**
	 * BrokerServerHandler constructor
	 */
	public BrokerServerHandler() {
		//nothing to do
	}
	
	@Override
	public void onStartConnection(SocketChannel ch) {
		ch.setUid(++BROKER_UIDS);
		brokerRoutingTable.addRoute(new Route(ch));
		ch.write(new AttributeRouterUniqueIdentifiantMessage(ch.getUid()));
		logger.info("{} - Broker: Accepte connection from {}", RouterProperties.MODULE_NAME, ch.getRemoteAddress());
	}

	@Override
	public void onMessageReceived(SocketChannel ch, NetworkMessage message) {
		boolean handled = BrokerSocketServerMessageHandlerFactory.handleMessage(ch, message);
		
		logger.info("{} - Broker: New message BROKERID={}|MSGTYPE={}|MSGCONTENT({})|CHECKSUM={}|HANDLED={}", RouterProperties.MODULE_NAME, ch.getUid(), message.getName(), message.toString(), message.getCheckSum(), handled);
	}

	@Override
	public void onConnectionClosed(SocketChannel ch) {
		logger.info("{} - Broker: Disconnection from {}|BROKERID={}", RouterProperties.MODULE_NAME, ch.getRemoteAddress(), ch.getUid());
		brokerRoutingTable.removeRoute(ch.getUid());
	}

	@Override
	public void onErrorJsonParser(SocketChannel ch) {
		logger.error("{} - Broker: Error Json parser from {}|BROKERID={}", RouterProperties.MODULE_NAME, ch.getRemoteAddress(), ch.getUid());
		ch.write(new RejectedRequestMessage(ch.getUid(), "Json syntax error."));
	}
	
}
