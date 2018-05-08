package org.fixme.core.database.collections;

import org.bson.types.ObjectId;
import org.fixme.core.protocol.types.OrderObject;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.Mongo;

public class InstrumentCollectionDAO extends BasicDAO<OrderObject, ObjectId> {
	
	public InstrumentCollectionDAO(Mongo mongo, Morphia morphia, String dbName) {
		super(mongo, morphia, dbName);
	}
}
