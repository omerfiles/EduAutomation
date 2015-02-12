package tests.misc;

import org.junit.Test;

public class CreateStudents extends EdusoftWebTest {
	
	
	@Test
	public void createStudents()throws Exception{
		
		int numOfStudents=1000;
		for(int i=0;i<numOfStudents;i++){
			
			pageHelper.addStudent("student"+dbService.sig(4));
		}
		
		
	}
	
	@Test
	public void createMultipleClassStudents()throws Exception{
//		String []classes=new String[]{"class2","class3","class4","class1"};
		String []classes=new String[]{"Primero","Segundo","Tercero","Cuarto","Quinto"};
		String instId="6550063";
		
		pageHelper.addStudentsToMultileClasses(500, classes,instId);
	}
	
	@Test
	public void createStudentsApi()throws Exception{
		
		int numOfStudents=1000;
		for(int i=0;i<numOfStudents;i++){
//			pageHelper.createUserUsingApi(sut, userName, fname, lname, pass, instId, className);
		}
		
		
	}

}
