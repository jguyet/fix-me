package org.fixme.core.database.collections;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;

@Entity
public class BaseCollection {
	
	@Id
	@Property("id")
	private ObjectId id;
 
	public ObjectId getId() {
    	return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
}
