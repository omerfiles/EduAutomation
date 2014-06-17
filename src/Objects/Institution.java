package Objects;

import java.util.List;

import Enums.SchoolImpTypes;

public class Institution extends BasicObject {
	
	private List<Teacher>classTeachers;
	private List<Student>students;
	private List<Course>courses;
	private SchoolAdmin schoolAdmin;
	private String phone;
	private String host;
	private String numberOfComonents;
	private String numberOfUsers;
	private String concurrentUsers;
	private String email;
	private SchoolImpTypes schoolImpType;
	
	
	public List<Teacher> getClassTeachers() {
		return classTeachers;
	}
	public void setClassTeachers(List<Teacher> classTeachers) {
		this.classTeachers = classTeachers;
	}
	public List<Student> getStudents() {
		return students;
	}
	public void setStudents(List<Student> students) {
		this.students = students;
	}
	public List<Course> getCourses() {
		return courses;
	}
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	public SchoolAdmin getSchoolAdmin() {
		return schoolAdmin;
	}
	public void setSchoolAdmin(SchoolAdmin schoolAdmin) {
		this.schoolAdmin = schoolAdmin;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getNumberOfComonents() {
		return numberOfComonents;
	}
	public void setNumberOfComonents(String numberOfComonents) {
		this.numberOfComonents = numberOfComonents;
	}
	public String getNumberOfUsers() {
		return numberOfUsers;
	}
	public void setNumberOfUsers(String numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}
	public String getConcurrentUsers() {
		return concurrentUsers;
	}
	public void setConcurrentUsers(String concurrentUsers) {
		this.concurrentUsers = concurrentUsers;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public SchoolImpTypes getSchoolImpType() {
		return schoolImpType;
	}
	public void setSchoolImpType(SchoolImpTypes schoolImpType) {
		this.schoolImpType = schoolImpType;
	}
	
	public void printDetails(){
		
	}

}
