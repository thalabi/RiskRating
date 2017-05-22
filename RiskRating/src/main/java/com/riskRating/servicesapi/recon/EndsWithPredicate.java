package com.riskRating.servicesapi.recon;

import org.apache.commons.lang3.StringUtils;

//import org.springframework.util.StringUtils;

import com.google.common.base.Predicate;

public final class EndsWithPredicate implements Predicate<String> {

	public static Predicate<String> endsWith(String suffix) {
		return new EndsWithPredicate(suffix);
	}

	private String suffix;

	public EndsWithPredicate(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public boolean apply(String input) {
		return StringUtils.endsWithIgnoreCase(input, suffix);
	}
}