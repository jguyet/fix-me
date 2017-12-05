package org.fixme.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
					logger.error("{} - fail bind SocketChannel on port {}:{}", moduleName, ip, port);
					handler.onConnectionClosed(channel);
				}
			});
			
		} catch (IOException e) {
			logger.error("{} - fail bind SocketChannel on port {}:{}", moduleName, this.ip, this.port);
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
					startOnReadSocketChannel();
					return ;
				}
				
				if (Validator.validateObject(header) == false) {
					logger.info("{} - messageId: {} beans not valide.", moduleName, header.getId());
					startOnReadSocketChannel();
					return ;
				}
				
				logger.info("{} messageId: {}, message length: {}", moduleName, header.getId(), header.getLength());
				
				NetworkMessage message = NetworkMessageFactory.createNetworkMessage(header, finalbuffer);
				
				if (message != null) {
					
					message.deserialize();
					
					if (Validator.validateObject(message) == false) {
						logger.info("{} - messageId: {} beans not valide.", moduleName, header.getId());
						startOnReadSocketChannel();
						return ;
					}
					
					handler.onMessageReceived(channel, message);
				} else {
					logger.info("{} - messageId: {} doesn't exist.", moduleName, header.getId());
				}
				//start to read next message again
				startOnReadSocketChannel();
			}
			
			@Override
			public void failed(Throwable exc, SocketChannel channel) {
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
				logger.error("fail to write message from client");
			}
			
		});
	}
	
}
