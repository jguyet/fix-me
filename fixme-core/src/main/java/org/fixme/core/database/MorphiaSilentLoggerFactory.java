package org.fixme.core.database;

import com.google.code.morphia.logging.Logr;
import com.google.code.morphia.logging.LogrFactory;

public class MorphiaSilentLoggerFactory implements LogrFactory {
	
	 public Logr get(final Class<?> c) {
		 return new JDKSilentLogger(c);
	 }
	 
	 public class JDKSilentLogger implements Logr {

			private static final long serialVersionUID = 1L;

			public JDKSilentLogger(final Class<?> c) {}

			@Override
			public boolean isTraceEnabled() {return false;}

			@Override
			public void trace(String msg) {}

			@Override
			public void trace(String format, Object... arg) {}

			@Override
			public void trace(String msg, Throwable t) {}

			@Override
			public boolean isDebugEnabled() {return false;}

			@Override
			public void debug(String msg) {}

			@Override
			public void debug(String format, Object... arg) {}

			@Override
			public void debug(String msg, Throwable t) {}

			@Override
			public boolean isInfoEnabled() {return false;}

			@Override
			public void info(String msg) {}

			@Override
			public void info(String format, Object... arg) {}

			@Override
			public void info(String msg, Throwable t) {}

			@Override
			public boolean isWarningEnabled() {return false;}

			@Override
			public void warning(String msg) {}

			@Override
			public void warning(String format, Object... arg) {}

			@Override
			public void warning(String msg, Throwable t) {}

			@Override
			public boolean isErrorEnabled() {return false;}

			@Override
			public void error(String msg) {}

			@Override
			public void error(String format, Object... arg) {}

			@Override
			public void error(String msg, Throwable t) {}
		}
}
