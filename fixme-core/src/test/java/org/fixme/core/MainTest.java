package org.fixme.core;

import java.nio.ByteBuffer;

import org.fixme.core.protocol.ByteArrayBuffer;
import org.fixme.core.protocol.message.BuyInstrumentMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainTest {

	private static Logger logger = LoggerFactory.getLogger(MainTest.class);
	
	public static void main(String ...args) {
		
		BuyInstrumentMessage message = new BuyInstrumentMessage(50, "VIOLON", 1, "MSE", 500);
		
		message.serialize_message();
		
		ByteBuffer array = message.array();
		
		ByteArrayBuffer bb = new ByteArrayBuffer(array);
		
		bb.setPosition(8);
		
		BuyInstrumentMessage nmessage = new BuyInstrumentMessage(bb);
		
		nmessage.deserialize_message();
		
		logger.info("message: {}, ", nmessage.toString());
	}
}
