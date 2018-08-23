package app.wcc;

import app.utils.ConfigHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.json.JsonObject;
import java.util.List;

public class WCCLogin {
    private WebDriver WD;

    public WCCLogin(WebDriver webdriver){
        this.WD =  webdriver;
    }

    public Boolean login(String url) {
        String username;
        String password;

        username = ConfigHelper.getConfig("wcc","username");
        password =  ConfigHelper.getConfig("wcc","password");
        this.WD.get(url);

        if(this.WD.getCurrentUrl().contains("onepass")){
            if(this.openSSOpage()){
                System.out.println("SSO page opened!");

                if(this.loginSSO(username, password)){
                    return true;
                    //new WCCTicketInfo(this.WD, "571701", this.config.getString("ticketUrl"));
                    //new WCCTicketFiles(this.WD, "571701", this.config.getString("filesUrl"));
                }else{
                    System.out.println("error WCC page cannot be open");
                    return false;
                }
            }else{
                System.out.println("error on try to open SSO");
                return false;
            }
        }else if(this.WD.getCurrentUrl().contains("callcenter/menu")){
            return true;
        }else{
            return false;
        }
    }

    private Boolean openSSOpage(){
        List<WebElement> linksList;
        WebElement linkSSO;

        linkSSO = null;
        linksList = this.WD.findElements(By.tagName("a"));

        for(int i = 0; i < linksList.size(); i++){

            System.out.println(linksList.get(i).getAttribute("href"));

            if(linksList.get(i).getAttribute("href").contains("signon")){
                linkSSO = linksList.get(i);
            }
        }

        if(linkSSO != null){
            linkSSO.click();

            if(this.WD.getCurrentUrl().contains("https://signon.thomsonreuters.com")){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private Boolean loginSSO(String username, String password){
        WebElement usernameField;
        WebElement passwordField;
        WebElement submitBtn;

        usernameField = (new WebDriverWait(this.WD, 10).until(
                ExpectedConditions.presenceOfElementLocated(By.id("Username"))
        ));

        passwordField = (new WebDriverWait(this.WD, 10).until(
                ExpectedConditions.presenceOfElementLocated(By.id("Password"))
        ));

        submitBtn = (new WebDriverWait(this.WD, 10).until(
                ExpectedConditions.elementToBeClickable(By.id("SignIn"))
        ));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        submitBtn.submit();

        System.out.println(ExpectedConditions.urlContains("callcenter/menu").toString());

        if(this.WD.getCurrentUrl().contains("callcenter/menu")){
            return true;
        }else{
            return false;
        }
    }
}
