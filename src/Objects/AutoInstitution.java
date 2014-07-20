package Objects;

public class AutoInstitution {
	
	public AutoInstitution(String institutionId, String institutionName,
			String studentUserName, String teacherUserName,
			String studentPassword, String teacherPassword,
			String institutionDirectoryName) {
		super();
		InstitutionId = institutionId;
		this.institutionName = institutionName;
		this.studentUserName = studentUserName;
		this.teacherUserName = teacherUserName;
		this.studentPassword = studentPassword;
		this.teacherPassword = teacherPassword;
		this.institutionDirectoryName = institutionDirectoryName;
	}
	private String InstitutionId;
	private String institutionName;
	private String studentUserName;
	private String teacherUserName;
	private String studentPassword;
	private String teacherPassword;
	private String institutionDirectoryName;
	
	
	
	
	
	
	public String getInstitutionId() {
		return InstitutionId;
	}
	public String getInstitutionName() {
		return institutionName;
	}
	public String getStudentUserName() {
		return studentUserName;
	}
	public String getTeacherUserName() {
		return teacherUserName;
	}
	public String getStudentPassword() {
		return studentPassword;
	}
	public String getTeacherPassword() {
		return teacherPassword;
	}
	public String getInstitutionDirectoryName() {
		return institutionDirectoryName;
	}
	
	

}
