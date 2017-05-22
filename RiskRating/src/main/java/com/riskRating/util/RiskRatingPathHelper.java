package com.riskRating.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RiskRatingPathHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	
	private String csvDir;
	private String environment;
	private String riskRatingFile;
	
	public RiskRatingPathHelper(String csvDir, String environment, String riskRatingFile) {
		this.csvDir = csvDir;
		this.environment = environment;
		this.riskRatingFile = riskRatingFile;
	}

	public String getLatestFile() throws IOException {
		csvDir = csvDir.endsWith(File.pathSeparator) ? csvDir.substring(0, csvDir.length()-1) : csvDir; 
		String csvFullDir = csvDir + File.separator + environment;

		Collection<File> files = FileUtils.listFiles(new File(csvFullDir), new NameFileFilter(riskRatingFile), TrueFileFilter.INSTANCE);
		if (files == null || files.isEmpty()) throw new IllegalArgumentException("No files found name "+riskRatingFile+" in "+csvFullDir);
		Map<String, FileVO> fileMap = new HashMap<>();
		for (File file: files) {
			BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			
			String dateAndChronId = file.getParent().substring(csvFullDir.length()+1);
			Long dateAndChronIdValue = valueOfDateAndChronId(dateAndChronId);
			LOGGER.info("{} {}", dateAndChronId, dateAndChronIdValue);
			
			
			fileMap.put(
					file.getParent().substring(csvFullDir.length()+1),
					new FileVO (file.getPath(), dateAndChronIdValue, new Date(attr.creationTime().toMillis())));
		}
		
		for (Map.Entry<String,RiskRatingPathHelper.FileVO> entry: fileMap.entrySet()) {
			LOGGER.info("{} {}", entry.getKey(), entry.getValue());
		}
		
		FileVO latestFile = getFileWithMaxDateAndChronIdValue(fileMap);
		LOGGER.info("latestFile: {}", latestFile);
		return latestFile.getPath();
	}

	private Long valueOfDateAndChronId (String dateAndChronId) {
		Pattern p = Pattern.compile("(\\d{4})"+Pattern.quote(File.separator)+"(\\d{2})"+Pattern.quote(File.separator)+"(\\d{2})"+Pattern.quote(File.separator)+"(\\d+)");
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

	private FileVO getFileWithMaxDateAndChronIdValue (Map<String, FileVO> fileMap) {
		FileVO latestFile = null;
		Long maxDateAndChronIdValue = 0l;
		for (Map.Entry<String,RiskRatingPathHelper.FileVO> entry: fileMap.entrySet()) {
			if (entry.getValue().getDateAndCronId().compareTo(maxDateAndChronIdValue) == 1) {
				latestFile = entry.getValue();
				maxDateAndChronIdValue = entry.getValue().getDateAndCronId(); 
			}
		}
		return latestFile;
	}

	public String getCsvDir() {
		return csvDir;
	}

	public void setCsvDir(String csvDir) {
		this.csvDir = csvDir;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getRiskRatingFile() {
		return riskRatingFile;
	}

	public void setRiskRatingFile(String riskRatingFile) {
		this.riskRatingFile = riskRatingFile;
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
