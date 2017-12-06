package org.fixme.market.socket.reflection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MarketClassMessageHandler {
	public String value();
}
