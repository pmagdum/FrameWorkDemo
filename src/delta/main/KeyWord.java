package delta.main;

import org.openqa.selenium.WebDriver;

public class KeyWord 
{
	public static void executeKeyWord(WebDriver driver,String action, String input1,String input2)
	{
		if(action.equalsIgnoreCase("enter"))
		{
		driver.findElement(Locator.getLocator(input1)).sendKeys(input2);
		}
	else				
	if(action.equalsIgnoreCase("click"))
	{
		try{
			Thread.sleep(2000);
		}
		catch(Exception e)
		{}
		driver.findElement(Locator.getLocator(input1)).click();
		System.out.println(input1 + "element is clicked.");
	}
	else
	{
		System.out.println("invalid action"+action);
	}
	}

}
