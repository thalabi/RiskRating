package com.riskRating.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	public PathTest() {
		// TODO Auto-generated constructor stub
	}

	@Test
	public void test1() {
		Path sourcePath = Paths.get("C:\\temp\\cppib\\csv\\PROD\\2017\\05\\18\\1148\\legal-entity-rating.csv");
		Path targetPath = Paths.get("C:\\temp\\cppib\\csv\\UAT\\2017\\05\\16\\7\\legal-entity-rating.csv");
		Assert.assertTrue(Files.exists(sourcePath));
		Assert.assertTrue(Files.exists(targetPath));
		LOGGER.info(": {}", Files.exists(sourcePath));
	}
	
	@Test
	public void test2() throws IOException {
		String sourceDir = "C:\\temp\\cppib\\csv\\PROD";
		List<String> fileNames = new ArrayList<>();
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(sourceDir))) {
            for (Path path : directoryStream) {
                fileNames.add(path.toString());
            }
        } catch (IOException e) {Assert.fail(e.getMessage());}
		LOGGER.info(": {}", fileNames);
	}
	
	@Test
	public void test3() {
		File sourceDir = new File("C:\\temp\\cppib\\csv\\PROD");
		String[] extensions = new String[]{"csv"};
		Collection<File> files = FileUtils.listFiles(sourceDir, extensions, true);
		LOGGER.info(": {}", files);
	}

	@Test
	public void test4() {
		File sourceDir = new File("C:\\temp\\cppib\\csv\\PROD");
		Collection<File> files = FileUtils.listFiles(sourceDir, new NameFileFilter("legal-entity-rating.csv"), TrueFileFilter.INSTANCE);
		LOGGER.info(": {}", files);
		for (File file: files) {
			LOGGER.info(": {} {} {} {}", file.getPath(), file.getAbsolutePath(), file.getName(), file.getAbsoluteFile());
		}
	}
	@Test
	public void test5() throws IOException {
		File sourceDir = new File("C:\\temp\\cppib\\csv\\PROD");
		String riskRatingFile = "legal-entity-rating.csv";
		
		Collection<File> files = FileUtils.listFiles(sourceDir, new NameFileFilter(riskRatingFile), TrueFileFilter.INSTANCE);
		Map<String, FileVO> fileMap = new HashMap<>();
		for (File file: files) {
			BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			
			String dateAndChronId = file.getParent().substring(sourceDir.getPath().length()+1);
			Long dateAndChronIdValue = valueOfDateAndChronId(dateAndChronId);
			LOGGER.info("{} {}", dateAndChronId, dateAndChronIdValue);
			
			
			fileMap.put(
					file.getParent().substring(sourceDir.getPath().length()+1),
					new FileVO (file.getPath(), dateAndChronIdValue, new Date(attr.creationTime().toMillis())));
		}
		
		for (Map.Entry<String,PathTest.FileVO> entry: fileMap.entrySet()) {
			LOGGER.info("{} {}", entry.getKey(), entry.getValue());
		}
		
		FileVO latestFile = getLatestFile(fileMap);
		LOGGER.info("latestFile: {}", latestFile);
	}
	
	private Long valueOfDateAndChronId (String dateAndChronId) {
		Pattern p = Pattern.compile("(\\d{4})\\\\(\\d{2})\\\\(\\d{2})\\\\(\\d+)");
		Matcher m = p.matcher(dateAndChronId);
		if (m.matches()) {
			return Long.valueOf(m.group(1))*Double.valueOf(Math.pow(10, 10)).longValue() + 
				   Long.valueOf(m.group(2))*Double.valueOf(Math.pow(10, 8)).longValue() +
				   Long.valueOf(m.group(3))*Double.valueOf(Math.pow(10, 6)).longValue() +
				   Long.valueOf(m.group(4));
		} else {
			throw new IllegalArgumentException("Path does not match pattern yyyy/mm/dd/chronId.");
		}
	}
	
	@Test
	public void valueOfDateAndChronIdTest () {
		String dateAndChronId1 = "2017\\05\\12\\1";
		Long dateAndChronIdValue1 = valueOfDateAndChronId(dateAndChronId1);
		Assert.assertEquals(Long.valueOf(20170512000001l), dateAndChronIdValue1);
		String dateAndChronId2 = "2016\\12\\31\\99999";
		Long dateAndChronIdValue2 = valueOfDateAndChronId(dateAndChronId2);
		Assert.assertEquals(Long.valueOf(20161231099999l), dateAndChronIdValue2);
		
		Assert.assertTrue(dateAndChronIdValue1.compareTo(dateAndChronIdValue2) == 1);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void valueOfDateAndChronIdTest_invalidArgument () {
		String dateAndChronId1 = "2017\\05\\12\\";
		valueOfDateAndChronId(dateAndChronId1);
	}
	@Test (expected=IllegalArgumentException.class)
	public void valueOfDateAndChronIdTest_invalidArgument_2 () {
		String dateAndChronId1 = "2017\\5\\12\\1";
		valueOfDateAndChronId(dateAndChronId1);
	}
	@Test (expected=IllegalArgumentException.class)
	public void valueOfDateAndChronIdTest_invalidArgument_3 () {
		String dateAndChronId1 = "2017\\05\\2\\1";
		valueOfDateAndChronId(dateAndChronId1);
	}
	
	private FileVO getLatestFile (Map<String, FileVO> fileMap) {
		FileVO latestFile = null;
		Long maxDateAndChronIdValue = 0l;
		for (Map.Entry<String,PathTest.FileVO> entry: fileMap.entrySet()) {
			if (entry.getValue().getDateAndCronId().compareTo(maxDateAndChronIdValue) == 1) {
				latestFile = entry.getValue();
				maxDateAndChronIdValue = entry.getValue().getDateAndCronId(); 
			}
		}
		return latestFile;
	}
	
	@Test
	public void patternQuoteTest() {
		String quoteFileSeparator = Pattern.quote(File.separator);
		LOGGER.info("quotesFileSeparator: {}", quoteFileSeparator);
	}
	@Test
	public void patternQuoteUnixFileSeparatorTest() {
		String quoteFileSeparator = Pattern.quote("/");
		LOGGER.info("quotesFileSeparator: {}", quoteFileSeparator);
		Pattern p = Pattern.compile("(\\d{4})"+quoteFileSeparator+"(\\d{2})"+quoteFileSeparator+"(\\d{2})"+quoteFileSeparator+"(\\d+)");
		Matcher m = p.matcher("2017/05/21/1234");
		Assert.assertTrue(m.matches());
		Assert.assertEquals("2017", m.group(1));
		Assert.assertEquals("05", m.group(2));
		Assert.assertEquals("21", m.group(3));
		Assert.assertEquals("1234", m.group(4));
	}
	class FileVO {
		String path;
		Long dateAndCronId;
		Date creationTime;
		
		FileVO (String path, Long dateAndCronId, Date creationTime) {
			this.path = path;
			this.dateAndCronId = dateAndCronId;
			this.creationTime = creationTime;
		}
	    public String toString() {
	        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	    }
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public Long getDateAndCronId() {
			return dateAndCronId;
		}
		public void setDateAndCronId(Long dateAndCronId) {
			this.dateAndCronId = dateAndCronId;
		}
		public Date getCreationTime() {
			return creationTime;
		}
		public void setCreationTime(Date creationTime) {
			this.creationTime = creationTime;
		}
	}
}
