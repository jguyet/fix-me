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
 * Broker server handler
 * @author jguyet
 */
public class BrokerServerHandler implements IASynchronousSocketChannelHandler {
	
	private static Logger					logger = LoggerFactory.getLogger(BrokerServerHandler.class);

	public static RoutingTable				brokerRoutingTable = new RoutingTable();

	/**
	 * BrokerServerHandler constructor
	 */
	public BrokerServerHandler() {
		//nothing to do
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStartConnection(SocketChannel ch) {
		
		//set default route to channel
		ch.setRouteId(RouterProperties.DEFAULT_ROUTE_IDENTIFIANT);
		//send route ID
		ch.write(new AttributeRouterUniqueIdentifiantMessage(ch.getUid()));
		
		logger.info("{} - Broker: Accepte connection from {}", RouterProperties.MODULE_NAME, ch.getRemoteAddress());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onMessageReceived(SocketChannel ch, NetworkMessage message) {
		
		if (message.getmindId() == RouterProperties.DEFAULT_ROUTE_IDENTIFIANT) {
			boolean handled = BrokerSocketServerMessageHandlerFactory.handleMessage(ch, message);
			
			logger.info("{} - Broker: New message INTERNALID={}|RID={}|MSGTYPE={}|MSGCONTENT({})|CHECKSUM={}|HANDLED={}", RouterProperties.MODULE_NAME, ch.getUid(),  ch.getRouteId(), message.getName(), message.toString(), message.getCheckSum(), handled);
		} else {
			//TODO get Route on RoutingTable
		}
	}

	@Override
	public void onConnectionClosed(SocketChannel ch) {
		logger.info("{} - Broker: Disconnection from {}", RouterProperties.MODULE_NAME, ch.getRemoteAddress());
	}
	
}
