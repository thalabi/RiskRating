package com.riskRating.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;
import com.riskRating.services.recon.DiffEngine;
import com.riskRating.servicesapi.recon.RowKey;
import com.riskRating.util.CsvReaderUtil;

public class DiffEngineTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	@Test
	public void diffEngineTest() {
		Path leftCsvPath = Paths.get("C:\\temp\\cppib\\csv\\PROD\\2017\\05\\18\\1148\\legal-entity-rating.csv");
		List<String[]> leftCsv = CsvReaderUtil.readCsv(leftCsvPath);
		Path rightCsvPath = Paths.get("C:\\temp\\cppib\\csv\\DEV2\\2017\\05\\16\\1124\\legal-entity-rating.csv");
		List<String[]> rightCsv = CsvReaderUtil.readCsv(rightCsvPath);
		
		DiffEngine diffEngine = new DiffEngine();
		MapDifference<RowKey, Map<String, String>> diff = diffEngine.doDiff(leftCsv, rightCsv);
		
		Map<RowKey, ValueDifference<Map<String,String>>> entriesDiff = diff.entriesDiffering();
		LOGGER.debug("entriesDiff.size(): {}", entriesDiff.size());
		for (Map.Entry<RowKey, ValueDifference<Map<String,String>>> entry: entriesDiff.entrySet()) {
			LOGGER.debug("key: {}, leftValue(): {}, rightValue(): {}", entry.getKey(), entry.getValue().leftValue(), entry.getValue().rightValue());
		}
		
		Map<RowKey,Map<String,String>> entriesOnLeft = diff.entriesOnlyOnLeft();
		LOGGER.debug("entriesOnLeft.size(): {}", entriesOnLeft.size());
		for (RowKey rowKey: entriesOnLeft.keySet())
			LOGGER.debug("rowKey: {}", rowKey);

		Map<RowKey,Map<String,String>> entriesOnRight = diff.entriesOnlyOnRight();
		LOGGER.debug("entriesOnRight.size(): {}", entriesOnRight.size());
		for (RowKey rowKey: entriesOnRight.keySet())
			LOGGER.debug("rowKey: {}", rowKey);

	}
}
