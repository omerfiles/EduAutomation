package tests.edo.newux;

import org.junit.Before;

import pageObjects.edo.newUxHomePage;
import tests.misc.EdusoftWebTest;

public class BasicNewUxTest extends EdusoftWebTest {

	newUxHomePage newUxHomePage;

	@Before
	public void setup() throws Exception {
		super.setup();
		newUxHomePage = pageHelper.openCILatestUXLink();
	}

}
