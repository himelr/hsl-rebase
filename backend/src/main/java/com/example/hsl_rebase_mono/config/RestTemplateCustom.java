package com.example.hsl_rebase_mono.config;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.web.client.RestTemplate;

public class RestTemplateCustom implements RestTemplateCustomizer {

	@Override
	public void customize(RestTemplate restTemplate) {
		restTemplate.getInterceptors().add(new CustomClientHttpRequestInterceptor());
	}

}
