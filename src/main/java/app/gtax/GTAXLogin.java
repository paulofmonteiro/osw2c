package app.gtax;

import app.utils.ConfigHelper;
import javafx.concurrent.Task;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.json.JsonObject;

public class GTAXLogin {

    private WebDriver WD;
    private Boolean testEnvironment;

    public GTAXLogin(WebDriver webdriver, Boolean testEnvironment){
        this.WD = webdriver;
        this.testEnvironment = testEnvironment;
    }

    public Boolean login(){
        String username;
        String password;
        String url;

        System.out.println(this.testEnvironment);

        if(this.testEnvironment){
            url = ConfigHelper.getConfig("gtax", "urlTest");
        }else{
            url = ConfigHelper.getConfig("gtax", "url");
        }


        WD.get(url);

        if(testEnvironment){
            if(doLogin(ConfigHelper.getConfig("gtax", "usernameTest"), ConfigHelper.getConfig("gtax", "passwordTest"))){
                return true;
            }
        }else{
            if(doLogin(ConfigHelper.getConfig("gtax", "username"), ConfigHelper.getConfig("gtax", "password"))){
                return true;
            }
        }

        return false;
    }

    private Boolean doLogin(String username, String password){
        WebElement usernameField;
        WebElement passwordField;
        WebElement submitBnt;

        usernameField = (new WebDriverWait(this.WD, 30).until(
                ExpectedConditions.presenceOfElementLocated(By.id("username")
                )));

        passwordField = (new WebDriverWait(this.WD, 30).until(
                ExpectedConditions.presenceOfElementLocated(By.id("password")
                )));


        submitBnt = (new WebDriverWait(this.WD, 30).until(
                ExpectedConditions.presenceOfElementLocated(By.id("Login")
                )));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        submitBnt.submit();

        return true;
    }
}
