package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoLoginPage;

public class MavenTests extends EdusoftWebTest {
	@Before
	public void setup() throws Exception {
		super.setup();
	}
	
	@Test
	public void testFirefox()throws Exception{
		
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		webDriver.printScreen();
		

	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
