package delta.main;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import generics.Excel;

public class BaseDriver implements AutomationConstants
{
	//Store remaining global variables in interface called AutomatonConstants and implement this interface to BaseDriver class.
	
	//for extent report add extentReport and freemarker jar files.
	//associate jar files: 1. freemarker-2.3.23.jar    2.extentreports-java-2.41.1.jar
public WebDriver driver;
public static ExtentReports eReport; 
public ExtentTest testReport;

@BeforeTest
public void initFrameWork()
{
	eReport = new ExtentReports(reportFilePath);
}
	
@AfterTest
public void endFrameWork()
{
	eReport.flush();
}
	
	@DataProvider
	public  String [][] getScenarios()
	{
		int scenarioCount = Excel.getRowCount(path, sheet);
		String [][] data = new String[scenarioCount][2];
		
		for(int i=1;i<=scenarioCount;i++)
		{
			String ScenarioName=Excel.getCellValue(path,sheet,i,0);
			String status = Excel.getCellValue(path,sheet, i,1);
			
			System.out.println("ScenarioName = "+ScenarioName);
			System.out.println("Status = "+status);
			
			data[i-1][0] = ScenarioName;
			data[i-1][1]=status;
			
		}
		
		return data;
		
	}
}
