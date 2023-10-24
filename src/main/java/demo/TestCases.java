package demo;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.UUID;  
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TestCases {
    ChromeDriver driver;

    public TestCases() {
        System.out.println("Constructor: TestCases");
        WebDriverManager.chromedriver().timeout(30).setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    }

    public void endTest() {
        System.out.println("End Test: TestCases");
        driver.close();
        driver.quit();

    }

    public void testCase01() throws InterruptedException, IOException {
        // Navigate to
        // https://www.w3schools.com/jsref/tryit.asp?filename=tryjsref_win_open
        driver.navigate().to("https://www.w3schools.com/jsref/tryit.asp?filename=tryjsref_win_open");

        // Swithc to Frame Using Locator "ID" iframeResult
        driver.switchTo().frame("iframeResult");

        // Click on the "Try it" button at the top of the page. Using Locator "XPath"
        // //button[text()='Try it']
        WebElement TryBtn = driver.findElement(By.xpath("//button[text()='Try it']"));
        TryBtn.click();
        Thread.sleep(2000);

        String parent = driver.getWindowHandle();
        Set<String> s = driver.getWindowHandles();

        // Now iterate using Iterator
        Iterator<String> I1 = s.iterator();

        while (I1.hasNext()) {
            String child_window = I1.next();

            if (!parent.equals(child_window)) {
                driver.switchTo().window(child_window);

                System.out.println("\nCurrent Tab Title is " + driver.switchTo().window(child_window).getTitle());
                System.out.println("\nCurrent URL is" + driver.switchTo().window(child_window).getCurrentUrl());
                // take screenshot
                try {
                    //Create direcotry
                    File theDir = new File("/screenshots");
                    if (!theDir.exists()) {
                        theDir.mkdirs();
                    }

                    // Generates random UUID for file name   
                    UUID uuid=UUID.randomUUID();                       
                    String fileName = String.format("screenshot_%s.png", uuid);
                    //take Screenshot
                    TakesScreenshot scrShot = ((TakesScreenshot) driver);
                    File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
                    File DestFile = new File("screenshots\\" + fileName);
                    FileUtils.copyFile(SrcFile, DestFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                driver.close();
            }
        }

        // Switch back to the original window by using the window handle.
        driver.switchTo().window(parent);

    }

}
