package org.fixme.market.socket;

import org.fixme.core.FixmeSocketChannelManager;
import org.fixme.core.IASynchronousSocketChannelHandler;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.market.App;
import org.fixme.market.MarketProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketMarket implements IASynchronousSocketChannelHandler {
	/**
	 * LOGGER
	 */
	private static Logger				logger = LoggerFactory.getLogger(SocketMarket.class);
	
	private String						ip;
	private int							port;
	private FixmeSocketChannelManager	channel;
	
	public SocketMarket(String ip, int port) {
		this.ip = ip;
		this.port = port;
		this.channel = new FixmeSocketChannelManager(this.ip, this.port, this, MarketProperties.MODULE_NAME);
	}
	
	public void intialize() {
		this.channel.initialize();
	}
	
	@Override
	public void onStartConnection(SocketChannel ch) {
		logger.info("{} - Router: Connection estabilised on {}:{}", MarketProperties.MODULE_NAME, ip, port);
		this.channel.startOnReadSocketChannel();
	}

	@Override
	public void onMessageReceived(SocketChannel ch, NetworkMessage message) {
		boolean handled = SocketMarketHandlerFactory.handleMessage(ch, message);
		
		logger.info("{} - Router: ROUTERID={}|MSGTYPE={}|MSGCONTENT={}|CHECKSUM={}|HANDLED={}", MarketProperties.MODULE_NAME, ch.getUid(), message.getName(), message.toString(), message.getCheckSum(), handled);
	}

	@Override
	public void onConnectionClosed(SocketChannel ch) {
		App.stopped = true;
		if (ch.isOpen() != false)
			logger.info("{} - Router: Connection closed from {}", MarketProperties.MODULE_NAME, ch.getRemoteAddress());
	}
}
