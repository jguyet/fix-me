package org.fixme.broker.socket.handler;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.ExecutedRequestMessage;
import org.fixme.core.protocol.messages.RejectedRequestMessage;
import org.fixme.core.reflection.handler.ClassMessageHandler;
import org.fixme.core.reflection.handler.MethodMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transaction methods handler
 * @author jguyet
 */
@ClassMessageHandler("TransactionBrokerHandler")
public class TransactionBrokerHandler {
	
	/**
	 * LOGGER
	 */
	private static Logger logger = LoggerFactory.getLogger(TransactionBrokerHandler.class);
	

	/**
	 * on market buy or sell rejected
	 * @param channel
	 * @param message
	 * @return
	 */
	@MethodMessageHandler(ExecutedRequestMessage.MESSAGE_ID)
	public static boolean onTransactionMessageExecuted(SocketChannel channel, ExecutedRequestMessage message) {
		
		return true;
	}
	
	/**
	 * on market buy or sell rejected
	 * @param channel
	 * @param message
	 * @return
	 */
	@MethodMessageHandler(RejectedRequestMessage.MESSAGE_ID)
	public static boolean onTransactionMessageRejected(SocketChannel channel, RejectedRequestMessage message) {
		
		return true;
	}
}
