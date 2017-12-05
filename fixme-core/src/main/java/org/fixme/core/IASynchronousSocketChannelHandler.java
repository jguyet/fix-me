package org.fixme.core;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;

public interface IASynchronousSocketChannelHandler {
	/**
	 * call on accept new connection
	 * @param ch AsynchronousSocketChannel
	 */
	public void onStartConnection(SocketChannel ch);
	
	/**
	 * call on receive new buffered message
	 */
	public void onMessageReceived(SocketChannel ch, NetworkMessage message);
	
	/**
	 * called on connection as closed
	 * @param ch
	 */
	public void onConnectionClosed(SocketChannel ch);
}
