package com.example.hsl_rebase_mono.controller;

import com.example.hsl_rebase_mono.dto.plan.CalculationDto;
import com.example.hsl_rebase_mono.dto.plan.RouteQueryDto;
import com.example.hsl_rebase_mono.dto.search.SearchResponseDto;
import com.example.hsl_rebase_mono.services.RouteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/route")
public class RouteController {

	private final RouteService routeService;

	public RouteController(RouteService routeService) {
		this.routeService = routeService;
	}

	@PostMapping("/calculated")
	public CalculationDto getCalculatedRoute(@RequestBody RouteQueryDto routeQueryDto) {
		return routeService.getCalculatedRoute(routeQueryDto);
	}

	@GetMapping("/address-search")
	public SearchResponseDto getAddress(@RequestParam String searchTerm) {
		SearchResponseDto searchResponseDto = routeService.searchResponseDto(searchTerm);
		return searchResponseDto;
	}
}
