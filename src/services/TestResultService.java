package services;

import java.util.ArrayList;
import java.util.List;

import jsystem.extensions.report.html.Report;
import jsystem.framework.system.SystemObjectImpl;

import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.springframework.stereotype.Service;

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
		System.out.println("Failure added: "+failCause);
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
		report.report("Failures are:");

		for (int i = 0; i < results.size(); i++) {
			System.out.println("Failure " + i + ": "
					+ results.get(i).toString());
			report.report("Failure " + i + ": " + results.get(i).toString());
		}
	}

	public void assertEquals(String expected, String actual) {
		System.out.println("Asserting "+expected +" and "+actual);
		if (expected != actual) {
			addFailTest("Expected String was: " + expected
					+ " but actual string was: " + actual);
//			throw new ComparisonFailure("Assert failed", expected, actual);

		}

	}

}
