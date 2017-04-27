package automationFramework;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import automationFramework.pageObjects.GoogleHomePage;
import automationFramework.util.WebUtils;

/**
 * @author chaitu
 *
 *Test Class to implement the test plan for given constraints
 */
public class KargoTestCase {
	
	//Get new web driver instance for this test
	WebDriver driver;
	
	@Before
	public void setUp(){
		driver = new ChromeDriver();
	}
	
	@Test
	public void aboutPageNavigationShouldBeSuccessful() {
		//Go to google
		GoogleHomePage googleHomePage = WebUtils.goToGoogle(driver);
		
		//Confirm that we navigated to google
		Assert.assertTrue("Failed to verify that google search box is displayed", WebUtils.checkIfElementExists(driver, By.name("q")));
		
		//Search for kargo
		googleHomePage.searchGoogleForKeyword(driver, "Kargo");
		
		//Verify that kargo link is located and click on it
		Assert.assertTrue(
			"Falied to locate link to kargo.com",
			WebUtils.findAndClickElement(driver, By.linkText("Kargo"))
		);
		
		Assert.assertTrue(
			"Failed to locate the home div",
			WebUtils.checkIfElementExists(driver, By.cssSelector("div[class='section--hero__content']"))
		);
		
		//verify that navigation to home page is successful
		Assert.assertTrue(
			"Failed to verify that navigation to home page is successful",
			WebUtils.elementHasText(driver, By.cssSelector("div[class='section--hero__content']"), "HOME")
		);
		
		//verify that nav button is located and click on it
		Assert.assertTrue(
			"Failed to locate Nav Menu Button",
			WebUtils.findAndClickElement(driver, By.cssSelector("a[href='#nav-main']"))
		);
		
		//verify that main menu is opened
		Assert.assertTrue(
			"Failed to verify that main menu is opened",
			WebUtils.checkIfElementExists(driver, By.cssSelector("div[class='menu-main-menu-container']"))
		);
		
		//locate the link to about page in the nav menu and click on it
		Assert.assertTrue(
			"Failed to locate about link in the nav menu", 
			WebUtils.findAndClickElement(driver, By.cssSelector("a[href='http://www.kargo.com/about/']"))
		);
		
		Assert.assertTrue(
			"Failed to locate the about div",
			WebUtils.checkIfElementExists(driver, By.cssSelector("div[class='section--hero__content']"))
		);
		
		//Make sure that navigation to about page is successful by finding the text ABOUT US 
		Assert.assertTrue(
			"Failed to verify that navigation to about page is successful",
			WebUtils.elementHasText(driver, By.cssSelector("div[class='section--hero__content']"), "ABOUT US")
		);
	}
	
	@Test
	public void openFileAndSaveSearchResults() {
		try {
			//open file for reading
			BufferedReader fileReader = new BufferedReader(new FileReader("SearchKeys.txt"));
			//create new .xslx work book
			Workbook wb = new XSSFWorkbook();
			String keyword;
			while ((keyword = fileReader.readLine()) != null) {
				//Go to google
				GoogleHomePage googleHomePage = WebUtils.goToGoogle(driver);
				
				//search for keyword
				googleHomePage.searchGoogleForKeyword(driver, keyword);
				
				//create new worksheet in the workbook to store results for the keyword
			    Sheet workSheet = wb.createSheet(keyword + " results");
			    
			    //initialize the sheet
			    workSheet.createRow(0).createCell(0).setCellValue("Google search results for: " + keyword);
			    workSheet.createRow(2).createCell(0).setCellValue("Result Text");
			    workSheet.getRow(2).createCell(1).setCellValue("Result Url");

			    int rowNum = 4;
			    int pageCount = 0;
			    
			    /*
			     * this can be made an infinite loop to fetch all search results 
			     * but google is assuming that this script is a bot if we direct too many search queries
			     * so for now, I am collecting the search results from first 5 pages or until the next page is found.
			     */
			    while (pageCount < 5) {
			    	pageCount++;
				    
				    List<WebElement> searchResults = driver.findElements(By.xpath("//h3[@class='r']/a"));
				    
				    for (WebElement result : searchResults) {
				    	String href = result.getAttribute("href");
				    	String text = result.getText();
				    	if (!text.equals("")) {
				    		workSheet.createRow(rowNum).createCell(0).setCellValue(text);
				    		workSheet.getRow(rowNum++).createCell(1).setCellValue(href);
				    	}
				    }
				    
				    boolean foundNextPage = false;
				    
			    	try {
			    		//try to find the next page of search results
			    		foundNextPage = WebUtils.findAndClickElement(driver, By.linkText("Next"));
			    	} catch (WebDriverException we) {
			    		//sometimes the element can be detached from dom so refresh and try again
			    		driver.navigate().refresh();
			    		foundNextPage = WebUtils.findAndClickElement(driver, By.linkText("Next"));
			    	}
			    	
			    	//stop if there is no next page
			    	if (!foundNextPage) {
			    		break;
			    	}
			    	
			    	//implicitly wait to synchronize the requests
			    	driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			    }
			}
			
			//output stream to write the results
			FileOutputStream fout=new FileOutputStream(new File("GoogleSearchResults.xlsx"));
			wb.write(fout);
			
			//kill all the resources
			wb.close();
			fileReader.close();
			fout.close();
		} catch (IOException io) {
			System.out.println("exception while reading file" + io);
		}	
	}
	
	@After
	public void tearDown() {
		driver.quit();
	}
}
