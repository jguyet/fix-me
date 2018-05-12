package org.fixme.core.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.fixme.core.protocol.NetworkMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketChannel {
	
	//##############################
	//@STATIC VARIABLES SECTION --->
	//##############################
	
	private static Logger				logger = LoggerFactory.getLogger(SocketChannel.class);
	private static int					CHANNEL_DIGIT_UID = 1;
	
	//##############################
	//@PRIVATE VARIABLES SECTION -->
	//##############################
	
	private int							uid;
	private AsynchronousSocketChannel	channel;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public SocketChannel(AsynchronousSocketChannel ch) {
		this.channel = ch;
		this.uid = SocketChannel.CHANNEL_DIGIT_UID++;
	}
	
	//##############################################################################
	//@GETTER SETTER SECTION ------------------------------------------------------>
	//##############################################################################

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public AsynchronousSocketChannel getChannel() {
		return channel;
	}
	
	//##############################################################################
	//@METHODS SECTION ------------------------------------------------------------>
	//##############################################################################
	
	public boolean isOpen() {
		return this.channel.isOpen();
	}
	
	public String getRemoteAddress() {
		String remoteAddress = null;
		
		try {
			remoteAddress = channel.getRemoteAddress().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return remoteAddress;
	}
	
	/**
	 * method for write on asynchronous messages
	 * @param socketChannel
	 * @param buffer
	 */
	public void write(NetworkMessage message) {
		/**
		 * SERIALIZE MESSAGE
		 */
		message.serialize_message();
		/**
		 * GET BYTEBUFFER OF MESSAGE
		 */
		byte[] buffer = message.array();
		
		ByteBuffer finalbuffer = ByteBuffer.allocate(buffer.length + 1);

		finalbuffer.put(buffer);
		finalbuffer.put((byte)'\n');
		finalbuffer.flip();
		/**
		 * SEND TO DEST
		 */
		this.channel.write(finalbuffer, this, new CompletionHandler<Integer, SocketChannel> () {

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
	
	//##############################################################################
	//@RESERVED SECTION ----------------------------------------------------------->
	//##############################################################################
	
	public String splittedMessage = "";
}
