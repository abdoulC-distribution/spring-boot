package org.springframework.boot.logging.log4j2;

public static class LevelSetLoggerConfig extends LoggerConfig {

	LevelSetLoggerConfig(String name, Level level, boolean additive) {
		super(name, level, additive);
	}

}