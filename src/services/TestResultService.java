package services;

import java.util.ArrayList;
import java.util.List;

import jsystem.extensions.report.html.Report;
import jsystem.framework.system.SystemObjectImpl;

import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import drivers.GenericWebDriver;

@Service("testResultService")
public class TestResultService {

	List<String> results = new ArrayList<String>();
	List<String> consoleLogEntries = new ArrayList<String>();
	
	@Autowired
	Reporter reporter;

	private GenericWebDriver webDriver;

	public void addFailTest(String failCause, boolean terminateTest,
			boolean takeScreenshot) throws Exception {
		results.add(failCause);
		reporter.report("Failure added: " + failCause);
		if (takeScreenshot) {
			webDriver.printScreen(failCause);
		}

		if (terminateTest == true) {
			Assert.fail("Terminating test");
		}

	}

	public void addFailTest(String failCause) {
		System.out.println("Failure added: " + failCause);
		reporter.report("Failure added: " + failCause);
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

	public void assertElementText(WebElement element, String expectedText) throws Exception {
		String actualText = element.getText();

		if (assertEquals(expectedText, actualText,
				"Text do not match for element: " + element.getAttribute("id"))) {
			webDriver.highlightElement(element);
			webDriver.printScreen("elementTextDoNotMatch");
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

	public boolean assertEquals(int expected, int acutal, String message) {
		if (expected != acutal) {

			addFailTest("Expected int was: " + expected
					+ " but actual int was: " + acutal + " " + message);

			// throw new ComparisonFailure("Assert failed", expected, actual);
			return false;
		}
		return true;

	}

	public List<String> getConsoleLogEntries() {
		return consoleLogEntries;
	}

	public void setConsoleLogEntries(List<String> consoleLogEntries) {
		this.consoleLogEntries = consoleLogEntries;
	}

	public void setWebDriver(GenericWebDriver webDriver) {
		this.webDriver = webDriver;
	}

}
