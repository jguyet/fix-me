package org.fixme.core;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class LoggingProperties {

	public static final Level	HIBERNATE_LOGS_LEVEL = Level.OFF;
	
	public static final Level	REFLECTION_LOGS_LEVEL = Level.OFF;
	
	public static final Level	VALIDATOR_LOGS_LEVEL = Level.OFF;
	
	public static final Level	PROJECT_LOGS_LEVEL = Level.ALL;
	
	public static final Level	CORE_LOGS_LEVEL = Level.INFO;
	
	public static void load() {
		/**
		 * SET LEVEL LOGS org.hibernate
		 */
		Logger log = (Logger) LoggerFactory.getLogger("org.hibernate.validator.util.Version");
		log.setLevel(LoggingProperties.HIBERNATE_LOGS_LEVEL);
		
		log = (Logger) LoggerFactory.getLogger("org.hibernate.validator.engine.resolver.DefaultTraversableResolver");
		log.setLevel(LoggingProperties.HIBERNATE_LOGS_LEVEL);
		
		log = (Logger) LoggerFactory.getLogger("org.hibernate.validator.xml.ValidationXmlParser");
		log.setLevel(LoggingProperties.HIBERNATE_LOGS_LEVEL);
		
		log = (Logger) LoggerFactory.getLogger("org.hibernate");
		log.setLevel(LoggingProperties.HIBERNATE_LOGS_LEVEL);
		
		/**
		 * SET LEVEL LOGS org.reflections
		 */
		log = (Logger) LoggerFactory.getLogger("org.reflections.Reflections");
		log.setLevel(LoggingProperties.REFLECTION_LOGS_LEVEL);
		
		/**
		 * SET LEVEL LOGS Validator
		 */
		log = (Logger) LoggerFactory.getLogger("org.fixme.core.validation.Validator");
		log.setLevel(LoggingProperties.VALIDATOR_LOGS_LEVEL);
		
		/**
		 * PROJECT LOG LEVEL
		 */
		log = (Logger) LoggerFactory.getLogger("org.fixme");
		log.setLevel(LoggingProperties.PROJECT_LOGS_LEVEL);
		
		/**
		 * CORE LOG LEVEL
		 */
		log = (Logger) LoggerFactory.getLogger("org.fixme.core");
		log.setLevel(LoggingProperties.CORE_LOGS_LEVEL);
	}
}
