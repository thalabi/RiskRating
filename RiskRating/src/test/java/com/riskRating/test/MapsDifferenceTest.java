package com.riskRating.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;
import com.google.common.collect.Maps;
import com.riskRating.util.CsvReaderUtil;

public class MapsDifferenceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	@Test
	public void mapsDifferenceTest() {
		Path leftCsvPath = Paths.get("C:\\temp\\cppib\\csv\\PROD\\2017\\05\\18\\1148\\legal-entity-rating.csv");
		List<String[]> leftCsv = CsvReaderUtil.readCsv(leftCsvPath);
		Path rightCsvPath = Paths.get("C:\\temp\\cppib\\csv\\DEV2\\2017\\05\\16\\1124\\legal-entity-rating.csv");
		List<String[]> rightCsv = CsvReaderUtil.readCsv(rightCsvPath);
		
		Map<String, String[]> leftCsvMap = toRowMap(leftCsv);
		Map<String, String[]> rightCsvMap = toRowMap(rightCsv);
		//LOGGER.debug("leftCsvMap: {}", leftCsvMap);
		//LOGGER.debug("rightCsvMap: {}", rightCsvMap);
		
		MapDifference<String, String[]> diff = Maps.difference(leftCsvMap, rightCsvMap);
		Map<String, ValueDifference<String[]>> entriesDiff = diff.entriesDiffering();
		LOGGER.debug(": {}", entriesDiff.size());
		for (String key: entriesDiff.keySet()) {
			LOGGER.debug("key: {}", key);
		}
//		
//		DiffEngine diffEngine = new DiffEngine();
//		MapDifference<RowKey, Map<String, String>> diff = diffEngine.doDiff(leftCsv, rightCsv);
		
	}
	
	private Map<String, String[]> toRowMap(List<String[]> csvRows) {
		Map<String, String[]> result = new HashMap<>();
		for (String[] row: csvRows) {
			result.put(row[5], row);
			//LOGGER.debug("row[5]: {} = row[0]: {}, row[1]: {}, row[2]: {}, row[3]: {}, row[4]: {}", row[5], row[0], row[1], row[2], row[3], row[4]);
		}
		return result;
	}
}
