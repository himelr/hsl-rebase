package com.example.hsl_rebase_mono.config;

import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Slf4j
@Configuration
public class WebClientConfiguration {

	private Logger logger = LoggerFactory.getLogger(WebClientConfiguration.class);

	@Bean
	public WebClient webClient() {
		return WebClient
				.builder()
				.filter(logRequest())
				.clientConnector(new ReactorClientHttpConnector(
						HttpClient.create().wiretap("reactor.netty.http.client.HttpClient",
								LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
				))
				.build();
	}

	private ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
			clientRequest.headers().forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
			return Mono.just(clientRequest);
		});
	}
}
