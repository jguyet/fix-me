package org.fixme.core.reflection.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation ClassHandler for detect if class is handler class
 * @author jguyet
 * @Commented
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ClassMessageHandler {
	/**
	 * @param String value
	 * @Default String value name of the handler
	 * @return
	 */
	public String value();
}
