package org.fixme.core.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Class array of bytes adder and reader by @ByteBuffer or @byte[]
 * @author jguyet
 * @Commented
 */
public class ByteArrayBuffer {
	
	//##############################
	//@STATICS VARIABLES SECTION -->
	//##############################
	
	/**
	 * Long 8 bytes
	 * |0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|
	 */
	public static final int		BYTE_LONG_SIZE = 8;
	/**
	 * Int 4 bytes
	 * |0000 0000 0000 0000 0000 0000 0000 0000|
	 */
	public static final int		BYTE_INT_SIZE = 4;
	/**
	 * Short 2 bytes
	 * |0000 0000 0000 0000 0000|
	 */
	public static final int		BYTE_SHORT_SIZE = 2;
	/**
	 * 1 byte
	 * |0000 0000|
	 */
	public static final int		BYTE_BYTE_SIZE = 1;

	//##############################
	//@PRIVATE VARIABLES SECTION -->
	//##############################
	
	private ByteBuffer			buffer;
	private int					size;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	/**
	 * ByteArrayBuffer __contructor(ByteBuffer buffer)<br>
	 * copy content of param1<br>
	 * @param buffer ByteBuffer
	 */
	public ByteArrayBuffer(ByteBuffer buffer) {
		byte[] array = buffer.array();
		this.buffer = ByteBuffer.allocate(array.length);
		
		this.buffer.put(array);
		this.buffer.flip();
		this.size = array.length;
	}
	
	/**
	 * ByteArrayBuffer __contructor(ByteArrayBuffer buffer)<br>
	 * copy content of param1<br>
	 * @param buffer ByteArrayBuffer
	 */
	public ByteArrayBuffer(ByteArrayBuffer buffer) {
		this.buffer = ByteBuffer.allocate(buffer.size);
		
		this.buffer.put(buffer.array());
		this.buffer.flip();
		this.size = buffer.size;
	}
	
	/**
	 * ByteArrayBuffer @default __contructor()<br>
	 */
	public ByteArrayBuffer() {
		this.buffer = ByteBuffer.allocate(0);
		this.size = 0;
	}
	
	//##############################################################################
	//@GETTER SETTER SECTION ------------------------------------------------------>
	//##############################################################################
	
	/**
	 * setter of buffer position
	 * @param position
	 */
	public void setPosition(int position) {
		this.buffer.position(position);
	}
	
	/**
	 * getter of buffer position
	 * @return int
	 */
	public int getPosition() {
		return this.buffer.position();
	}
	
	/**
	 * getter of size
	 * @return int
	 */
	public int size() {
		return this.size;
	}
	
	/**
	 * setter of size<br>
	 * no modification if param1 is inferior of size
	 * @param s
	 */
	public void size(int s) {
		if (s <= this.size)
			return ;
		augmentSize(s - this.size);
	}
	
	/**
	 * Augment size buffer size + param1
	 * @param min
	 */
	private void augmentSize(int min) {
		int newSize = (this.size + min);
		ByteBuffer nbuffer = ByteBuffer.allocate(newSize);
		
		//start buffer
		this.buffer.flip();
		nbuffer.put(this.buffer.array(), 0, this.size);
		//set buffer
		this.buffer = nbuffer;
		//change size
		this.size = newSize;
	}
	
	//#####################################################################################
	//@READ SECTION ---------------------------------------------------------------------->
	//#####################################################################################
	
	/**
	 * Read @long value on buffer
	 * @return long
	 */
	public long readLong() {
		return this.buffer.getLong();
	}
	
	/**
	 * Read @int value on buffer
	 * @return int
	 */
	public int readInt() {
		return this.buffer.getInt();
	}
	
	/**
	 * Read @short value on buffer
	 * @return short
	 */
	public short readShort() {
		return this.buffer.getShort();
	}
	
	/**
	 * Read @byte value on buffer
	 * @return byte
	 */
	public byte readByte() {
		return this.buffer.get();
	}
	
	/**
	 * Read @byte[] array on buffer by length and offset
	 * @param buf
	 * @param offset
	 * @param size
	 * @return ByteArrayBuffer
	 */
	public ByteArrayBuffer readBytes(byte[] buf, int offset, int lentgh) {
		this.buffer.get(buf, offset, lentgh);
		return this;
	}
	
	/**
	 * Read @byte[] array on buffer by length
	 * @param size
	 * @return byte[]
	 */
	public byte[] readBytes(int size) {
		byte[] bytes = new byte[size];
		
		this.readBytes(bytes, 0, size);
		return bytes;
	}
	
	/**
	 * Read @String on buffer<br>
	 * step1 : size = readInt()<br>
	 * step2 : readBytes(size)<br>
	 * step3 : to UTF-8 bytes<br>
	 * @return String
	 */
	public String readString() {
		int len = this.buffer.getInt();
		byte[] bytes = this.readBytes(len);
		
		String str = new String(bytes, Charset.forName("UTF-8"));
		
		return str;
	}
	
	//#####################################################################################
	//@WRITE SECTION ---------------------------------------------------------------------->
	//#####################################################################################
	
	/**
	 * write @long var on buffer and increment position
	 * @param n long
	 * @return ByteArrayBuffer
	 */
	public ByteArrayBuffer writeLong(long n) {
		this.augmentSize(BYTE_LONG_SIZE);
		this.buffer.putLong(n);
		return this;
	}
	
	/**
	 * write @int var on buffer and increment position
	 * @param n int
	 * @return ByteArrayBuffer
	 */
	public ByteArrayBuffer writeInt(int n) {
		this.augmentSize(BYTE_INT_SIZE);
		this.buffer.putInt(n);
		return this;
	}
	
	/**
	 * write @short var on buffer and increment position
	 * @param n short
	 * @return ByteArrayBuffer
	 */
	public ByteArrayBuffer writeShort(short n) {
		this.augmentSize(BYTE_SHORT_SIZE);
		this.buffer.putShort(n);
		return this;
	}
	
	/**
	 * write @byte var on buffer and increment position
	 * @param b byte
	 * @return ByteArrayBuffer
	 */
	public ByteArrayBuffer write(byte b) {
		
		this.augmentSize(BYTE_BYTE_SIZE);
		this.buffer.put(b);
		return this;
	}
	
	/**
	 * write @byte[] var on buffer and increment position
	 * @param array byte[]
	 * @return ByteArrayBuffer
	 */
	public ByteArrayBuffer write(byte[] array) {
		this.augmentSize(array.length);
		this.buffer.put(array);
		return this;
	}
	
	/**
	 * write @byte[] array on buffer and increment position
	 * @param array byte[]
	 * @param offset int
	 * @param length int
	 * @return ByteArrayBuffer
	 */
	public ByteArrayBuffer write(byte[] array, int offset, int length) {
		this.augmentSize(length);
		this.buffer.put(array, offset, length);
		return this;
	}
	
	/**
	 * write @ByteArrayBuffer var on buffer and increment position
	 * @param b ByteArrayBuffer
	 * @return ByteArrayBuffer
	 */
	public ByteArrayBuffer write(ByteArrayBuffer b) {
		this.write(b.array());
		return this;
	}
	
	/**
	 * write @String var on buffer and increment position<br>
	 * step1 : writeInt(str.lentgh)<br>
	 * step2 : writeBytes(str.bytes)<br>
	 * @param str String
	 * @return ByteArrayBuffer
	 */
	public ByteArrayBuffer writeString(String str) {
		this.writeInt(str.length());
		this.write(str.getBytes());
		return this;
	}
	
	//#####################################################################################
	//@ARRAY SECTION --------------------------------------------------------------------->
	//#####################################################################################
	
	/**
	 * Getter byte[] array by offset and length want
	 * @param offset int
	 * @param length int
	 * @return byte[]
	 */
	public byte[] array(int offset, int length) {
		byte[] array = array();
		byte[] result = new byte[length];
		
		System.arraycopy(array, offset, result, 0, length);
		return result;
	}
	
	/**
	 * Getter byte[] array of buffer
	 * @return byte[]
	 */
	public byte[] array() {
		return buffer.array();
	}
	
	/**
	 * Flips this buffer. The limit is set to the current position and then the position 
	 * is set to zero. If the mark is defined then it is discarded.<br><br>
	 * After a sequence of channel-read or put operations, invoke this method to prepare 
	 * for a sequence of channel-write or relative get operations. For example:<br><br>
	 * 
 	 * &nbsp buf.put(magic);    // Prepend header<br>
 	 * &nbsp in.read(buf);      // Read data into rest of buffer<br>
 	 * &nbsp buf.flip();        // Flip buffer<br>
 	 * &nbsp out.write(buf);    // Write header + data to channel<br><br>
 	 * 
 	 * This method is often used in conjunction with the compact method when transferring
 	 * data from one place to another.
	 */
	public void flip() {
		this.buffer.flip();
	}
	
	/**
	 * Getter of buffer ByteBuffer
	 * @return ByteBuffer
	 */
	public ByteBuffer getByteBuffer() {
		return this.buffer;
	}
}
