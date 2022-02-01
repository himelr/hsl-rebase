package com.example.hsl_rebase_mono.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@ComponentScan("com.example.hsl_rebase_mono.services")
public class RestConfiguration {

	@Bean
	@DependsOn(value = {"restTemplateCustom"})
	public RestTemplateBuilder restTemplateBuilder() {
		return new RestTemplateBuilder(restTemplateCustom());
	}

	@Bean
	@Qualifier("restTemplateCustom")
	public RestTemplateCustom restTemplateCustom() {
		return new RestTemplateCustom();
	}

}
