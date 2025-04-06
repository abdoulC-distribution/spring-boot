package org.springframework.boot.logging.log4j2;

import org.springframework.boot.logging.LoggingSystem;
import Log4J2LoggingSystem;

public static class Factory implements LoggingSystemFactory {

	private static final boolean PRESENT = ClassUtils
			.isPresent("org.apache.logging.log4j.core.impl.Log4jContextFactory", Factory.class.getClassLoader());

	@Override
	public LoggingSystem getLoggingSystem(ClassLoader classLoader) {
		if (PRESENT) {
			return new Log4J2LoggingSystem(classLoader);
		}
		return null;
	}

}