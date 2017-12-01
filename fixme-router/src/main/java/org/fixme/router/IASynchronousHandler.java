package org.fixme.router;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Interface ASynchronousHandler
 * @author jguyet
 */
public interface IASynchronousHandler {

	/**
	 * call on accept new connection
	 * @param ch AsynchronousSocketChannel
	 */
	public void onStartConnection(AsynchronousSocketChannel ch);
	
	/**
	 * handle method<br>
	 * on read new message she call onMessageReceived method
	 */
	public void handle(AsynchronousSocketChannel ch);
	
	/**
	 * call on receive new buffered message
	 */
	public void onMessageReceived(AsynchronousSocketChannel ch, ByteBuffer message);
	
	/**
	 * called on connection as closed
	 * @param ch
	 */
	public void onConnectionClosed(AsynchronousSocketChannel ch);
}
