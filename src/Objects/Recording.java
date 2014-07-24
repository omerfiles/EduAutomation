package Objects;

import java.io.File;

public class Recording extends BasicObject{
	
	
	private File recordingFile;
	private String expectedScore;
	private String[]wordsScores;
	private long recordingLength;
	
	public Recording(){};
	
	public Recording(String id, File file, String expectedScore,
			String[] wordsScores, long recordingLength) {
//		super();
		this.id=id;
		this.recordingFile = file;
		this.expectedScore = expectedScore;
		this.wordsScores = wordsScores;
		this.recordingLength = recordingLength;
	}
	
	
	public File getFile() {
		return recordingFile;
	}
	public void setRecordingFile(File file) {
		this.recordingFile = file;
	}
	public String getExpectedScore() {
		return expectedScore;
	}
	public void setExpectedScore(String expectedScore) {
		this.expectedScore = expectedScore;
	}
	public String[] getWordsScores() {
		return wordsScores;
	}
	public void setWordsScores(String[] wordsScores) {
		this.wordsScores = wordsScores;
	}
	public long getRecordingLength() {
		return recordingLength;
	}
	public void setRecordingLength(long recordingLength) {
		this.recordingLength = recordingLength;
	}

}
