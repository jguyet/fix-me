package org.fixme.core.client;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.fixme.core.protocol.NetworkMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketChannel {
	
	/**
	 * LOGGER
	 */
	private static Logger logger = LoggerFactory.getLogger(SocketChannel.class);
	
	/**
	 * STATIC VARS
	 */
	private static int CHANNEL_DIGIT_UID = 1;
	
	private int							uid;
	private AsynchronousSocketChannel	channel;
	
	public SocketChannel(AsynchronousSocketChannel ch) {
		this.channel = ch;
		this.uid = SocketChannel.CHANNEL_DIGIT_UID++;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public AsynchronousSocketChannel getChannel() {
		return channel;
	}
	
	public boolean isOpen() {
		return this.channel.isOpen();
	}
	
	/**
	 * method for write on asynchronous messages
	 * @param socketChannel
	 * @param buffer
	 */
	public void write(NetworkMessage message) {
		
		ByteBuffer buffer = message.array();
		
		this.channel.write(buffer, this, new CompletionHandler<Integer, SocketChannel> () {

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
	
	//RESERVED
	
	public ByteBuffer splittedMessage = ByteBuffer.allocate(0);
}
