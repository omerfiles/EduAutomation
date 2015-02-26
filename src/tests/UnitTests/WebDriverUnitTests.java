package tests.UnitTests;

import java.io.File;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import drivers.IEWebDriver;
import Enums.Browsers;
import tests.misc.EdusoftWebTest;

public class WebDriverUnitTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void testOpenIE() throws Exception {
		setBrowser(Browsers.IE.toString());
		super.setup();
		Assert.assertTrue(webDriver instanceof IEWebDriver);

	}

	@Test
	public void takeScreenshotLocal() throws Exception {
		String fileName = "unitTestPrintScreen" + dbService.sig();
		String path= webDriver.printScreen(fileName);

		System.out.println("Check if file was created");
		File file = new File(path);
		testResultService.assertEquals(true, file.exists());
	}

	@Test
	public void testTakeScreenShot() {

	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
