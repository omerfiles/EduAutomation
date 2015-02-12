package tests.UnitTests;

import org.junit.Test;

import tests.misc.EdusoftBasicTest;

public class ServicesUnitTests extends EdusoftBasicTest {

	@Test
	public void testStudentServiceSetProgress() throws Exception {
		//for edo peru
		studentService.setProgress("5230494003988", "3797", "31514");

		studentService.checkStudentProgress("5230494003988", "3797", "31514");
	}

}
