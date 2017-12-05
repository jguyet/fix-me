package org.fixme.broker.socket.handler;

import org.fixme.broker.socket.reflection.BrokerClassMessageHandler;
import org.fixme.broker.socket.reflection.BrokerMethodMessageHandler;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.message.AttributeUniqueIdentifiantMessage;
import org.fixme.core.protocol.message.BuyInstrumentMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@BrokerClassMessageHandler("StartConnectionHandler")
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
	@BrokerMethodMessageHandler(AttributeUniqueIdentifiantMessage.MESSAGE_ID)
	public static boolean attributeUniqueIdentifianMessageHandler(SocketChannel ch, AttributeUniqueIdentifiantMessage message) {
		ch.setUid(message.id);
		
		logger.info("Broker - Router - attributeUniqueIdentifianMessageHandler {}", message.id);
		
		BuyInstrumentMessage buymessage = new BuyInstrumentMessage(ch.getUid(), "VIOLON", 1, "MSE", 500);
		ch.write(buymessage);
		return true;
	}
}
