package pageObjects;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import services.TestResultService;
import services.TextService;
import Enums.ByTypes;
import Enums.SRWordLevel;
import drivers.GenericWebDriver;

public class SRpage extends GenericPage {

	public SRpage(GenericWebDriver webDriver,TestResultService testResultService) {
		super(webDriver,testResultService);
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	public void CheckWordScore(String word, int expectedWordLevel,
			TextService textService) throws Exception {
		System.out.println("Checking WL of word: "+word+". Expected WL is: "+expectedWordLevel);
		boolean found = false;
		SRWordLevel wordLevel = null;
		if (expectedWordLevel <= 2) {
			wordLevel = SRWordLevel.failed;
		} else if (expectedWordLevel > 2 && expectedWordLevel <= 4) {
			wordLevel = SRWordLevel.pass;
		} else if (expectedWordLevel > 4 && expectedWordLevel <= 6) {
			wordLevel = SRWordLevel.success;
		}
		webDriver.waitForElement(
				"//div[@id='txtOriginal']//span[@class='"
						+ wordLevel.toString() + "'][contains(text(),"
						+ textService.resolveAprostophes(word) + ")]",
				ByTypes.xpath);

	}
	public void checkWordsLevels(String[] words, String[] wordsScores,
			TextService textService) throws Exception {
		webDriver.printScreen("checkWordsLevels");
		try {
			for (int i = 0; i < words.length; i++) {
				CheckWordScore(words[i], Integer.valueOf(wordsScores[i]),
						textService);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Word  is missing");
		}
	}
	
	public String getWordsScoring(String elementId) throws Exception {
		String str = webDriver.waitForElement(elementId, ByTypes.id).getAttribute(
				"value");
		System.out.println("word scoring is:" + str);
		str = str.replace(",", "");
		return str;

	}
	
	public void allowMicFirefox() throws AWTException{
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_TAB);
		webDriver.sleep(500);
		robot.keyPress(KeyEvent.VK_TAB);
		webDriver.sleep(500);
		robot.keyPress(KeyEvent.VK_TAB);
		webDriver.sleep(500);
		robot.keyPress(KeyEvent.VK_ENTER);
	}

}
