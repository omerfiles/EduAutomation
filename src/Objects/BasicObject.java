package Objects;

import jsystem.framework.system.SystemObjectImpl;

import org.springframework.beans.factory.annotation.Autowired;

import services.Configuration;
import services.PageHelperService;

abstract class BasicObject{
	private String id;
	
	private String name;
	
	@Autowired
	Configuration configuration;
	

	
	
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
