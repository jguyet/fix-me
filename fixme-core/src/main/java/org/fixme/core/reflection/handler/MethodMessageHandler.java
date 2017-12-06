package org.fixme.core.reflection.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MethodMessageHandler {
	public int value();
}
