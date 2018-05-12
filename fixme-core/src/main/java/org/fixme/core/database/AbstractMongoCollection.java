package org.fixme.core.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public abstract class AbstractMongoCollection<Document> {
	
	protected String			collectionName;
	protected MongoCollection<Document>	mongoCollection;
	
	private MongoDatabase		mongoDB;
	
	@SuppressWarnings("unchecked")
	public AbstractMongoCollection(MongoDatabase mongoDB, String collectionName) {
		this.mongoDB = mongoDB;
		this.collectionName = collectionName;
		this.mongoCollection = (MongoCollection<Document>) this.mongoDB.getCollection(this.collectionName);
	}
}
