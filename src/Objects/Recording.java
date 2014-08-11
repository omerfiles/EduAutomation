package Objects;

import java.io.File;

public class Recording extends BasicObject{
	
	
	private File recordingFile;
	private int SL;
	private String[]WL;
	
	
	public Recording(){};
	
	public Recording(String id, File file, int expectedScore,
			String[] wordsScores, long recordingLength) {
//		super();
		this.id=id;
		this.recordingFile = file;
		this.SL = expectedScore;
		this.WL = wordsScores;
	
	}
	
	
	public File getFile() {
		return recordingFile;
	}
	public void setRecordingFile(File file) {
		this.recordingFile = file;
	}
	public int getSL() {
		return SL;
	}
	public void setSL(int expectedScore) {
		this.SL = expectedScore;
	}
	public String[] getWL() {
		return WL;
	}
	public void setWL(String[] wordsScores) {
		this.WL = wordsScores;
	}
	

}
