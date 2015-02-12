package tests.misc.CI;

import org.junit.Test;

import Enums.ByTypes;
import tests.misc.EdusoftWebTest;

public class CIHelper extends EdusoftWebTest {

	@Test
	public void getCILatestLink() throws Exception {
		webDriver.openUrl("http://vstf2013:9010/WebUX");

	String link=	webDriver
				.waitForElement(
						"//div[@class='container']//table//tbody//tr[1]//td//div[1]//div//a",
						ByTypes.xpath).getAttribute("href");
		webDriver.openUrl(link);
		System.out.println("opened");
	}

}
