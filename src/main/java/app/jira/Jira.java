package app.jira;

import app.utils.ConfigHelper;
import app.utils.PagesHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Jira {
    private WebDriver WD;
    private Boolean testEnvironment;

    public Jira(WebDriver webdriver){
        this.WD = webdriver;

    }

    public Boolean login(){
        String username;
        String password;
        String url;

        url = ConfigHelper.getConfig("jira", "url");
        System.out.println(url);
        WD.get(url);

        if(PagesHelper.waitForPageLoaded(WD)){
            if(this.WD.getCurrentUrl().contains("https://safe.thomson.com")){
                if(new SafeLogin(this.WD).login(url)){

                    return true;
                }

                return true;
            }
        }

        return false;
    }

    private Boolean doLogin(){
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

        /*usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        submitBnt.submit();*/

        return true;
    }

    public Boolean insertGTAXnumber(String jiraReference, String GTAXNumber){
        WebElement element;
        WebElement txtField;
        WebElement btnForm;

        this.WD.get(ConfigHelper.getConfig("jira", "ticketUrl") + jiraReference);

        if(PagesHelper.waitForPageLoaded(this.WD)){
            element = (new WebDriverWait(this.WD, 30).until(
                    ExpectedConditions.presenceOfElementLocated((By.id("customfield_10300-val"))
            )));

            element.click();

            txtField = (new WebDriverWait(this.WD, 30).until(
                    ExpectedConditions.presenceOfElementLocated((By.id("customfield_10300"))
                    )));

            txtField.sendKeys(GTAXNumber);


            btnForm = (new WebDriverWait(this.WD, 30).until(
                    ExpectedConditions.presenceOfElementLocated((By.cssSelector("#customfield_10300-form button[type='submit'"))
                    )));

            btnForm.submit();

            try {
                Thread.sleep(3000);
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

}
