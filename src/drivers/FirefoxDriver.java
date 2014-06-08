package drivers;

import java.net.URL;
import java.util.Arrays;

import jsystem.framework.report.Reporter;

import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import services.DbService;

@Service
public class FirefoxDriver extends GenericWebDriver {
	
	@Override
	public void init(String remoteUrl, String folderName) throws Exception {
		setBrowserName("firefox");
		setInitialized(true);
		dbService = new DbService();
		report.report("Remote url from pom file is: " + remoteUrl);
		// sutUrl = configuration.getProperty("sut.url");
		logsFolder = folderName;
		try {
			if (remoteUrl == null) {
				// remoteUrl = configuration.getProperty("remote.machine");
			}
			report.startLevel("Initializing WebDriver",
					Reporter.EnumReportLevel.CurrentPlace);

//			 DesiredCapabilities capabilities = new DesiredCapabilities("firefox", "29.0.1", Platform.WINDOWS);
			webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"),
					DesiredCapabilities.firefox());
			// webDriver = new RemoteWebDriver( capabilities);

			report.stopLevel();
		} catch (Exception e) {
			logger.error("Cannot register node or start the remote driver! ", e);
		}
	}
	

}
