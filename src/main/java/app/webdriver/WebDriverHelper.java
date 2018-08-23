package app.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;

public class WebDriverHelper {

    private static HashMap<String, WebDriver> instances;

    private static ChromeOptions setDriverOptions(){
        ChromeOptions options;

        options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("test-type");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("no-sandbox");
        options.addArguments("--headless");//hide browser
        //windows
        //options.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");

        options.setBinary("/usr/bin/google-chrome");

        //load from config file
        //System.setProperty("webdriver.chrome.driver", "C:\\wcc2gtax\\assets\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");

        return options;
    }

    private static WebDriver setNewWebDriver(String key){
        WebDriver driver;

        if(instances == null){
            instances = new HashMap<>();
        }

        if(!instances.containsKey(key)){
            driver = new ChromeDriver(setDriverOptions());

            instances.put(key, driver);
        }

        return instances.get(key);
    }

    public static WebDriver getWebDriver(String key){
        WebDriver driver;

        if(instances == null || !instances.containsKey(key)){
            driver = setNewWebDriver(key);
        }else{
            driver = instances.get(key);
        }

        return driver;
    }
}
