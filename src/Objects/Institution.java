package Objects;

import java.util.List;

public class Institution extends BasicObject {
	
	private List<Teacher>classTeachers;
	private List<Student>students;
	private List<Course>courses;
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

}
