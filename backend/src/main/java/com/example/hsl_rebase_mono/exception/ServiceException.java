package com.example.hsl_rebase_mono.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

	private final String message;

	public ServiceException(String message) {
		super(message);
		this.message = message;
	}
}
