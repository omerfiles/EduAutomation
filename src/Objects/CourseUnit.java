package Objects;

import java.util.List;

public class CourseUnit extends BasicObject {
	List<UnitComponent>unitComponenst;

	public List<UnitComponent> getUnitComponent() {
		return unitComponenst;
	}

	public void setUnitComponent(List<UnitComponent> unitComponents) {
		this.unitComponenst = unitComponents;
	}
	public void addUnitComponent(UnitComponent unitComponent){
		unitComponenst.add(unitComponent);
	}
}
