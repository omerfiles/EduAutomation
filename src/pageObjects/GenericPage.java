package pageObjects;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import services.TextService;
import drivers.GenericWebDriver;

public abstract class GenericPage {
	@Autowired
	GenericWebDriver webDriver;

	
	protected static final Logger logger = LoggerFactory.getLogger(GenericPage.class);
	
	public GenericPage(GenericWebDriver webDriver){
		this.webDriver=webDriver;
//		textService=new TextService();
	
	}
	public abstract GenericPage waitForPageToLoad()throws Exception;
}
