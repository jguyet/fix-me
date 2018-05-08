package org.fixme.broker.socket;

import org.fixme.broker.Broker;
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
		Broker.initialized_connection = true;
		Broker.router = ch;
		this.channel.startOnReadSocketChannel();
	}

	@Override
	public void onMessageReceived(SocketChannel ch, NetworkMessage message) {
		boolean handled = SocketBrokerMessageHandlerFactory.handleMessage(ch, message);
		
		//logger.info("{} - Router: New message MSGTYPE={}|MSGCONTENT({})|CHECKSUM={}|HANDLED={}", BrokerProperties.MODULE_NAME, message.getName(), message.toString(), message.getCheckSum(), handled);
	}

	@Override
	public void onConnectionClosed(SocketChannel ch) {
		Broker.stopped = true;
		Broker.initialized_connection = false;
		Broker.router = null;
		if (ch.isOpen() != false)
			logger.info("{} - Router: Connection closed from {}", BrokerProperties.MODULE_NAME, ch.getRemoteAddress());
	}

	@Override
	public void onErrorJsonParser(SocketChannel ch) {
		//...
	}

}
