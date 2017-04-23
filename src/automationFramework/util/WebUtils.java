package automationFramework.util;


import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.PageFactory;
import automationFramework.pageObjects.GoogleHomePage;

/**
 * @author chaitu
 * 
 * utility class to implement most used functionalities in web testing
 */
public class WebUtils {
	public static boolean findAndClick(WebDriver driver, By by) {
		/* Utility function to locate an element and click it*/
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
		} catch (TimeoutException te) {
			// return false if the test timeout before finding the element
    		return false;
    	}
		
		//element is found, get the element and click it
		WebElement clickable = driver.findElement(by);
	    clickable.click();
	    return true;
	}
	
	public static GoogleHomePage goToGoogle(WebDriver driver) {
		/* Utility function to go to google home page */
		driver.get("https://google.com");
		return PageFactory.initElements(driver, GoogleHomePage.class);
	}
	
	public static boolean checkIfElementExists(WebDriver driver, By by) {
		/* Utility function to check if an element exists */
		return driver.findElements(by).size() > 0;
	}
}
