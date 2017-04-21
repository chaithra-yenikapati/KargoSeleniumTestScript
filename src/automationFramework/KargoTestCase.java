package automationFramework;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class KargoTestCase {
	
	WebDriver driver = new ChromeDriver();
	
	@Test
	public void aboutPageNavigationShouldBeSuccessful() {
		driver.get("https://www.google.com");
		WebElement searchElement = driver.findElement(By.name("q"));
		searchElement.clear();
		searchElement.sendKeys("Kargo");
		searchElement.click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("resultStats")));
		
		WebElement kargoSiteLink = driver.findElement(By.linkText("Kargo"));
		kargoSiteLink.click();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[href='#nav-main']")));
		WebElement navButton = driver.findElement(By.cssSelector("a[href='#nav-main']"));
		navButton.click();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[href='http://www.kargo.com/about/']")));
		
		WebElement about = driver.findElement(By.cssSelector("a[href='http://www.kargo.com/about/']"));
		about.click();
		
		System.out.println("opened google");
	}
	
	@After
	public void tearDown() {
		driver.quit();
	}
}
