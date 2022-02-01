package com.example.hsl_rebase_mono.services.clients;

import com.example.hsl_rebase_mono.dto.search.SearchResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
public class AddressWebClient {

	@Value("${hsl.search-url}")
	private String searchUrl;

	private WebClient routeClient;

	public AddressWebClient(WebClient webClient) {
		routeClient = webClient;
	}

	public SearchResponseDto getAddresses(String searchTerm) {
		return routeClient.get()
				.uri(searchUrl, uri -> uri
						.queryParam("text", searchTerm)
						.queryParam("size", 7)
						.queryParam("layers", "address")
						.build())
				.retrieve()
				.bodyToMono(SearchResponseDto.class)
				.block(Duration.of(5, ChronoUnit.SECONDS));
	}
}
