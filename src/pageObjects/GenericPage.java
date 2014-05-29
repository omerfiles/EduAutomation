package pageObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import services.Configuration;
import drivers.GenericWebDriver;

public abstract class GenericPage {
	@Autowired
	protected GenericWebDriver webDriver;
	
	@Autowired
	protected Configuration configuration;

	
	protected static final Logger logger = LoggerFactory.getLogger(GenericPage.class);
	
	public GenericPage(GenericWebDriver webDriver){
		this.webDriver=webDriver;
//		textService=new TextService();
	
	}
	public abstract GenericPage waitForPageToLoad()throws Exception;
	public abstract GenericPage OpenPage(String url)throws Exception;
}
