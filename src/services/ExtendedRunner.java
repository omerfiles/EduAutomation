package services;

import org.junit.internal.runners.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


public class ExtendedRunner extends SpringJUnit4ClassRunner {

	public ExtendedRunner(Class<?> klass)
			throws org.junit.runners.model.InitializationError, InitializationError {
		super(klass);
		// TODO Auto-generated constructor stub
	}

//	@Override
//	protected org.junit.runners.model.Statement methodBlock(
//			FrameworkMethod method) {
//		// TODO Auto-generated method stub
//
//		final TestCaseParams customAnnotation = method
//				.getAnnotation(TestCaseParams.class);
//		if (customAnnotation != null) {
//			for (int i = 0; i < customAnnotation.testCaseID().length; i++) {
//				System.out.println("Test case id is:"
//						+ customAnnotation.testCaseID()[i]);
//			}
//		}
//		else
//		{
//			System.out.println("Test case id is missing");
//		}
//
//		return super.methodBlock(method);
//	}

	// @Override
	// protected List<FrameworkMethod> computeTestMethods() {
	// // First, get the base list of tests
	// // Filter the list down
	// List<FrameworkMethod> filteredMethods = null;
	// try {
	// final List<FrameworkMethod> allMethods = getTestClass()
	// .getAnnotatedMethods(TestCaseParams.class);
	// if (allMethods == null || allMethods.size() == 0)
	// return allMethods;
	//
	// filteredMethods = new ArrayList<FrameworkMethod>(
	// allMethods.size());
	// for (final FrameworkMethod method : allMethods) {
	// final TestCaseParams customAnnotation = method
	// .getAnnotation(TestCaseParams.class);
	// if (customAnnotation != null) {
	// // Add to accepted test methods, if matching criteria met
	// // For example `if(currentOs.equals(customAnnotation.OS()))`
	// System.out.println(customAnnotation.testCaseID());
	// filteredMethods.add(method);
	// } else {
	// // If test method doesnt have the custom annotation, either add it to
	// // the accepted methods, or not, depending on what the 'default' behavior
	// // should be
	// filteredMethods.add(method);
	// }
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return filteredMethods;
	// }

	// @Override
	// protected Description methodDescription(Method method) {
	// TODO Auto-generated method stub
	// method.getAnnotation(TestCaseParams.class);
	// TestCaseParams tc = method.getAnnotation(TestCaseParams.class);
	// String testCaseId = tc.testCaseID();
	// System.out.println(testCaseId);
	// return super.methodDescription(method);
}

// }
