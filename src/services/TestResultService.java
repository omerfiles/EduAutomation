package services;

import java.util.ArrayList;
import java.util.List;

import jsystem.extensions.report.html.Report;
import jsystem.framework.system.SystemObjectImpl;

import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import drivers.GenericWebDriver;

@Service
public class TestResultService extends SystemObjectImpl {



	List<String> results = new ArrayList<String>();

	public void addFailTest(String failCause, boolean terminateTest) {
		results.add(failCause);
		if (terminateTest == true) {
			Assert.fail("Terminating test");
		}

	}

	public void addFailTest(String failCause) {
		System.out.println("Failure added: " + failCause);
		results.add(failCause);

	}

	public boolean hasFailedResults() {
		boolean hasFailedTest = false;
		if (results.size() > 0) {
			hasFailedTest = true;
		}
		return hasFailedTest;
	}

	public List<String> getFailedCauses() {
		return results;
	}

	public void assertTrue(String message, boolean condition) {
		if (condition != true) {

			addFailTest(message);
		}

	}

	public void printAllFailures() {
		System.out.println("Failures are:");
	

		for (int i = 0; i < results.size(); i++) {
			System.out.println("Failure " + i + ": "
					+ results.get(i).toString());
			
		}
	}

	public void assertEquals(String expected, String actual) {
		assertEquals(expected, actual, null);
	}

	public boolean assertEquals(String expected, String actual, String message) {
		// System.out.println("Asserting " + expected + ". and " + actual +
		// ".");
		if (expected.equals(actual) == false) {

			addFailTest("Expected String was: " + expected
					+ " but actual string was: " + actual + " " + message);

			// throw new ComparisonFailure("Assert failed", expected, actual);
			return false;
		}
		return true;
		// System.out.println("Assert passed");

	}

	public void assertEquals(boolean expected, boolean actual) throws Exception {
		assertEquals(expected, actual, null);
	}

	public boolean assertEquals(boolean expected, boolean actual, String message)
			throws Exception {
		boolean result = true;
		System.out.println("Asserting " + expected + ". and " + actual + ".");
		if (expected != actual) {
			System.out.println(message);
			addFailTest("Expected boolean was: " + expected
					+ " but actual boolean was: " + actual + " " + message);

			// throw new ComparisonFailure("Assert failed", expected, actual);
			result = false;
			
		}
		return result;

	}

	public boolean assertEquals(int expected, int acutal,String message) {
		if (expected!=acutal) {

			addFailTest("Expected int was: " + expected
					+ " but actual int was: " + acutal + " " + message);
			
			// throw new ComparisonFailure("Assert failed", expected, actual);
			return false;
		}
		return true;
		
	}

}
