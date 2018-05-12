package org.fixme.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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

public class FixmeSocketChannelManager {
	
	/**
	 * FINAL STATIC VARS
	 */
	private static final int				SIZE_READ_BYTEBUFFER_ALLOCATED = 2048;
	/**
	 * LOGGER
	 */
	private static Logger logger = LoggerFactory.getLogger(FixmeSocketChannelManager.class);

	private SocketChannel						channel;
	private String								ip;
	private int									port;
	private IASynchronousSocketChannelHandler	handler;
	
	private String								moduleName;
	
	public FixmeSocketChannelManager(String ip, int port, IASynchronousSocketChannelHandler handler, String moduleName) {
		this.ip = ip;
		this.port = port;
		this.handler = handler;
		this.moduleName = moduleName;
	}
	
	public void initialize() {
		try {
			channel = new SocketChannel(AsynchronousSocketChannel.open());
			InetSocketAddress hostAddress = new InetSocketAddress(ip, port);
			
			channel.getChannel().connect(hostAddress, channel, new CompletionHandler<Void, SocketChannel >() {

				@Override
				public void completed(Void result, SocketChannel attachment) {
					handler.onStartConnection(channel);
				}

				@Override
				public void failed(Throwable exc, SocketChannel attachment) {
					logger.error("{} - fail bind SocketChannel on {}:{}", moduleName, ip, port);
					handler.onConnectionClosed(channel);
				}
			});
			
		} catch (IOException e) {
			logger.error("{} - fail bind SocketChannel on {}:{}", moduleName, this.ip, this.port);
			return ;
		}
	}
	
	/**
	 * method for wait on asynchronous new messages
	 * @param socketChannel
	 */
	public void startOnReadSocketChannel() {
		final ByteBuffer buffer = ByteBuffer.allocate(SIZE_READ_BYTEBUFFER_ALLOCATED);
		
		this.channel.getChannel().read(buffer, this.channel, new CompletionHandler<Integer, SocketChannel >() {
			
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
					startOnReadSocketChannel();
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
					startOnReadSocketChannel();
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
					startOnReadSocketChannel();
					return ;
				}
				/**
				 * CHECK IF HEADER IS VALID
				 */
				if (Validator.validateObject(header) == false) {
					//start to read next message again
					startOnReadSocketChannel();
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
						startOnReadSocketChannel();
						return ;
					}
					/**
					 * CHECK IF MESSAGE IS VALID
					 */
					if (Validator.validateObject(message) == false) {
						//start to read next message again
						startOnReadSocketChannel();
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
				startOnReadSocketChannel();
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
	public void startOnWriteSocketChannel(final ByteBuffer buffer) {
		
		this.channel.getChannel().write(buffer, this.channel, new CompletionHandler<Integer, SocketChannel> () {

			@Override
			public void completed(Integer result, SocketChannel attachment) {
				//finish to right message to client, nothing to do
			}

			@Override
			public void failed(Throwable exc, SocketChannel attachment) {
				if (attachment.isOpen())
					logger.error("fail to write message from client");
			}
			
		});
	}
	
	public void stop() {
		try {
			if (this.channel != null && this.channel.getChannel().isOpen())
				this.channel.getChannel().close();
		} catch (IOException e) {
			logger.error("{} - fail to close SocketChannel on {}:{}", moduleName, ip, port);
			return ;
		}
		logger.info("{} - SocketChannel port {}:{} stopped", moduleName, ip, port);
	}
	
}
