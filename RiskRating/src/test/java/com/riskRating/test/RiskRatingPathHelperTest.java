package com.riskRating.test;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.riskRating.util.RiskRatingPathHelper;

public class RiskRatingPathHelperTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	
	private String riskRatingFile = "legal-entity-rating.csv";
	
	@Rule
	public TemporaryFolder folder= new TemporaryFolder();
	
	@Test
	public void test1() throws IOException {
		String csvDir = folder.getRoot().getAbsolutePath();
		String environment = "PROD";

		File subFolder1 = folder.newFolder(environment, "2017", "06", "22", "777777");
		File file1 = new File(subFolder1.getPath()+File.separator+riskRatingFile);
		Assert.assertTrue(file1.createNewFile());
		File subFolder2 = folder.newFolder(environment, "2017", "06", "23", "127");
		File file2 = new File(subFolder2.getPath()+File.separator+riskRatingFile);
		Assert.assertTrue(file2.createNewFile());
		File subFolder3 = folder.newFolder(environment, "2017", "06", "23", "63281");
		File file3 = new File(subFolder3.getPath()+File.separator+riskRatingFile);
		Assert.assertTrue(file3.createNewFile());
		File subFolder4 = folder.newFolder(environment, "2017", "06", "24", "127");
		File file4 = new File(subFolder4.getPath()+File.separator+riskRatingFile);
		Assert.assertTrue(file4.createNewFile());
		File subFolder5 = folder.newFolder(environment, "2017", "09", "01", "1");
		File file5 = new File(subFolder5.getPath()+File.separator+riskRatingFile);
		Assert.assertTrue(file5.createNewFile());

		RiskRatingPathHelper rrph = new RiskRatingPathHelper(csvDir, environment, riskRatingFile);
		String latestFile = rrph.getLatestFile();
		LOGGER.info("latestFile: {}", latestFile);		
	}

}
