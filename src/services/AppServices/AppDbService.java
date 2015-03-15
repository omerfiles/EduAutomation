package services.AppServices;

import services.DbService;
import services.Reporter;

public class AppDbService extends DbService {
	
	AppConfig appConfig;
	Reporter reporter;

	public AppDbService() throws Exception {
		super();
		this.MAX_DB_TIMEOUT=10;
		appConfig=new AppConfig(null);
		reporter=new Reporter();
		this.report=reporter;
		this.configuration=appConfig;
		
		// TODO Auto-generated constructor stub
	}

	

	

}
