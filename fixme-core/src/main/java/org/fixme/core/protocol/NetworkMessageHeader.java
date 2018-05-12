package org.fixme.core.protocol;

/**
 * NetworkMessageHeader class
 * @author jguyet
 * @Commented
 */
public class NetworkMessageHeader {

	//##############################
	//@PRIVATE VARIABLES SECTION -->
	//##############################
	
	private int id;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public NetworkMessageHeader(int id) {
		this.id = id;
	}
	
	//##############################################################################
	//@GETTER SETTER SECTION ------------------------------------------------------>
	//##############################################################################
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
