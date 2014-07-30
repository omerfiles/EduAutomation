package pageObjects;

import services.TextService;
import Enums.ByTypes;
import Enums.SRWordLevel;
import drivers.GenericWebDriver;

public class SRpage extends GenericPage {

	public SRpage(GenericWebDriver webDriver) {
		super(webDriver);
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
				"//div[@id='txtOriginal']//div//span[@class='"
						+ wordLevel.toString() + "'][contains(text(),"
						+ textService.resolveAprostophes(word) + ")]",
				ByTypes.xpath);

	}
	public void checkWordsLevels(String[] words, String[] wordsScores,
			TextService textService) throws Exception {
		for (int i = 0; i < words.length; i++) {
			CheckWordScore(words[i], Integer.valueOf(wordsScores[i]),
					textService);
		}
	}
	
	public String getWordsScoring() throws Exception {
		String str = webDriver.waitForElement("wl", ByTypes.id).getAttribute(
				"value");
		System.out.println("Random data is:" + str);
		str = str.replace(",", "");
		return str;

	}

}
