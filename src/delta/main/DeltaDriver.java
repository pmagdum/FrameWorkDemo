package delta.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import com.relevantcodes.extentreports.LogStatus;

import generics.Excel;
import generics.Property;
import generics.Utility;

public class DeltaDriver extends BaseDriver 
{
	//move all the global variables and Beforesuite and Aftersuite in Base driver class.
	//Coz, to make your main class less complex.
	// and beforeSuite and aftersuite will execute only once.
	public String browser1;
	//@Parameters({"browser","rAdd"})
	@BeforeMethod
	public void launchApp(XmlTest value) throws MalformedURLException
	{
		//driver = new FirefoxDriver();
		
		String rAdd=value.getParameter("rAdd");
		 browser1=value.getParameter("browser");
	
	URL u = new URL(rAdd);
		DesiredCapabilities dc = new DesiredCapabilities();
	dc.setBrowserName(browser1);
	dc.setPlatform(Platform.ANY);
	driver=new RemoteWebDriver(u,dc);	
		
		/*
		if(browser1.equals("firefox"))
		{
		driver = new FirefoxDriver();
			
		}
		else
			if(browser1.equals("chrome"))
			{
				System.setProperty("webdriver.chrome.driver","C:/chromedriver.exe");
				driver=new ChromeDriver();
				
			}
			*/
			
		//driver.get("http://demo.actitime.com/login.do"); // create property file.
		String AppUrl = Property.getPropertyValue(configPath,"URL");
		String TimeOut=Property.getPropertyValue(configPath, "TimeOut");
		driver.get(AppUrl);
		driver.manage().timeouts().implicitlyWait(Long.parseLong(TimeOut),TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}
	
	@Test(dataProvider="getScenarios")
	public void testScenarios(String ScenarioSheet, String status)
	{ 
		//String scenarioSheet="Scenario1";  //to store multiple scenarios store it in dataProvider.
				
		testReport = eReport.startTest(browser1+"_"+ScenarioSheet);
		if(status.equalsIgnoreCase("yes"))
		{
			int stepCount= Excel.getRowCount(scenarioPath, ScenarioSheet);
			
			for(int i=1;i<=stepCount;i++)
			{
								
				String description=Excel.getCellValue(scenarioPath,ScenarioSheet,i,0);
				String action = Excel.getCellValue(scenarioPath, ScenarioSheet, i,1);
				String input1 = Excel.getCellValue(scenarioPath, ScenarioSheet, i, 2);
				String input2 = Excel.getCellValue(scenarioPath, ScenarioSheet, i,3);
		
				System.out.println(description);
				System.out.println(action);
				System.out.println(input1);
				System.out.println(input2);
		
				String msg="description:"+description+"  action:"+action+"  input1:"+input1+"  input2:"+input2;
				testReport.log(LogStatus.INFO, msg);
				
				KeyWord.executeKeyWord(driver, action, input1, input2);
				//Assert.fail();
			}//for loop
		}//if
			else
			{
				testReport.log(LogStatus.SKIP, "Execution ststus is NO");
				throw new SkipException("Skipping this scenario");
			}
	}//test method
		
	@AfterMethod
	public void quitApp(ITestResult test)
	{
		if(test.getStatus()==ITestResult.FAILURE)
		{
			String pImage = Utility.getPageScreenShot(driver,imageFolderPath);
			String p=testReport.addScreenCapture(pImage);
			testReport.log(LogStatus.FAIL,"Page screenshot: " +p);
		}
		
		eReport.endTest(testReport);
		driver.close();
	}
		
}
