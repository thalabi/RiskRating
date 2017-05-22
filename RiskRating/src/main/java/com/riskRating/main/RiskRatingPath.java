package com.riskRating.main;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.riskRating.util.RiskRatingPathHelper;

public class RiskRatingPath {

	private static final Logger LOGGER = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	
	public RiskRatingPath() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		RiskRatingPath rrp = new RiskRatingPath();
		rrp.run();
	}

	private void run() throws IOException {
		String csvDir = "C:\\temp\\cppib\\csv";
		String environment = "PROD";
		String riskRatingFile = "legal-entity-rating.csv";
		
		RiskRatingPathHelper rrph = new RiskRatingPathHelper(csvDir, environment, riskRatingFile);
		String latestFile = rrph.getLatestFile();
		LOGGER.info("latestFile: {}", latestFile);
		
		rrph.setEnvironment("DEV2");
		String latestFile2 = rrph.getLatestFile();
		LOGGER.info("latestFile2: {}", latestFile2);

		rrph.setEnvironment("UAT");
		String latestFile3 = rrph.getLatestFile();
		LOGGER.info("latestFile3: {}", latestFile3);

	}
}
