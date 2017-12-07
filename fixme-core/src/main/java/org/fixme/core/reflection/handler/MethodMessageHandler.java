package org.fixme.core.reflection.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation Method Handler for trace Methods Message Handlers by Value MESSAGE_ID
 * @author jguyet
 * @Commented
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MethodMessageHandler {
	/**
	 * @param int value
	 * @Default handle messageID
	 * @return
	 */
	public int value();
}
