package pageObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import Objects.GenericTestObject;
import services.Configuration;
import services.Reporter;
import services.TestResultService;
import drivers.GenericWebDriver;

public abstract class GenericPage extends GenericTestObject {
	@Autowired
	protected GenericWebDriver webDriver;
	
//	@Autowired
//	protected Configuration configuration;
	
//	@Autowired
	protected TestResultService  testResultService;
	
//	@Autowired
//	protected AudioService audioService;
	
//	@Autowired
//	protected DbService dbService;
	private String sutUrl=null;
	
	protected Configuration configuration;
	
	Reporter report;

	
	protected static final Logger logger = LoggerFactory.getLogger(GenericPage.class);
	
	public GenericPage(GenericWebDriver webDriver,TestResultService testResultService){
		this.webDriver=webDriver;
		this.testResultService=testResultService;
		this.report=webDriver.getReporter();
		this.configuration=webDriver.getConfiguration();
//		System.out.println(report.toString());
//		this.dbService=webDriver.getDbService();
//		textService=new TextService();
		
	
	}
	public abstract GenericPage waitForPageToLoad()throws Exception;
	public abstract GenericPage OpenPage(String url)throws Exception;
	public String getSutUrl() {
		return sutUrl;
	}
	public void setSutUrl(String sutUrl) {
		this.sutUrl = sutUrl;
	}
}
