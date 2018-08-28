package app.gtax;

import app.utils.ConfigHelper;
import app.utils.PagesHelper;
import javafx.concurrent.Task;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.json.JsonObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                closeAllTicketFrames();

                return true;
            }
        }else{
            if(doLogin(ConfigHelper.getConfig("gtax", "username"), ConfigHelper.getConfig("gtax", "password"))){
                closeAllTicketFrames();
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

    public void closeAllTicketFrames(){
        WD.switchTo().defaultContent();

        if(PagesHelper.waitForPageLoaded(this.WD)){
            List<WebElement> newTabs = this.WD.findElements(By.cssSelector("ul.x-tab-strip.x-tab-strip-top > li.x-tab-strip-closable > a.x-tab-strip-close"));
            System.out.println(newTabs.size());

            //itera sobre a abas de um novo chamado
            for(int i = 0; i < newTabs.size(); i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GTAXLogin.class.getName()).log(Level.SEVERE, null, ex);
                }

                WebElement newCaseTab = newTabs.get(i);

                System.out.println("tab: " + i);
                //muda para a aba
                JavascriptExecutor executor = (JavascriptExecutor)this.WD;
                executor.executeScript("arguments[0].click();", newCaseTab);

                try{
                    List<WebElement> buttons = this.WD.findElements(By.cssSelector("div.x-window.darkBackground.x-resizable-pinned div.x-window-footer button"));

                    for(int j = 0; j < buttons.size(); j++){
                        if(buttons.get(j).getText().contains("Don't Save")){
                            buttons.get(j).click();
                        }
                    }

                }catch(Exception e){
                    System.out.println("Erro when trying to cancel a new ticket tab");
                }
            }
        }
    }
}
