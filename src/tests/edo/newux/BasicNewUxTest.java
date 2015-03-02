package tests.edo.newux;

import org.junit.Before;

import pageObjects.edo.NewUxHomePage;
import tests.misc.EdusoftWebTest;

public class BasicNewUxTest extends EdusoftWebTest {

	NewUxHomePage newUxHomePage;

	@Before
	public void setup() throws Exception {
		super.setup();
		newUxHomePage = pageHelper.openCILatestUXLink();
	}
	
	public void loginAsStudent(){
		
	}

}
