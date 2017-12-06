package org.fixme.core.protocol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation Message Identify
 * @author jguyet
 * @Commented
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface AnnotationMessageID {
	
	/**
	 * @default annotation var<br>
	 * Integer of Message Identify
	 */
	public int value();
}
