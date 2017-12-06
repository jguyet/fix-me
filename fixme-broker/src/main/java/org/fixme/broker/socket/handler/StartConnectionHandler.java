package org.fixme.broker.socket.handler;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.messages.AttributeUniqueIdentifiantMessage;
import org.fixme.core.protocol.messages.BuyInstrumentMessage;
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
	 * attributeUniqueIdentifianMessageHandler(SocketChannel ch, AttributeUniqueIdentifiantMessage message)<br>
	 * Method handle AttributeUniqueIdentifiantMessage
	 * @param ch
	 * @param message
	 * @return boolean
	 */
	@MethodMessageHandler(AttributeUniqueIdentifiantMessage.MESSAGE_ID)
	public static boolean attributeUniqueIdentifianMessageHandler(SocketChannel ch, AttributeUniqueIdentifiantMessage message) {
		ch.setUid(message.id);
		
		BuyInstrumentMessage buymessage = new BuyInstrumentMessage(ch.getUid(), "VIOLON", 1, "MSE", 500);
		ch.write(buymessage);
		return true;
	}
}
