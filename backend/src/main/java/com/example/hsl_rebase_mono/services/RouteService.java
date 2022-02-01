package com.example.hsl_rebase_mono.services;

import com.example.hsl_rebase_mono.dto.plan.CalculationDto;
import com.example.hsl_rebase_mono.dto.plan.ItineraryDto;
import com.example.hsl_rebase_mono.dto.plan.LegDto;
import com.example.hsl_rebase_mono.dto.plan.PlanDataDto;
import com.example.hsl_rebase_mono.dto.plan.RouteQueryDto;
import com.example.hsl_rebase_mono.dto.search.SearchResponseDto;
import com.example.hsl_rebase_mono.exception.ServiceException;
import com.example.hsl_rebase_mono.services.clients.AddressWebClient;
import com.example.hsl_rebase_mono.services.clients.RouteWebClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RouteService {

	private final RouteWebClient routeWebClient;
	private final AddressWebClient addressWebClient;

	public RouteService(RouteWebClient routeWebClient, AddressWebClient addressWebClient) {
		this.routeWebClient = routeWebClient;
		this.addressWebClient = addressWebClient;
	}

	public CalculationDto getCalculatedRoute(RouteQueryDto routeQueryDto) {
		PlanDataDto planDataDto = routeWebClient.getRoute(routeQueryDto);

		CalculationDto calculationDto = new CalculationDto();

		double distance = calculateDistance(planDataDto.getPlan().getItineraries());

		calculationDto.setDistance(distance);
		calculationDto.setTotalCaloriesBurned(calculateCaloriesBurned(distance));
		calculationDto.setCo2Emissions(calculateEmissions(distance, 6.1));
		return calculationDto;
	}

	public SearchResponseDto searchResponseDto(String searchTerm) {
		return addressWebClient.getAddresses(searchTerm);
	}

	//https://www.verywellfit.com/how-many-calories-you-burn-during-exercise-4111064#citation-2
	private long calculateCaloriesBurned(double distance) {

		//distance / speed -> minutes
		double duration = distance / 4 * 60 ;

		//3 = Slow-paced walking MET (metabolic equivalent for task)
		int weight = 70;
		return Math.round(duration * (3 * 3.5 * weight) / 200);
	}

	private double calculateDistance(List<ItineraryDto> itineraryDtos) {
		return round(itineraryDtos.stream().findFirst()
				.orElseThrow(() -> new ServiceException("Failed to fetch route"))
				.getLegs().stream().map(LegDto::getDistance).reduce(0.0d, Double::sum)  / 1000, 1);
	}

	//https://comcar.co.uk/emissions/co2litre/
	private double calculateEmissions(double distance, double consumptionPer100) {
		return round(2.3035 * distance * (consumptionPer100 / 100), 2);
	}

	private static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}

		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
