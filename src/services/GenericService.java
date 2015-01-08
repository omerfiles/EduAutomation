package services;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import Enums.TestRunnerType;

public abstract class GenericService {
	
	
	Reporter reporter;

	public GenericService() {
		// TODO Auto-generated constructor stub
	}
	
	private void init() {
		
	}
	
	public void sleep(int seconds) throws Exception {
		Thread.sleep(seconds * 1000);
	}
	
	public TestRunnerType getTestRunner() {
		// if test is run in debug/development
		TestRunnerType testRunner = null;
		if (System.getProperty("remote.machine") != null) {
			testRunner = TestRunnerType.CI;
		} else if (System.getProperty("remote.machine") == null) {
			testRunner = TestRunnerType.local;
		}
		return testRunner;
	}

}
