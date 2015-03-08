package Interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import Enums.Browsers;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface TestCaseParams {
	String[] testCaseID();
	String testName()  default "";
	String testCategory()default "";
	String []browsers()  default "";
	Browsers testedBrowser() default Browsers.empty;
	boolean ignoreTestTimeout()default false;
	String testTimeOut()default "0";
	String envFile()default "";
	


}
