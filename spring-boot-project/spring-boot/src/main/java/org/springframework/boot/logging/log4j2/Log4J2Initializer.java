package org.springframework.boot.logging.log4j2;

public class Log4J2Initializer {
	private static final String LOG4J_BRIDGE_HANDLER = "org.apache.logging.log4j.jul.Log4jBridgeHandler";

	private static final String LOG4J_LOG_MANAGER = "org.apache.logging.log4j.jul.LogManager";

	private final ClassLoader classLoader;

	public Log4J2Initializer(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public void beforeInitialize() {
		LoggerContext loggerContext = getLoggerContext();
		if (isAlreadyInitialized(loggerContext)) {
			return;
		}
		if (!configureJdkLoggingBridgeHandler()) {
			super.beforeInitialize();
		}
		loggerContext.getConfiguration().addFilter(FILTER);
	}


	public void initialize(LoggingInitializationContext initializationContext, String configLocation, LogFile logFile) {
		LoggerContext loggerContext = getLoggerContext();
		if (isAlreadyInitialized(loggerContext)) {
			return;
		}
		StatusConsoleListener listener = new StatusConsoleListener(Level.WARN);
		StatusLogger.getLogger().registerListener(listener);
		loggerContext.putObject(STATUS_LISTENER_KEY, listener);
		Environment environment = initializationContext.getEnvironment();
		if (environment != null) {
			loggerContext.putObject(ENVIRONMENT_KEY, environment);
			Log4J2LoggingSystem.propertySource.setEnvironment(environment);
			PropertiesUtil.getProperties().addPropertySource(Log4J2LoggingSystem.propertySource);
		}
		loggerContext.getConfiguration().removeFilter(FILTER);
		super.initialize(initializationContext, configLocation, logFile);
		//markAsInitialized(loggerContext);
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

	private boolean configureJdkLoggingBridgeHandler() {
		try {
			if (isJulUsingASingleConsoleHandlerAtMost() && !isLog4jLogManagerInstalled()
					&& isLog4jBridgeHandlerAvailable()) {
				removeDefaultRootHandler();
				Log4jBridgeHandler.install(false, null, true);
				return true;
			}
		}
		catch (Throwable ex) {
			// Ignore. No java.util.logging bridge is installed.
		}
		return false;
	}

}