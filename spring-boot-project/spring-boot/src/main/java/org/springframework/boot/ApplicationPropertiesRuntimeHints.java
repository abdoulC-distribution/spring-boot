package org.springframework.boot;

import org.springframework.boot.context.properties.bind.BindableRuntimeHintsRegistrar;

public class ApplicationPropertiesRuntimeHints implements RuntimeHintsRegistrar {

	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
		BindableRuntimeHintsRegistrar.forTypes(ApplicationProperties.class).registerHints(hints, classLoader);
	}
}