package com.example.hsl_rebase_mono.enums;

public enum ERole {

	X0(0),
	USER(1),
	MODERATOR(2),
	ADMIN(3);

	private int value;

	ERole(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
