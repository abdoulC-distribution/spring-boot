package org.springframework.boot.builder;

import org.springframework.context.ConfigurableApplicationContext;

public static class ParentContextAvailableEvent extends ApplicationEvent {

	public ParentContextAvailableEvent(ConfigurableApplicationContext applicationContext) {
		super(applicationContext);
	}

	public ConfigurableApplicationContext getApplicationContext() {
		return (ConfigurableApplicationContext) getSource();
	}

}