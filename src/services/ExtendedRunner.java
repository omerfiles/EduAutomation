package services;

import java.util.ArrayList;
import java.util.List;

import org.json.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import Interfaces.TestCaseParams;

;

public class ExtendedRunner extends BlockJUnit4ClassRunner {

	private String testId;

	public ExtendedRunner(Class<?> klass)
			throws org.junit.runners.model.InitializationError {
		super(klass);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Statement methodBlock(FrameworkMethod method) {
		// TODO Auto-generated method stub
		TestCaseParams params = method.getAnnotation(TestCaseParams.class);
		if (params != null) {
			for (int i = 0; i < params.testCaseID().length; i++) {
				System.out.println("Covers test case id:"
						+ params.testCaseID()[i]);
			}
			System.setProperty("testCaseId", params.testCaseID()[0]);
		}
		return super.methodBlock(method);
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	// get scenraio name from naven command line
//	@Override
//	protected List<FrameworkMethod> computeTestMethods() {
//
//		// load test names from scenario file
//
//		boolean useScenarioFile = false;
//		TextService textService = new TextService();
//		List<String[]> testNames = null;
//		try {
//			String filePath = System.getProperty("scenarioFile");
//			if (filePath == null) {
//				filePath = "files//csvFiles/testScenarios/regression.csv";
//			}
//			testNames = textService.getStr2dimArrFromCsv(filePath);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		// First, get the base list of tests
//		final List<FrameworkMethod> allMethods = getTestClass()
//				.getAnnotatedMethods();
//		if (allMethods == null || allMethods.size() == 0)
//			return allMethods;
//
//		// Filter the list down
//		final List<FrameworkMethod> filteredMethods = new ArrayList<FrameworkMethod>(
//				allMethods.size());
//		if (useScenarioFile == true) {
//
//			for (final FrameworkMethod method : allMethods) {
//				final TestCaseParams customAnnotation = method
//						.getAnnotation(TestCaseParams.class);
//				if (customAnnotation != null) {
//					// Add to accepted test methods, if matching criteria met
//					// For example `if(currentOs.equals(customAnnotation.OS()))`
//
//					for (int i = 0; i < testNames.size(); i++) {
//						if (testNames.get(i)[0].equals(customAnnotation
//								.testName())) {
//							filteredMethods.add(method);
//							break;
//						}
//					}
//
//				} else {
//
//					// If test method doesnt have the custom annotation, either
//					// add
//					// it to
//					// the accepted methods, or not, depending on what the
//					// 'default'
//					// behavior
//					// should be
//					// filteredMethods.add(method);
//				}
//			}
//		} else {
//			return allMethods;
//		}
//
//		return filteredMethods;
//	}
//
//	
//	
	
	@Override
	protected void validateInstanceMethods(List<Throwable> errors) {
        validatePublicVoidNoArgMethods(After.class, false, errors);
        validatePublicVoidNoArgMethods(Before.class, false, errors);
        validateTestMethods(errors);

//        if (computeTestMethods().size() == 0)
//            errors.add(new Exception("No runnable methods"));
       
    }

	@Override
	protected Object createTest() throws Exception {
		// TODO Auto-generated method stub
		return super.createTest();
	}

}

// }
