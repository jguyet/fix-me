package org.fixme.core.database;

import org.fixme.core.LoggingProperties;
import org.fixme.core.database.collections.InstrumentCollectionDAO;
import org.fixme.core.database.collections.MarketCollectionDAO;
import org.fixme.core.protocol.types.Instrument;
import org.fixme.core.protocol.types.Market;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.logging.MorphiaLoggerFactory;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class Database {
	
	//##############################
	//@STATIC VARIABLES SECTION --->
	//##############################
	
    private static Logger 				logger = (Logger) LoggerFactory.getLogger(Database.class);
	
	//##############################
	//@PRIVATE VARIABLES SECTION -->
	//##############################
    
    //HOST
    private String 						hostname;
    private int							port;
    
    //USER CREDENTIAL
    private String						userName;
    private String						password;
    
    //DATABASE NAME
    private String						databaseName;
    
    //MONGO
    private MongoCredential				mongoCredential;
    private MongoClientOptions 			mongoOptions;
    private MongoClient 				mongoClient;
    private MongoDatabase				mongoDB;
    
    //Collections
    private InstrumentCollectionDAO		instrumentCollection;
    private MarketCollectionDAO			marketCollection;
    
	//##############################################################################
	//@CONTRUCTOR SECTION --------------------------------------------------------->
	//##############################################################################
    
    public Database(String hostname, int port, String databaseName) {
    	this.hostname = hostname;
    	this.port = port;
    	this.databaseName = databaseName;
    }
    
	//##############################################################################
	//@BUILD METHODS SECTION ------------------------------------------------------>
	//##############################################################################
    
    public void buildDatabaseOptions() {
    	MongoClientOptions.Builder optionsBuilder = MongoClientOptions.builder();
    	
    	this.mongoOptions = optionsBuilder.build();
    	logger.info("MongoClientOptions builded");
    }
    
    public void buildCredential(String username, String password) {
    	this.userName = username;
    	this.password = password;
    	
    	// Automatically detect SCRAM-SHA-1 or Challenge Response protocol
    	this.mongoCredential = MongoCredential.createCredential(this.userName, this.databaseName, this.password.toCharArray());
    	logger.info("MongoCredential builded");
    }
    
    public void buildDatabaseConnection() {
    	this.mongoClient = new MongoClient(new ServerAddress(hostname, port), mongoCredential, mongoOptions);
    	this.mongoDB = mongoClient.getDatabase(databaseName);
    	logger.info("Database connection estabilised on database \"{}\" on address {}:{}", databaseName, hostname, port);
    }
    
    public void buildCollections() {
    	
    	if (LoggingProperties.MORPHIA_LOGS_LEVEL == Level.OFF) {
    		//Set morphia logging OFF
    		MorphiaLoggerFactory.registerLogger(MorphiaSilentLoggerFactory.class);
    	}
    	Morphia morphia = new Morphia();
    	
    	morphia.map(Instrument.class);
    	instrumentCollection = new InstrumentCollectionDAO(this.mongoClient, morphia, this.databaseName);
    	
    	morphia.map(Market.class);
    	marketCollection = new MarketCollectionDAO(this.mongoClient, morphia, this.databaseName);
    	logger.info("MongoCollections builded");
    }
    
    public void closeDatabaseConnection() {
    	this.mongoClient.close();
    	logger.info("Database connection closed on address {}:{}", hostname, port);
    }
    
	//##############################################################################
	//@GETTER SETTER SECTION ------------------------------------------------------>
	//##############################################################################
    
    public MongoDatabase Mongo() {
    	return mongoDB;
    }

	public InstrumentCollectionDAO getInstrumentCollection() {
		return instrumentCollection;
	}

	public MarketCollectionDAO getMarketCollection() {
		return marketCollection;
	}
}
