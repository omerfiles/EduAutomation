package tests.edo.newux;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxHomePage;
import Interfaces.TestCaseParams;

public class CarouselAndNavBarTests extends BasicNewUxTest {

	NewUxHomePage homePage;

	@Before
	public void setup() throws Exception {
		super.setup();
		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,
				testResultService);
		// pageHelper.setUserLoginToNull("5230643000004");
		homePage = loginPage.loginAsStudent();
	}

	@Ignore
	@TestCaseParams(testCaseID = { "19135" })
	public void CaruselMavigation() throws Exception {
		homePage.carouselNavigateNext();
		sleep(1);
		System.out.println(homePage.getUnitName());
		;
		homePage.carouselNavigateBack();
		webDriver.printScreen("After back");
		System.out.println(homePage.getUnitName());
		;

	}

	@Test
	@TestCaseParams(testCaseID = { "20035" })
	public void testNavigationBar() throws Exception {
	
		report.report("Check that the nav bar is open");
		IsNavBarOpen();
		
		
		
		homePage.clickToOpenNavigationBar();
		homePage.getNavigationBarStatus();

		String notif = homePage.getNavBarItemsNotification("4");
		testResultService.assertEquals("56", notif,
				"Number of notifications not found");

		checkffNavBarItemDisabled("3");
		
		checkIfNavBarItemAlerted("2");
		
		report.report("close the nav bar");
		homePage.clickToOpenNavigationBar();
		IsNavBarClosed();

	}

	private void IsNavBarOpen() throws Exception {
		boolean isNavBarOpen=homePage.isNavBarOpen();
		testResultService.assertEquals(true, isNavBarOpen,"Nav bar is not dislayed");
	}
	private void IsNavBarClosed() throws Exception {
		boolean isNavBarOpen=homePage.isNavBarOpen();
		testResultService.assertEquals(false, isNavBarOpen,"Nav bar is not closed");
	}

	private void checkIfNavBarItemAlerted(String id) throws Exception {
		boolean isEnabled = homePage.isNavBarItemAlerted(id);
		testResultService.assertEquals(true, isEnabled);
		
	}

	private void checkffNavBarItemDisabled(String id) throws Exception {
		boolean isEnabled = homePage.isNavBarItemEnabled(id);
		testResultService.assertEquals(true, isEnabled);
	}

	@After
	public void tearDown() throws Exception {
		homePage.clickOnLogOut();
		super.tearDown();
	}

}
