package org.fixme.core.protocol;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class NetworkMessageHeader {

	@Max(value=1001)
	@Min(value=1000)
	private int id;
	
	@Min(value=8)
	@Max(value=2048)
	private int length;
	
	public NetworkMessageHeader(int id, int length) {
		this.id = id;
		this.length = length;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
