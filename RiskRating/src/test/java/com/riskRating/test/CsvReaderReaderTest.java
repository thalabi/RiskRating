package com.riskRating.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.riskRating.util.CsvReaderUtil;

public class CsvReaderReaderTest {

	@Test
	public void readCsvTest() {
		Path csvPath = Paths.get("C:\\temp\\cppib\\csv\\PROD\\2017\\05\\18\\1148\\legal-entity-rating.csv");
		List<String[]> csvRows = CsvReaderUtil.readCsv(csvPath);
		Assert.assertFalse(csvRows.isEmpty());
	}
}
