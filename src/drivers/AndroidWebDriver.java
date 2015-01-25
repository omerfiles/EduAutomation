package drivers;



import java.net.URL;

import org.junit.Assert;
import org.openqa.selenium.By;
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
	public void init(String remoteUrl, String folderName) throws Exception {
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
			webDriver = new RemoteWebDriver(DesiredCapabilities.android());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
		

	}

	@Override
	public void maximize() {
		// TODO Auto-generated method stub
		
	}
	
	
}
