package com.riskRating.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.riskRating.servicesapi.exception.RiskRatingServicesRuntimeException;

import au.com.bytecode.opencsv.CSVReader;

public class CsvReaderUtil {

	public static List<String[]> readCsv(Path path) {
		if (!Files.exists(path)) {
			return ImmutableList.<String[]>of();
		}
		List<String[]> result;
		try (CSVReader csvReader = new CSVReader(new InputStreamReader(Files.newInputStream(path)))) {
			result = csvReader.readAll();
		} catch (IOException e) {
			throw new RiskRatingServicesRuntimeException(e);
		}
		return result;
	}

}
