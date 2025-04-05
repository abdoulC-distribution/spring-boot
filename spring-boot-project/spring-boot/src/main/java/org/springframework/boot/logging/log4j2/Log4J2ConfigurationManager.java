package org.springframework.boot.logging.log4j2;

public class Log4J2ConfigurationManager {

	protected String[] getStandardConfigLocations() {
		List<String> locations = new ArrayList<>();
		locations.add("log4j2-test.properties");
		if (isClassAvailable("com.fasterxml.jackson.dataformat.yaml.YAMLParser")) {
			Collections.addAll(locations, "log4j2-test.yaml", "log4j2-test.yml");
		}
		if (isClassAvailable("com.fasterxml.jackson.databind.ObjectMapper")) {
			Collections.addAll(locations, "log4j2-test.json", "log4j2-test.jsn");
		}
		locations.add("log4j2-test.xml");
		locations.add("log4j2.properties");
		if (isClassAvailable("com.fasterxml.jackson.dataformat.yaml.YAMLParser")) {
			Collections.addAll(locations, "log4j2.yaml", "log4j2.yml");
		}
		if (isClassAvailable("com.fasterxml.jackson.databind.ObjectMapper")) {
			Collections.addAll(locations, "log4j2.json", "log4j2.jsn");
		}
		locations.add("log4j2.xml");
		String propertyDefinedLocation = new PropertiesUtil(new Properties())
				.getStringProperty(ConfigurationFactory.CONFIGURATION_FILE_PROPERTY);
		if (propertyDefinedLocation != null) {
			locations.add(propertyDefinedLocation);
		}
		return StringUtils.toStringArray(locations);
	}

	protected void loadDefaults(LoggingInitializationContext initializationContext, LogFile logFile) {
		String location = getPackagedConfigFile((logFile != null) ? "log4j2-file.xml" : "log4j2.xml");
		load(initializationContext, location, logFile);
	}

	protected void loadConfiguration(LoggingInitializationContext initializationContext, String location,
			LogFile logFile) {
		load(initializationContext, location, logFile);
	}

	protected void reinitialize(LoggingInitializationContext initializationContext) {
		List<String> overrides = getOverrides(initializationContext);
		if (!CollectionUtils.isEmpty(overrides)) {
			reinitializeWithOverrides(overrides);
		}
		else {
			LoggerContext context = getLoggerContext();
			context.reconfigure();
		}
	}

	private void load(LoggingInitializationContext initializationContext, String location, LogFile logFile) {
		List<String> overrides = getOverrides(initializationContext);
		applySystemProperties(initializationContext.getEnvironment(), logFile);
		loadConfiguration(location, logFile, overrides);
	}

}