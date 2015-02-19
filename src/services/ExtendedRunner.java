package services;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.internal.ParameterisedTestClassRunner;
import junitparams.internal.TestMethod;

import org.junit.After;
import org.junit.Before;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import Enums.Browsers;
import Interfaces.TestCaseParams;

;

public class ExtendedRunner extends JUnitParamsRunner {

	private String testId;
	String defaultTestTimeOut = "10";
	private ParameterisedTestClassRunner parameterisedRunner;

	public ExtendedRunner(Class<?> klass)
			throws org.junit.runners.model.InitializationError {
		super(klass);
		parameterisedRunner = new ParameterisedTestClassRunner(getTestClass());
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	@Override
	protected void validateInstanceMethods(List<Throwable> errors) {
		validatePublicVoidNoArgMethods(After.class, false, errors);
		validatePublicVoidNoArgMethods(Before.class, false, errors);
		validateTestMethods(errors);

	}

	private int count;

	// String testPrefix="_";

	public int nextCount() {
		return count++;
	}

	public int count() {
		return count;
	}

	public void getBrowserParam(TestMethod method) {
		if (method.parametersSets().length > 0) {
			
			System.out.println("Got browser param: "+ method.parametersSets()[count].toString());
			System.setProperty("browserParam",
					method.parametersSets()[nextCount()].toString());
		}
	}

	// @Override
	// protected Statement methodBlock(FrameworkMethod method) {
	// // TODO Auto-generated method stub
	//
	// TestMethod testMethod = parameterisedRunner.testMethodFor(method);
	// getBrowserParam(testMethod);
	// return super.methodBlock(method);
	// }
	
	
//	@Override
//	protected Statement methodBlock(FrameworkMethod method) {
//		// TODO Auto-generated method stub
//
//		TestMethod testMethod = parameterisedRunner.testMethodFor(method);
//		getBrowserParam(testMethod);
//		return super.methodBlock(method);
//	}
	
	
	@Override
	protected Statement methodBlock(FrameworkMethod method) {
		// TODO Auto-generated method stub

		TestMethod testMethod = parameterisedRunner.testMethodFor(method);
		getBrowserParam(testMethod);

		TestCaseParams params = method.getAnnotation(TestCaseParams.class);
		if (params != null) {
			for (int i = 0; i < params.testCaseID().length; i++) {
				System.out.println("Covers test case id:"
						+ params.testCaseID()[i]);
			}
			System.setProperty("testCaseId", params.testCaseID()[0]);

			if (params.testedBrowser() != Browsers.empty) {
				System.setProperty("browserParam", params.testedBrowser()
						.toString());

			}

			if (params.ignoreTestTimeout() == true) {
				System.setProperty("ignoreTineOut", "true");
			}

		}
		try {
			if (params.equals(null)) {
				System.setProperty("testTimeOut", defaultTestTimeOut);
			} else {
				if (!params.testTimeOut().equals("0")) {
					System.setProperty("testTimeOut", params.testTimeOut());
				} else {
					System.setProperty("testTimeOut", defaultTestTimeOut);
				}
			}
		}

		catch (NullPointerException e) {
			System.setProperty("testTimeOut", defaultTestTimeOut);
		}

		System.out.println("Timeout is set to: "
				+ System.getProperty("testTimeOut") + " minutes");

		return super.methodBlock(method);
	}

	@Override
	protected void validatePublicVoidNoArgMethods(
			Class<? extends Annotation> annotation, boolean isStatic,
			List<Throwable> errors) {
		 List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);

//	        for (FrameworkMethod eachTestMethod : methods) {
//	            eachTestMethod.validatePublicVoidNoArg(isStatic, errors);
//	        }
	}
}

// }
