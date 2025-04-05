package org.springframework.boot.logging.log4j2;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.status.StatusConsoleListener;
import org.apache.logging.log4j.status.StatusLogger;
import org.springframework.core.env.Environment;
import org.springframework.boot.logging.LoggingSystem;


public class Log4J2Initializer {
	private static final String LOG4J_BRIDGE_HANDLER = "org.apache.logging.log4j.jul.Log4jBridgeHandler";

	private static final String LOG4J_LOG_MANAGER = "org.apache.logging.log4j.jul.LogManager";

	private final ClassLoader classLoader;

	public Log4J2Initializer(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public void beforeInitialize() {
		if (!configureJdkLoggingBridgeHandler()) {
			super.beforeInitialize();
		}
	}


	public Runnable getShutdownHandler() {
		return () -> getLoggerContext().stop();
	}

	private LoggerContext getLoggerContext() {
		return (LoggerContext) LogManager.getContext(false);
	}

	private boolean isAlreadyInitialized(LoggerContext loggerContext) {
		return LoggingSystem.class.getName().equals(loggerContext.getExternalContext());
	}

}