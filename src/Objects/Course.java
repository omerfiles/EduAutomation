package Objects;

import java.util.ArrayList;
import java.util.List;

public class Course extends BasicObject {
	List<CourseUnit>courseUnits=new ArrayList<CourseUnit>();

	public List<CourseUnit> getCourseUnits() {
		return courseUnits;
	}

	public void setCourseUnits(List<CourseUnit> courseUnits) {
		this.courseUnits = courseUnits;
	}
	
	public void AddUnit(CourseUnit courseUnit){
		courseUnits.add(courseUnit);
	}

}
