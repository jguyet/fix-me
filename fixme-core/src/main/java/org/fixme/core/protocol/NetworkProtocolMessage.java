package org.fixme.core.protocol;

import org.fixme.core.utils.Json;

/**
 * Header protocol message
 * @author jguyet
 * @Commented
 */
public class NetworkProtocolMessage {
	
	private static final String ID_KEY = "MESSAGE_ID";
	
	/**
	 * read message Header<br>
	 * @param buffer
	 * @return
	 */
	public static NetworkMessageHeader readHeader(Json message) {
		
		NetworkMessageHeader header = null;
		
		if (message.has(ID_KEY) == false)
			return null;
		int id = message.getInt(ID_KEY);
		
		header = new NetworkMessageHeader(id);
		return (header);
	}
	
	/**
	 * write NetworkMessageHeader on Map<String, String><br>
	 * @param header
	 * @return
	 */
	public static void writeHeader(NetworkMessageHeader header, Json message) {
		message.put(ID_KEY, header.getId());
	}
}
