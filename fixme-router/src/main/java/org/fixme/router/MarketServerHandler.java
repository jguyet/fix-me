package org.fixme.router;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.ReadPendingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Market Handler
 * @author jguyet
 */
public class MarketServerHandler {

	private static Logger					logger = LoggerFactory.getLogger(MarketServerHandler.class);
	
	private AsynchronousServerSocketChannel channelListener;
	private int								port;
	
	/**
	 * MarketServerHandler constructor<br>
	 * @param port The local address to bind the socket
	 */
	public MarketServerHandler(int port) {
		this.port = port;
	}
	
	/**
	 * Start channel listener on MarketServerHandler.port<br>
	 * return false if an other I/O error occurs
	 * @return boolean
	 */
	public boolean run() {
		try {
			channelListener = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
			
			channelListener.accept(null, new CompletionHandler<AsynchronousSocketChannel,Void>() {
			      public void completed(AsynchronousSocketChannel ch, Void att) {
			          // accept the next connection
			    	  channelListener.accept(null, this);

			          // handle this connection
			    	  handler.onStartConnection(ch);
			      }
			      public void failed(Throwable exc, Void att) {
			    	try {
						throw new IOException("connection failed");
					} catch (IOException e) {
						e.printStackTrace();
					}
			      }
			  });
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		logger.info("Connection estabilised on port " + port);
		return true;
	}
	
	/**
	 * Stop if channel listener is opened<br>
	 * return false if an other I/O error occurs
	 * @return boolean
	 */
	public boolean stop() {
		try {
		if (channelListener.isOpen())
			channelListener.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Interface handling channels
	 */
	private IASynchronousHandler			handler = new IASynchronousHandler() {

		@Override
		public void onStartConnection(AsynchronousSocketChannel ch) {
			try {
				logger.info("New AsynchronousSocketChannel connection with " + ch.getRemoteAddress());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			handle(ch);
		}
		
		@Override
		public void handle(AsynchronousSocketChannel ch) {
			try {
			while (ch != null && ch.isOpen()) {
				ByteBuffer buffer = ByteBuffer.allocate(512);
				ch.read(buffer);
				onMessageReceived(ch, buffer);
			}
			} catch (ReadPendingException | NotYetConnectedException e) {
				onConnectionClosed(ch);
				return ;
			}
			onConnectionClosed(ch);
		}

		@Override
		public void onMessageReceived(AsynchronousSocketChannel ch, ByteBuffer message) {
			logger.info("new Message {}", message.array());
		}

		@Override
		public void onConnectionClosed(AsynchronousSocketChannel ch) {
			try {
				logger.info("Connection closed by " + ch.getRemoteAddress());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
}
