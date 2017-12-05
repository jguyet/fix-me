package org.fixme.broker.socket.reflection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BrokerClassMessageHandler {
	public String value();
}
