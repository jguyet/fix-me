package org.fixme.core.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ByteArrayBuffer {
	
	public static final int		BYTE_LONG_SIZE = 8;
	public static final int		BYTE_INT_SIZE = 4;
	public static final int		BYTE_SHORT_SIZE = 2;
	public static final int		BYTE_BIT_SIZE = 1;

	private ByteBuffer	buffer;
	private int			size;
	
	public ByteArrayBuffer(ByteBuffer buffer) {
		byte[] array = buffer.array();
		this.buffer = ByteBuffer.allocate(array.length);
		
		this.buffer.put(array);
		this.buffer.flip();
		this.size = array.length;
	}
	
	public ByteArrayBuffer(ByteArrayBuffer buffer) {
		this.buffer = ByteBuffer.allocate(buffer.size);
		
		this.buffer.put(buffer.array());
		this.buffer.flip();
		this.size = buffer.size;
	}
	
	public ByteArrayBuffer(int size) {
		this.buffer = ByteBuffer.allocate(size);
		this.size = size;
	}
	
	public ByteArrayBuffer() {
		this.buffer = ByteBuffer.allocate(0);
		this.size = 0;
	}
	
	public void setPosition(int position) {
		this.buffer.position(position);
	}
	
	public int getPosition() {
		return this.buffer.position();
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void setSize(int s) {
		if (s <= this.size)
			return ;
		augmentSize(s - this.size);
	}
	
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
	
	public long readLong() {
		return this.buffer.getLong();
	}
	
	public int readInt() {
		return this.buffer.getInt();
	}
	
	public short readShort() {
		return this.buffer.getShort();
	}
	
	public byte readByte() {
		return this.buffer.get();
	}
	
	public ByteArrayBuffer readBytes(byte[] buf, int offset, int size) {
		this.buffer.get(buf, offset, size);
		return this;
	}
	
	public byte[] readBytes(int size) {
		byte[] bytes = new byte[size];
		
		this.readBytes(bytes, 0, size);
		return bytes;
	}
	
	public String readString() {
		int len = this.buffer.getInt();
		byte[] bytes = this.readBytes(len);
		
		String str = new String(bytes, Charset.forName("UTF-8"));
		
		return str;
	}
	
	public ByteArrayBuffer putLong(long n) {
		this.augmentSize(BYTE_LONG_SIZE);
		this.buffer.putLong(n);
		return this;
	}
	
	public ByteArrayBuffer putInt(int n) {
		this.augmentSize(BYTE_INT_SIZE);
		this.buffer.putInt(n);
		return this;
	}
	
	public ByteArrayBuffer putShort(short n) {
		this.augmentSize(BYTE_SHORT_SIZE);
		this.buffer.putShort(n);
		return this;
	}
	
	public ByteArrayBuffer put(byte b) {
		
		this.augmentSize(BYTE_BIT_SIZE);
		this.buffer.put(b);
		return this;
	}
	
	public ByteArrayBuffer put(byte[] array) {
		this.augmentSize(array.length);
		this.buffer.put(array);
		return this;
	}
	
	public ByteArrayBuffer put(byte[] array, int offset, int length) {
		this.augmentSize(length);
		this.buffer.put(array, offset, length);
		return this;
	}
	
	public ByteArrayBuffer put(ByteArrayBuffer b) {
		this.put(b.array());
		return this;
	}
	
	public ByteArrayBuffer putString(String str) {
		this.putInt(str.length());
		this.put(str.getBytes());
		return this;
	}
	
	public byte[] array(int offset, int length) {
		byte[] array = array();
		byte[] result = new byte[length];
		
		System.arraycopy(array, offset, result, 0, length);
		return result;
	}
	
	public byte[] array() {
		return buffer.array();
	}
	
	public void flip() {
		this.buffer.flip();
	}
	
	public ByteBuffer getByteBuffer() {
		return this.buffer;
	}
}
