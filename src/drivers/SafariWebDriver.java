package drivers;

import java.net.URL;

import jsystem.framework.report.Reporter;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import services.DbService;

public class SafariWebDriver extends GenericWebDriver {

		@Override
		public void init(String remoteUrl, String folderName) throws Exception {
			setBrowserName("safari");
			setInitialized(true);
			dbService = new DbService();
			report.report("Remote url from pom file is: " + remoteUrl);
			logsFolder = folderName;
			try {
				if (remoteUrl == null) {
					// remoteUrl = configuration.getProperty("remote.machine");
				}
//				report.startLevel("Initializing SafariWebDriver",
//						Reporter.EnumReportLevel.CurrentPlace);

//				 DesiredCapabilities capabilities = new DesiredCapabilities("firefox", "29.0.1", Platform.WINDOWS);
				webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"),
						DesiredCapabilities.safari());
				// webDriver = new RemoteWebDriver( capabilities);

				report.stopLevel();
			} catch (Exception e) {
				logger.error("Cannot register node or start the remote driver! ", e);
			}
		}
		
	}


