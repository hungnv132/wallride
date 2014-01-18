package org.wallride.core.support;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.wallride.core.domain.Media;

import javax.inject.Inject;

//@Component
public class MediaUtils {

	private Environment environment;

	public MediaUtils(Environment environment) {
		this.environment = environment;
	}

	public String link(Media media) {
		return link(media.getId());
	}

	public String link(String id) {
		return environment.getRequiredProperty("media.url") + id;
	}
}
