package pageObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import services.Configuration;
import services.DbService;
import drivers.GenericWebDriver;

public abstract class GenericPage {
	@Autowired
	protected GenericWebDriver webDriver;
	
	@Autowired
	protected Configuration configuration;
	
	
	protected DbService dbService;

	
	protected static final Logger logger = LoggerFactory.getLogger(GenericPage.class);
	
	public GenericPage(GenericWebDriver webDriver){
		this.webDriver=webDriver;
		this.dbService=webDriver.getDbService();
//		textService=new TextService();
	
	}
	public abstract GenericPage waitForPageToLoad()throws Exception;
	public abstract GenericPage OpenPage(String url)throws Exception;
}
