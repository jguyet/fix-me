package org.fixme.broker.socket;

import org.fixme.broker.App;
import org.fixme.broker.BrokerProperties;
import org.fixme.core.FixmeSocketChannelManager;
import org.fixme.core.IASynchronousSocketChannelHandler;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketBroker implements IASynchronousSocketChannelHandler {

	//##############################
	//@STATICS VARIABLES SECTION -->
	//##############################
	
	private static Logger				logger = LoggerFactory.getLogger(SocketBroker.class);
	
	//##############################
	//@PRIVATE VARIABLES SECTION -->
	//##############################
	
	private String						ip;
	private int							port;
	private FixmeSocketChannelManager	channel;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public SocketBroker(String ip, int port) {
		this.ip = ip;
		this.port = port;
		this.channel = new FixmeSocketChannelManager(this.ip, this.port, this, BrokerProperties.MODULE_NAME);
	}
	
	public void intialize() {
		this.channel.initialize();
	}
	
	public void stop() {
		this.channel.stop();
	}
	
	//##############################################################################
	//@HANDLER SECTION ------------------------------------------------------------>
	//##############################################################################
	
	@Override
	public void onStartConnection(SocketChannel ch) {
		logger.info("{} - Router: Connection estabilised on {}:{}", BrokerProperties.MODULE_NAME, ip, port);
		this.channel.startOnReadSocketChannel();
	}

	@Override
	public void onMessageReceived(SocketChannel ch, NetworkMessage message) {
		boolean handled = SocketBrokerMessageHandlerFactory.handleMessage(ch, message);
		
		logger.info("{} - Router: New message RID={}|MSGTYPE={}|MSGCONTENT({})|CHECKSUM={}|HANDLED={}", BrokerProperties.MODULE_NAME, ch.getUid(), message.getName(), message.toString(), message.getCheckSum(), handled);
	}

	@Override
	public void onConnectionClosed(SocketChannel ch) {
		App.stopped = true;
		if (ch.isOpen() != false)
			logger.info("{} - Router: Connection closed from {}", BrokerProperties.MODULE_NAME, ch.getRemoteAddress());
	}

}
