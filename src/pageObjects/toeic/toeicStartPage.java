package pageObjects.toeic;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import pageObjects.LoginPage;
import services.TestResultService;

public class toeicStartPage extends GenericPage {

	public toeicStartPage(GenericWebDriver webDriver,
			TestResultService testResultService) {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}

	@Override
	public toeicStartPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		webDriver.openUrl(url);
		return this;
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}



}
