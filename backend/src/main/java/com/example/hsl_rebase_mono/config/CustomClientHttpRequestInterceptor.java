package com.example.hsl_rebase_mono.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	private Logger logger = LoggerFactory.getLogger(CustomClientHttpRequestInterceptor.class);

	private void log(HttpRequest request) {
		logger.info("Headers: {}", request.getHeaders());
		logger.info("Request Method: {}", request.getMethod());
		logger.info("Request URI: {}", request.getURI());
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
		log(httpRequest);
		return clientHttpRequestExecution.execute(httpRequest, bytes);
	}
}