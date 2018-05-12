package org.fixme.market.socket.handler;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.AttributeRouterUniqueIdentifiantMessage;
import org.fixme.core.reflection.handler.ClassMessageHandler;
import org.fixme.core.reflection.handler.MethodMessageHandler;

@ClassMessageHandler("StartConnectionHandler")
public class StartConnectionHandler {

	/**
	 * attributeUniqueIdentifiantMessageHandler(SocketChannel ch, AttributeRouterUniqueIdentifiantMessage message)<br>
	 * Method handle AttributeRouterUniqueIdentifiantMessage
	 * @param ch
	 * @param message
	 * @return boolean
	 */
	@MethodMessageHandler(AttributeRouterUniqueIdentifiantMessage.MESSAGE_ID)
	public static boolean attributeRouterUniqueIdentifiantMessageHandler(SocketChannel ch, AttributeRouterUniqueIdentifiantMessage message) {
		ch.setUid(message.id);
		return true;
	}
}
