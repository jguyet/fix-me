package org.fixme.core;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ByteBufferUtils {

	public static String getString(ByteBuffer buffer) {
		int len = buffer.getInt();
		byte[] bytes = new byte[len];
		
		buffer.get(bytes, 0, len);
		
		String str = new String(bytes, Charset.forName("UTF-8"));
		return str;
	}
	
	public static ByteBuffer putString(ByteBuffer buffer, String str) {
		buffer.putInt(str.length());
		buffer.put(str.getBytes());
		return buffer;
	}
}
