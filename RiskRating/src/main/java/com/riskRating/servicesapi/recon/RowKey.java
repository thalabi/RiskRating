package com.riskRating.servicesapi.recon;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.ImmutableSet;

public class RowKey implements Serializable, Comparable<RowKey> {
	private static final long serialVersionUID = 1L;

	public static RowKey build(Set<String> key) {
		return new RowKey(key);
	}

	public static RowKey build(String key) {
		return new RowKey(ImmutableSet.of(key));
	}

	private final Set<String> key;

	private RowKey(Set<String> key) {
		this.key = key;
	}

	public Set<String> getKey() {
		return key;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int compareTo(RowKey o) {
		return CompareToBuilder.reflectionCompare(this, o);
	}
}
