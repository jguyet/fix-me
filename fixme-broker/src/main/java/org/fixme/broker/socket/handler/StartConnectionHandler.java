package org.fixme.broker.socket.handler;

import org.fixme.broker.prompt.BrokerPrompt;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.AttributeRouterUniqueIdentifiantMessage;
import org.fixme.core.protocol.messages.MarketDataRequestMessage;
import org.fixme.core.reflection.handler.ClassMessageHandler;
import org.fixme.core.reflection.handler.MethodMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ClassMessageHandler("StartConnectionHandler")
public class StartConnectionHandler {
	
	/**
	 * LOGGER
	 */
	private static Logger logger = LoggerFactory.getLogger(StartConnectionHandler.class);

	/**
	 * attributeRouterUniqueIdentifiantMessageHandler(SocketChannel ch, AttributeRouterUniqueIdentifiantMessage message)<br>
	 * Method handle AttributeRouterUniqueIdentifiantMessage
	 * @param ch
	 * @param message
	 * @return boolean
	 */
	@MethodMessageHandler(AttributeRouterUniqueIdentifiantMessage.MESSAGE_ID)
	public static boolean attributeRouterUniqueIdentifiantMessageHandler(BrokerPrompt prompt, SocketChannel ch, AttributeRouterUniqueIdentifiantMessage message) {
		ch.setUid(message.id);
		prompt.setUid(message.id);
		return true;
	}
}
