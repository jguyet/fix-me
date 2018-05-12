package org.fixme.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
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
import org.fixme.core.utils.Json;
import org.fixme.core.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FixmeSocketServerChannelManager {
	
	/**
	 * FINAL STATIC VARS
	 */
	private static final int							SIZE_READ_BYTEBUFFER_ALLOCATED = 2048;
	
	/**
	 * LOGGER
	 */
	private static Logger								logger = LoggerFactory.getLogger(FixmeSocketServerChannelManager.class);
	
	/**
	 * VARS
	 */
	private AsynchronousServerSocketChannel 			channelListener;
	private int											port;
	private IASynchronousSocketChannelHandler			handler;
	
	private String										moduleName;
	
	/**
	 * FixmeServerManager constructor<br>
	 * @param port The local address to bind the socket
	 */
	public FixmeSocketServerChannelManager(int port, IASynchronousSocketChannelHandler handler, String moduleName) {
		this.port = port;
		this.handler = handler;
		this.moduleName = moduleName;
	}
	
	/**
	 * Method for bind socket server
	 */
	public void initialize() throws BindException {
		try {
		channelListener = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
		} catch (IOException e) {
			logger.error("{} - failnn bind channelListener on port {}", moduleName, this.port);
			throw new BindException(e.getMessage());
		}
		logger.info("{} - Connection estabilised on port {}", moduleName, this.port);
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
				if (channelListener.isOpen())
					logger.error("{} - fail to accept a connection", moduleName);
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
				/**
				 * IF result == -1 connection are closed
				 */
				if (result == -1) {
					handler.onConnectionClosed(channel);
					return ;
				}
				byte[] array = new byte[result];
				buffer.get(array);
				//build buffer
				String finalbuffer = null;
				try {
					channel.splittedMessage += new String(array, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					channel.splittedMessage = "";
					startOnReadSocketChannel(socketChannel);
					return ;
				}
				
				
				for (int i = 0; i < channel.splittedMessage.length(); i++) {
					if (channel.splittedMessage.charAt(i) == '\n' && i > 0) {
						finalbuffer = channel.splittedMessage.substring(0, i);
						if (channel.splittedMessage.length() > i + 1) {
							channel.splittedMessage = channel.splittedMessage.substring(i + 1);
						}
						else
							channel.splittedMessage = "";
					}
				}
				if (finalbuffer == null) {
					//start to read next message again
					startOnReadSocketChannel(socketChannel);
					return ;
				}
				/**
				 * BUILD MESSAGE
				 */
				Json jsonMessage = null;
				
				try {
					jsonMessage = new Json(finalbuffer);
				} catch (Exception e) {
					handler.onErrorJsonParser(channel);
					handler.onConnectionClosed(channel);
					return ;
				}
				/**
				 * BUILD HEADER
				 */
				NetworkMessageHeader header = NetworkProtocolMessage.readHeader(jsonMessage);
				if (header == null) {
					//start to read next message again
					startOnReadSocketChannel(socketChannel);
					return ;
				}
				/**
				 * CHECK IF HEADER IS VALID
				 */
				if (Validator.validateObject(header) == false) {
					//start to read next message again
					startOnReadSocketChannel(socketChannel);
					return ;
				}
				
				NetworkMessage message = NetworkMessageFactory.createNetworkMessage(header, jsonMessage);
				
				if (message != null) {
					/**
					 * DESERIALIZE BUFFERED MESSAGE
					 */
					message.deserialize_message();
					
					/**
					 * CHECK CHECKSUM
					 */
					if (message.getCheckSum().equalsIgnoreCase(message.buildCheckSum()) == false) {
						logger.error("fail checkSum is not equals {} != {}", message.getCheckSum(), message.buildCheckSum());
						//start to read next message again
						startOnReadSocketChannel(socketChannel);
						return ;
					}
					/**
					 * CHECK IF MESSAGE IS VALID
					 */
					if (Validator.validateObject(message) == false) {
						//start to read next message again
						startOnReadSocketChannel(socketChannel);
						return ;
					}
					/**
					 * CALL HANDLER METHOD OF MESSAGE RECEIVER
					 */
					handler.onMessageReceived(channel, message);
				} else {
					logger.info("{} - messageId: {} doesn't exist.", moduleName, header.getId());
				}
				//start to read next message again
				startOnReadSocketChannel(socketChannel);
			}
			
			@Override
			public void failed(Throwable exc, SocketChannel channel) {
				if (channel.isOpen())
					logger.error("{} - fail to read message from client", moduleName);
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
				if (attachment.isOpen())
					logger.error("{} - fail to write message from client", moduleName);
			}
			
		});
	}
	
	/**
	 * method for stop socketServer services
	 */
	public void stopSocketServerChannel() {
		try {
			if (channelListener != null && channelListener.isOpen())
				channelListener.close();
		} catch (IOException e) {
			logger.error("{} - fail to close channelListener port :{}", moduleName, this.port);
			return ;
		}
		logger.info("{} - ChannelListener port {} stopped", moduleName, this.port);
	}
}
