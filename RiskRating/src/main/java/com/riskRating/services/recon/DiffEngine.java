package com.riskRating.services.recon;

import static com.riskRating.servicesapi.recon.ComparableFieldEquivalence.byComparableFields;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.riskRating.servicesapi.recon.RowKey;

public final class DiffEngine {

	//private static final Logger LOGGER = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public MapDifference<RowKey, Map<String, String>> doDiff(BigDecimal tolerance, List<String[]> leftCsv, List<String[]> rightCsv) {
		Map<RowKey, Map<String, String>> leftRowMap = toRowMap(leftCsv);
		Map<RowKey, Map<String, String>> rightRowMap = toRowMap(rightCsv);

		MapDifference<RowKey, Map<String, String>> diff = Maps.difference(leftRowMap, rightRowMap, byComparableFields(tolerance));
		return diff;
	}

	public MapDifference<RowKey, Map<String, String>> doDiff(List<String[]> leftCsv, List<String[]> rightCsv) {
		Map<RowKey, Map<String, String>> leftRowMap = toRowMap(leftCsv);
		Map<RowKey, Map<String, String>> rightRowMap = toRowMap(rightCsv);
		//LOGGER.debug("leftRowMap: {}", leftRowMap);
		//LOGGER.debug("rightRowMap: {}", rightRowMap);
		
		MapDifference<RowKey, Map<String, String>> diff = Maps.difference(leftRowMap, rightRowMap);
		return diff;
	}

	private Map<RowKey, Map<String, String>> toRowMap(List<String[]> csv) {
		if (csv.isEmpty()) {
			return ImmutableMap.<RowKey, Map<String, String>> of();
		}
		List<String[]> copyOfInput = Lists.newArrayList(csv);
		String[] headerLine = copyOfInput.remove(0);
		Set<Integer> keyIndexes = getKeyIndexes(headerLine);

		Map<RowKey, Map<String, String>> result = Maps.newHashMap();

		for (String[] line : copyOfInput) {
			result.put(getRowKey(line, keyIndexes), convertLineToRowMap(headerLine, line));
		}

		return result;
	}

	private RowKey getRowKey(String[] line, Set<Integer> keyIndexes) {
		Set<String> result = Sets.newHashSet();
		for (Integer keyIndex : keyIndexes) {
			result.add(line[keyIndex]);
		}
		return RowKey.build(result);
	}

	private Set<Integer> getKeyIndexes(String[] headerLine) {
		Set<Integer> keyIndexes = Sets.newHashSet();
		for (int i = 0; i < headerLine.length; i++) {
			String header = headerLine[i];
			if (StringUtils.endsWithIgnoreCase(header, "_key")) {
				keyIndexes.add(i);
			}
		}
		return keyIndexes;
	}

	private Map<String, String> convertLineToRowMap(String[] headers, String[] line) {
		Map<String, String> row = Maps.newHashMap();
		for (int i = 0; i < line.length; i++) {
			row.put(headers[i], line[i]);
		}
		return row;
	}
}