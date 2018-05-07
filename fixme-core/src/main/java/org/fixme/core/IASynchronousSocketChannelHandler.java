package org.fixme.core;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;

/**
 * @author jguyet
 * @Commented
 */
public interface IASynchronousSocketChannelHandler {
	/**
	 * call on accept new connection
	 * @param ch SocketChannel
	 */
	public void onStartConnection(SocketChannel ch);
	
	/**
	 * call on receive new buffered message
	 * @oaram1 SocketChannel
	 * @param2 NetworkMessage
	 */
	public void onMessageReceived(SocketChannel ch, NetworkMessage message);
	
	/**
	 * called on connection as closed
	 * @param ch SocketChannel
	 */
	public void onConnectionClosed(SocketChannel ch);
	
	
	public void onErrorJsonParser(SocketChannel ch);
}
