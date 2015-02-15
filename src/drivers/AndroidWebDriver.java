package drivers;



import io.selendroid.client.SelendroidDriver;
import io.selendroid.standalone.SelendroidConfiguration;

import java.net.URL;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import services.DbService;

@Service
public class AndroidWebDriver extends GenericWebDriver {

	@Override
	public void init(String remoteUrl, boolean useProxy) throws Exception {
//		System.out.println("remote url in chrome webdriver: " + remoteUrl);
//		DesiredCapabilities capa = SelendroidCapabilities.android();
//		SelendroidDriver selendroidDriver  = new SelendroidDriver("http://localhost:5555/wd/hub",capa);
//		WebElement inputField = selendroidDriver.findElement(By.id("my_text_field"));
//		Assert.assertEquals("true", inputField.getAttribute("enabled"));
//		inputField.sendKeys("Selendroid");
//		Assert.assertEquals("Selendroid", inputField.getText());
//		selendroidDriver.quit();
//		WebDriver driver=new RemoteWebDriver(DesiredCapabilities.android());
//		selendroidDriver.get("http://edonew.qa.com");
		
		
		try {
			SelendroidConfiguration configuration=new SelendroidConfiguration();
			
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			SelendroidDriver selendroidDriver=new SelendroidDriver(capabilities);
//			selendroidDriver.getScreenshotAs(target)
			
//			selendroidDriver.
			
			webDriver = new RemoteWebDriver(DesiredCapabilities.android());
			setBrowserName("android");
			setInitialized(true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
		

	}

	@Override
	public void maximize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String printScreen(String message, String level) throws Exception {
		// TODO Auto-generated method stub
		return null;
		
	}
	
	
	
	
}
