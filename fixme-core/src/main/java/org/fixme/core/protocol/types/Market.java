package org.fixme.core.protocol.types;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.fixme.core.database.collections.BaseCollection;
import org.fixme.core.protocol.ByteArrayBuffer;
import org.fixme.core.protocol.INetworkType;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;

@Entity
public class Market extends BaseCollection implements INetworkType {
	
	@Property("name")
	private String			name;
	
	@Property("sales_list")
	private List<ObjectId>	sales_list;
	
	@Property("purchase_list")
	private List<ObjectId>	purchase_list;
	
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
	
	public Market() {}
	
	public Market(String name) {
		this.name = name;
		this.sales_list = new ArrayList<ObjectId>();
		this.purchase_list = new ArrayList<ObjectId>();
	}
	
	//##############################################################################
	//@GETTER SETTER SECTION ------------------------------------------------------>
	//##############################################################################

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ObjectId> getSales_list() {
		return sales_list;
	}

	public void setSales_list(List<ObjectId> sales_list) {
		this.sales_list = sales_list;
	}
	
	public void sell_Instrument(Instrument i) {
		this.sales_list.add(i.getId());
	}

	public List<ObjectId> getPurchase_list() {
		return purchase_list;
	}

	public void setPurchase_list(List<ObjectId> purchase_list) {
		this.purchase_list = purchase_list;
	}
	
	public void add_purchase_Instrument(Instrument i) {
		this.purchase_list.add(i.getId());
	}
	
	//##############################################################################
	//@METHOD SECTION ------------------------------------------------------------->
	//##############################################################################

	@Override
	public void serialize(ByteArrayBuffer buffer) {
		buffer.writeString(this.name);
		
		buffer.writeInt(this.sales_list.size());
		for (ObjectId objId : this.sales_list) {
			buffer.writeString(objId.toHexString());
		}
		
		buffer.writeInt(this.purchase_list.size());
		for (ObjectId objId : this.purchase_list) {
			buffer.writeString(objId.toHexString());
		}
	}

	@Override
	public void deserialize(ByteArrayBuffer buffer) {
		this.name = buffer.readString();
		
		int size_sales = buffer.readInt();
		this.sales_list = new ArrayList<ObjectId>();
		for (int i = 0; i < size_sales; i++) {
			this.sales_list.add(new ObjectId(buffer.readString()));
		}
		
		int size_purchase = buffer.readInt();
		this.purchase_list = new ArrayList<ObjectId>();
		for (int i = 0; i < size_purchase; i++) {
			this.purchase_list.add(new ObjectId(buffer.readString()));
		}
		
	}
}
