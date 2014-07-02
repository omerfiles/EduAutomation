package Objects;

import jsystem.framework.system.SystemObjectImpl;

import org.springframework.beans.factory.annotation.Autowired;

import services.Configuration;
import services.InstitutionService;
import services.PageHelperService;

abstract class BasicObject {
	protected String id;
	
	protected String name;
	
	@Autowired
	Configuration configuration;
	@Autowired
	InstitutionService institutionService;
	

	
	
	void create(){};
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}



}
