package com.riskRating.servicesapi.recon;

import static com.riskRating.servicesapi.recon.EndsWithPredicate.endsWith;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hamcrest.number.BigDecimalCloseTo;

import com.google.common.base.Equivalence;
import com.google.common.collect.Sets;

/**
 * This Equivalence is used to compare Abacus analytics from different
 * environments. The tolerance value represents the percentage difference
 * allowable between environments, for any individual value.
 */
public final class ComparableFieldEquivalence extends Equivalence<Map<String, String>> {
	private static final String SUFFIX_COMPARE = "_comp";
	private BigDecimal tolerance;

	public static ComparableFieldEquivalence byComparableFields(BigDecimal tolerance) {
		return new ComparableFieldEquivalence(tolerance);
	}

	public ComparableFieldEquivalence(BigDecimal tolerance) {
		super();
		this.tolerance = tolerance;
	}

	@Override
	protected boolean doEquivalent(Map<String, String> a, Map<String, String> b) {
		Set<String> columnsToCompare = Sets.newHashSet(Sets.filter(a.keySet(), endsWith(SUFFIX_COMPARE)));

		for (String column : columnsToCompare) {
			if (doEquivalent(a.get(column), b.get(column))) {
				continue;
			} else {
				return false;
			}
		}

		return true;
	}

	private boolean doEquivalent(String left, String right) {
		BigDecimal bigDecimalLeft = new BigDecimal(left);
		BigDecimal bigDecimalRight = new BigDecimal(right);

		BigDecimal error = bigDecimalLeft.multiply(tolerance).abs();

		return new BigDecimalCloseTo(bigDecimalLeft, error).matchesSafely(bigDecimalRight);
	}

	@Override
	protected int doHash(Map<String, String> t) {
		Set<String> columnsToCompare = Sets.newHashSet(Sets.filter(t.keySet(), endsWith(SUFFIX_COMPARE)));

		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder()
				.append(t.keySet());

		for (String column : columnsToCompare) {
			hashCodeBuilder.append(t.get(column));
		}

		return hashCodeBuilder.toHashCode();
	}
}