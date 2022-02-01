package com.example.hsl_rebase_mono.dto.search;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchResponseDto {

	private String type;
	private List<FeatureDto> features;
}
