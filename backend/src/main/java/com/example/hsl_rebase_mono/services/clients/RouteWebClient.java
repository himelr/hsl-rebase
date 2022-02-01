package com.example.hsl_rebase_mono.services.clients;

import com.example.hsl_rebase_mono.dto.plan.LatLonDto;
import com.example.hsl_rebase_mono.dto.plan.PlanDataDto;
import com.example.hsl_rebase_mono.dto.plan.PlanRequestDto;
import com.example.hsl_rebase_mono.dto.plan.PlanResponseDto;
import com.example.hsl_rebase_mono.dto.plan.RouteQueryDto;
import com.example.hsl_rebase_mono.util.GraphqlSchemaReaderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
public class RouteWebClient  {

	@Value("${hsl.route-url}")
	private String routeUrl;

	private WebClient routeClient;

	public RouteWebClient(WebClient webClient) {
		routeClient = webClient;
	}

	public PlanDataDto getRoute(RouteQueryDto routeQueryDto) {
		PlanRequestDto planRequestDto = new PlanRequestDto();

		final String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("getRoute");
		planRequestDto.setQuery(formQuery(routeQueryDto, query));

		return routeClient.post()
				.uri(routeUrl)
				.bodyValue(planRequestDto)
				.retrieve()
				.bodyToMono(PlanResponseDto.class)
				.map(PlanResponseDto::getData)
				.block(Duration.of(20, ChronoUnit.SECONDS));
	}

	private String formQuery(RouteQueryDto routeQueryDto, String query) {
		return query
				.replace("$from", getLatLon(routeQueryDto.getFrom()))
				.replace("$to", getLatLon(routeQueryDto.getTo()));
	}

	private String getLatLon(LatLonDto latLonDto) {
		return "{lat: " + latLonDto.getLatitude() + ", lon: " + latLonDto.getLongitude() + "}";
	}
}
