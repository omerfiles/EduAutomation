package pageObjects.tms;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;

public class StudentRecordingsPage extends GenericPage {

	public StudentRecordingsPage(GenericWebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		webDriver.waitForElement("Student's Recordings", "linkText");
		return this;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public StudentRecordingsPage playStudentRecording() throws Exception {
		webDriver.waitForElement("playBtnStdRecImg", "id").click();
		return this;
	}

	public void scorePhonemes(int score) throws Exception {
		webDriver.waitForElement("//*[@id='RatePanel']/div[1]/ul/li[" + score
				+ "]/div/input", "xpath");
	}

	public void scoreIntonationAndStress(int score) throws Exception {
		webDriver.waitForElement("//*[@id='RatePanel']/div[2]/ul/li[" + score
				+ "]/div/input", "xpath");
	}

	public void scoreOverallPronunciation(int score) throws Exception {
		webDriver.waitForElement("//*[@id='RatePanel']/div[3]/ul/li[" + score
				+ "]/div/input", "xpath");
	}

	public String getFinalScore() throws Exception {
		String finalScore = webDriver.waitForElement("RateScore", "id")
				.getText();
		return finalScore;
	}

	public String getAutomatedScore() throws Exception {
		String automatedScore = webDriver.waitForElement("AutoScore", "id")
				.getText();
		return automatedScore;
	}

	public StudentRecordingsPage selectRecording() throws Exception {
		webDriver.waitForElement("rbRec", "id").click();
		return this;
	}

}
