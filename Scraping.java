package rssroy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;

public class Scraping {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		System.out.println(System.getProperty("user.dir"));
		String pwd = System.getProperty("user.dir");
		System.setProperty("http.proxyHost", "proxylb.icicibankltd.com");
		System.setProperty("http.proxyPort", "80");
		System.setProperty("https.proxyHost", "proxylb.icicibankltd.com");
		System.setProperty("https.proxyPort", "80");

		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver_win32/chromedriver.exe");
		//System.setProperty("webdriver.firefox.driver", pwd+"/geckodriver");
		ChromeOptions option =new ChromeOptions();
		option.addExtensions(new File(pwd+"/Block-image_v1.0.crx"));
		WebDriver driver = new ChromeDriver(option);
		//WebDriver driver = new FirefoxDriver();
		int page_no = 1;
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.get("https://www.eastbay.com/_-_/keyword-air+jordan+shoes?cm_PAGE=180&Rpp=180&Ns=P_NewArrivalDateEpoch%7C1");
		//String href= driver.findElement(By.cssSelector("#endeca_search_results > ul > li:nth-child(1) > a")).getAttribute("href");
		//System.out.println(href);
		 Map<String,String> multiHref=new HashMap<String,String>();  
		String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,"t");
		//driver.findElement(By.cssSelector("#endeca_search_results > ul > li:nth-child(1) > a:nth-child(1) > span.product_image > img")).sendKeys(selectLinkOpeninNewTab);
		//System.exit(0);
		int i=1;
		String moveTo = null;
		String clickOn = null;
		while(true){
			Actions act = new Actions(driver);
			try{
				
				//check for li
				String liClassValue = driver.findElement(By.cssSelector("#endeca_search_results > ul > li:nth-child("+i+")"))
												.getAttribute("class");
				if(liClassValue.equals("clearRow")){
					i++;
					continue;
				}else{
					
					try{
						String href= driver.findElement(By.cssSelector("#endeca_search_results > ul > li:nth-child("+i+") > a")).getAttribute("href");
						String img_url = driver.findElement(By.cssSelector("#endeca_search_results > ul > li:nth-child("+i+") > a:nth-child(1) > span.product_image > img")).getAttribute("src");
						System.out.println("New href--- "+href+"  for li "+i);
						multiHref.put(img_url, href);//.add(img_url,href);
						i++;
					}
					catch(NoSuchElementException ex) {
						System.out.println("Khatam ho gaya link bhai");
					}
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
				break;
			}
			
		}
		driver.close();
		System.out.println("All hrefs all together: ---"+multiHref.toString());
		//int hsize=multiHref.size();
		//Iterator itr = multiHref.iterator();
		WebDriver driver1 = new ChromeDriver(option);
		for(Map.Entry m:multiHref.entrySet())  {
			//driver1.get((String) itr.next());
			driver1.get((String) m.getValue());
			String title=driver1.findElement(By.cssSelector("#product_title")).getText();
			String price = driver1.findElement(By.xpath("//*[@id=\"list_price\"]/text()")).getText();
			List<String> shoesize = new ArrayList<String>();
			List<WebElement> sizes = driver1.findElements(By.cssSelector("#size_selection_list"));
			Iterator str= sizes.iterator();
			while(str.hasNext()) {
				String sz = driver1.findElement(By.cssSelector("a.in-stock")).getText();
				System.out.println("Size to be added ---"+sz);
				shoesize.add(sz);
			}
			Thread.sleep(5000);
		}
		
	}
}


