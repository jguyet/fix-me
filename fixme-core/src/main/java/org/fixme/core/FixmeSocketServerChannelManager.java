package org.fixme.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.protocol.NetworkMessageFactory;
import org.fixme.core.protocol.NetworkMessageHeader;
import org.fixme.core.protocol.NetworkProtocolMessage;
import org.fixme.core.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FixmeSocketServerChannelManager {
	
	/**
	 * FINAL STATIC VARS
	 */
	private static final int				SIZE_READ_BYTEBUFFER_ALLOCATED = 2048;
	
	/**
	 * LOGGER
	 */
	private static Logger					logger = LoggerFactory.getLogger(FixmeSocketServerChannelManager.class);
	
	/**
	 * VARS
	 */
	private AsynchronousServerSocketChannel channelListener;
	private int								port;
	private IASynchronousSocketChannelHandler			handler;
	
	/**
	 * FixmeServerManager constructor<br>
	 * @param port The local address to bind the socket
	 */
	public FixmeSocketServerChannelManager(int port, IASynchronousSocketChannelHandler handler) {
		this.port = port;
		this.handler = handler;
	}
	
	/**
	 * Method for bind socket server
	 */
	public void initialize() {
		try {
		channelListener = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
		} catch (IOException e) {
			logger.error("fail bind channelListener on port {}", this.port);
			return ;
		}
		logger.info("Connection estabilised on port {}", this.port);
	}
	
	/**
	 * Method for accept asynchronous news clients
	 */
	public void startOnAcceptSocketChannel() {
		channelListener.accept(channelListener, new CompletionHandler<AsynchronousSocketChannel,AsynchronousServerSocketChannel>() {
		    
			@Override
			public void completed(AsynchronousSocketChannel asynchronousSocketChannel, AsynchronousServerSocketChannel serverSock) {
		    	  //a connection is accepted, start to accept next connection
		    	  channelListener.accept(channelListener, this);

		    	  SocketChannel channel = new SocketChannel(asynchronousSocketChannel);
		          // handle this connection
		    	  handler.onStartConnection(channel);
		    	  
		    	  //start to read message from the client
		    	  startOnReadSocketChannel(channel);
		    }
			
			@Override
			public void failed(Throwable exc, AsynchronousServerSocketChannel serverSock) {
				logger.error("fail to accept a connection");
			}
		
		});
	}
	
	/**
	 * method for wait on asynchronous new messages
	 * @param socketChannel
	 */
	public void startOnReadSocketChannel(SocketChannel socketChannel) {
		final ByteBuffer buffer = ByteBuffer.allocate(SIZE_READ_BYTEBUFFER_ALLOCATED);
		
		socketChannel.getChannel().read(buffer, socketChannel, new CompletionHandler<Integer, SocketChannel >() {
			
			@Override
			public void completed(Integer result, SocketChannel channel) {
				buffer.flip();
				
				if (result == -1) {
					handler.onConnectionClosed(channel);
					return ;
				}
				
				int totalLength = result + channel.splittedMessage.capacity();
				ByteBuffer finalbuffer = ByteBuffer.allocate(totalLength);
					
				finalbuffer.put(channel.splittedMessage);
				finalbuffer.put(buffer.array(), 0, result);
				finalbuffer.flip();
				
				channel.splittedMessage = ByteBuffer.allocate(0);
				buffer.clear();
				
				NetworkMessageHeader header = NetworkProtocolMessage.readHeader(finalbuffer);
				
				if (header == null) {
					finalbuffer.flip();
					channel.splittedMessage = finalbuffer;
					//start to read next message again
					startOnReadSocketChannel(channel);
					return ;
				}
				
				if (Validator.validateObject(header) == false) {
					logger.info("messageId: {} beans not valide.", header.getId());
					startOnReadSocketChannel(channel);
					return ;
				}
				
				logger.info("messageId: {}, message length: {}", header.getId(), header.getLength());
				
				NetworkMessage message = NetworkMessageFactory.createNetworkMessage(header, finalbuffer);
				
				if (message != null) {
					
					message.deserialize();
					
					if (Validator.validateObject(message) == false) {
						logger.info("messageId: {} beans not valide.", header.getId());
						startOnReadSocketChannel(channel);
						return ;
					}
					
					handler.onMessageReceived(channel, message);
				} else {
					logger.info("messageId: {} doesn't exist.", header.getId());
				}
				//start to read next message again
				startOnReadSocketChannel(channel);
			}
			
			@Override
			public void failed(Throwable exc, SocketChannel channel) {
				logger.error("fail to read message from client");
			}

		});
	}
	
	/**
	 * method for write on asynchronous messages
	 * @param socketChannel
	 * @param buffer
	 */
	public void startOnWriteSocketChannel(SocketChannel socketChannel, final ByteBuffer buffer) {
		
		socketChannel.getChannel().write(buffer, socketChannel, new CompletionHandler<Integer, SocketChannel> () {

			@Override
			public void completed(Integer result, SocketChannel attachment) {
				//finish to right message to client, nothing to do
			}

			@Override
			public void failed(Throwable exc, SocketChannel attachment) {
				logger.error("fail to write message from client");
			}
			
		});
	}
	
	/**
	 * method for stop socketServer services
	 */
	public void stopSocketServerChannel() {
		try {
			if (channelListener.isOpen())
				channelListener.close();
		} catch (IOException e) {
			logger.error("fail to close channelListener port :{}", this.port);
			return ;
		}
		logger.info("ChannelListener port {} stopped", this.port);
	}
}
