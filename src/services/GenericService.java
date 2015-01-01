package services;

import Enums.TestRunnerType;

public class GenericService {

	public GenericService() {
		// TODO Auto-generated constructor stub
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
