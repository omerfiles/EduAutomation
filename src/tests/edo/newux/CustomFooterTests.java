package tests.edo.newux;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import drivers.GenericWebDriver;
import pageObjects.edo.NewUxHomePage;
import services.TestResultService;
import tests.misc.EdusoftWebTest;
import Enums.ByTypes;
import Interfaces.TestCaseParams;

public class CustomFooterTests extends BasicNewUxTest {
		
//	NewUxHomePage newUxHomePage; 
	@Before
	public void setup() throws Exception{
		super.setup();
//		newUxHomePage = pageHelper.openCILatestUXLink("20150217.8");
	}
		
	@Ignore
	@TestCaseParams(testCaseID = { "4564564","4564465" })
	public void CustomFooterEdusoft() throws Exception {
			
	//Verify 'About' link & label
	String label = "About Edusoft";	
	String url = "http://www.edusoftlearning.com/"; 
	newUxHomePage.checkCustomAboutLink(url, label);
		
    //Verify 'Contact Us' mailto
	String mailto = "info@edusoftlearning.com";
	newUxHomePage.checkCustomContactUsLink(mailto);
	
	//Verify LOGO image & link
	String logoLink = "http://www.edusoftlearning.com/?o=edo";
	String imageFileName = "edusoftNew.svg";
	newUxHomePage.checkCustomLogo(logoLink, imageFileName);
	 
	//Verify Privacy Statement & Legal Notices white label values
	String whiteLabel = "Edusoft";
	newUxHomePage.checkCustomPrivacyLegal(whiteLabel);
			
	}
	
	@After
	public void tearDown()throws Exception{
		super.tearDown();
	}

}
