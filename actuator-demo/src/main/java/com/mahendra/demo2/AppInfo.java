package com.mahendra.demo2;

import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.stereotype.Component;

@Component
public class AppInfo implements InfoContributor{

	@Override
	public void contribute(Builder builder) {
		builder.withDetail("app.name", "demo2")
			.withDetail("app.version", "0.1")
			.withDetail("app.runtime", "Java17")
			.build();
		
	}
}
