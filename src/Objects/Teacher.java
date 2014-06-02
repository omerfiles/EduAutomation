package Objects;

public class Teacher extends UserObject {
	
	public Teacher(){
		this.setUserName(configuration.getProperty("teacher.username"));
		this.setPassword(configuration.getProperty("teacher.password"));
	}
	public Teacher(String userName,String password){
		this.setUserName(userName);
		this.setPassword(password);
	}
	

}
