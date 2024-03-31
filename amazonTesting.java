package admitKard_UI_Automation.testcases.roughWork;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.TimeUnit;

public class amazonshoppingTest2 {


    static String productName = "iphone12";
    static int numberOfItemAddToCart = 2;
    static ArrayList<String> allSearchItemList = new ArrayList<>();




        public static void main(String[] args) throws InterruptedException {
            // Set the path to ChromeDriver
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/browserDriver/chromedriver");

            // Initialize WebDriver
            WebDriver driver = new ChromeDriver();
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            try {



                // Maximize the browser window
                driver.manage().window().maximize();

                // Set implicit wait
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

                // Navigate to the website
                driver.get("https://www.amazon.in/");

                // Test Case 1: Add specific item to cart
                addSpecificItemToCart(driver);

//               Test Case 2: Delivery address should be saved
                saveDeliveryAddress(driver);

                // Test Case 3: Add multiple items and then remove them from cart
                addAndRemoveItemsFromCart(driver);

            }
            catch (Exception e){
                System.out.println(e);
                driver.quit();
            }

            // Close the browser
            driver.quit();
        }

        // Test Case 1: Add specific item to cart
        public static void addSpecificItemToCart(WebDriver driver) throws InterruptedException {
            // Find the item and click on add to cart button
            //search item.
            getListItem(driver);

            String firstItemFromListLink = getFirstItemFromList(driver);
            driver.get(firstItemFromListLink);
            Thread.sleep(3000);
            driver.findElement(By.xpath("(//input[@value='Add to Cart'])")).click();
            driver.findElement(By.id("attach-close_sideSheet-link")).click();
            Thread.sleep(2000);
            String cartCount  = driver.findElement(By.id("nav-cart-count")).getText();
            Assert.assertEquals(Integer.parseInt(cartCount),1 );


            // Assert that the item is added to the cart
            // Add your assertion code here
        }

        // Test Case 2: Delivery address should be saved
        public static void saveDeliveryAddress(WebDriver driver) throws InterruptedException {

            // Move to checkout page
            driver.findElement(By.id("nav-cart-count")).click();
            driver.findElement(By.name("proceedToRetailCheckout")).click();


            // Assert that the delivery address is saved
            Thread.sleep(2000);
            Assert.assertEquals(driver.findElement(By.className("a-spacing-small")).getText(),"Sign in");
            System.out.println("Test2 pass");
            // Add your assertion code here
        }

        // Test Case 3: Add multiple items and then remove them from cart
        public static void addAndRemoveItemsFromCart(WebDriver driver) throws InterruptedException {
            //move to search page
            driver.get("https://www.amazon.in/");
            getListItem(driver);


            // Add multiple items to the cart
            if(allSearchItemList.size()>=numberOfItemAddToCart){
                System.out.println("test3 phase 1");
                System.out.println(allSearchItemList.size());
                for(int ele= 1; ele<numberOfItemAddToCart+1;ele++){
                    driver.get(allSearchItemList.get(ele));
                    Thread.sleep(2000);
                    addItemToCart(driver);
                    driver.navigate().back();

                }
            }
            Thread.sleep(4000);

//          Move to cart
            driver.findElement(By.id("nav-cart-count")).click();

            //Get Total Cart count
            int cartTotalCount = Integer.parseInt( driver.findElement(By.id("nav-cart-count")).getText());
            System.out.println("cart total count"+cartTotalCount);

            //Remove only added Items
            WebElement ActiveItemContainer = driver.findElement(By.xpath("//div[@data-name='Active Items']"));
            List<WebElement> itemRemaining = ActiveItemContainer.findElements(By.xpath("//div[@data-bundleitem='absent']"));

            int totalItem = itemRemaining.size() ;
            System.out.println("total ite,--->"+totalItem);
            while(totalItem != 0){
                ActiveItemContainer = driver.findElement(By.xpath("//div[@data-name='Active Items']"));
                itemRemaining = ActiveItemContainer.findElements(By.xpath("//div[@data-bundleitem='absent']"));
                if(!itemRemaining.isEmpty()){
                    WebElement item = itemRemaining.get(0);
                    String quantity  = item.findElement(By.xpath("//span[@id='a-autoid-0-announce']")).getText();
                    driver.findElement(By.xpath("//input[@value='Delete']")).click();
                    driver.navigate().refresh();
                    String cartCount  = driver.findElement(By.id("nav-cart-count")).getText();
                    Assert.assertEquals(Integer.parseInt(cartCount),cartTotalCount - Integer.parseInt( quantity.split(":")[1]));
                    cartTotalCount = cartTotalCount - Integer.parseInt( quantity.split(":")[1]);

                }
                totalItem--;
            }

            // Assert that the cart is empty
            // Add your assertion code here
        }

        public static String getFirstItemFromList(WebDriver driver) {
            WebElement mainListClass = driver.findElement(By.xpath("//span[@data-component-type='s-search-results']"));
            List<WebElement> listOfAllItems = mainListClass.findElements(By.xpath("//div[@data-component-type='s-search-result']"));
            for(WebElement val : listOfAllItems){
                String href = val.findElement(By.tagName("a")).getAttribute("href");
                allSearchItemList.add(href);
            }
            return  allSearchItemList.get(0);
        }

        public static void getListItem(WebDriver driver){
            driver.findElement(By.id("twotabsearchtextbox")).click();
            driver.findElement(By.id("twotabsearchtextbox")).sendKeys(productName);
            driver.findElement(By.id("twotabsearchtextbox")).sendKeys(Keys.ENTER);
        }

        public static void addItemToCart( WebDriver driver) throws InterruptedException {
            driver.findElement(By.xpath("(//input[@name='submit.add-to-cart'])")).click();
            driver.findElement(By.id("attach-close_sideSheet-link")).click();
            Thread.sleep(2000);
        }


}
