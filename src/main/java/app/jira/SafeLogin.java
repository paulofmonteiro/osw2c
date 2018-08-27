package app.jira;

import app.utils.ConfigHelper;
import app.utils.PagesHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.json.JsonObject;
import java.util.HashMap;

public class SafeLogin {

    private WebDriver WD;
    private String safeUrl;

    public SafeLogin(WebDriver webdriver){
        this.WD = webdriver;

        this.safeUrl = ConfigHelper.getConfig("safe", "url");
    }

    public Boolean login(String url){
        String username;
        String password;

        WebElement textFieldUserName;
        WebElement textFieldPassword;
        WebElement btnForm;
        WebElement captcha;

        username = ConfigHelper.getConfig("safe", "username");
        password = ConfigHelper.getConfig("safe", "password");
        captcha = null;

        if(PagesHelper.waitForPageLoaded(this.WD)){
            textFieldUserName = (new WebDriverWait(this.WD, 10)).until(
                    ExpectedConditions.presenceOfElementLocated(By.id("USER"))
            );

            textFieldPassword = (new WebDriverWait(this.WD, 10)).until(
                    ExpectedConditions.presenceOfElementLocated(By.id("PASSWORD"))
            );

            btnForm = (new WebDriverWait(this.WD, 10)).until(
                    ExpectedConditions.presenceOfElementLocated(By.id("safeLoginbtn"))
            );

            textFieldUserName.sendKeys(username);
            textFieldPassword.sendKeys(password);
            btnForm.submit();

            try{
                captcha = this.WD.findElement(By.cssSelector("#deliverCaptchaImage.hidden"));
            }catch(Exception e){
                System.out.println("Captcha nÃ£o foi solicitado");
            }

            if(captcha == null){
                if(this.resolveSecurityQuestion()){
                    if(this.verifyIsLoggedSuccess(url)){
                        return true;
                    }
                }else{
                    if(this.verifyIsLoggedSuccess(url)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private WebElement needSecurityQuestion(){
        String currentUrl;

        WebElement questionWrapElement;

        currentUrl = this.WD.getCurrentUrl();
        this.safeUrl = "https://safe.thomson.com/";
        questionWrapElement = null;

        if(currentUrl.contains(this.safeUrl)){
            try{
                questionWrapElement = this.WD.findElement(By.id("question"));
            }catch(Exception e){
                System.out.println("there's no question");
            }
        }

        return questionWrapElement;
    }

    private String getSecurityQuestion(WebElement questionWrapElement){
        WebElement questionElement;
        String question;

        questionElement = questionWrapElement.findElement(By.cssSelector("dd.question"));
        question = questionElement.getText();

        return question;
    }

    private String searchSecurityQuestionAnswer(String question){
        HashMap<String, String> questionAnswerMap = new HashMap<>();
        String answer;

        questionAnswerMap.put("What is your oldest cousin's first and last name?", "Lucas");
        questionAnswerMap.put("In what city or town did your mother and father meet?", "osasco");
        questionAnswerMap.put("In what city did you meet your spouse/significant other?", "piracaia");

        answer = questionAnswerMap.get(question);

        return answer;
    }

    private Boolean resolveSecurityQuestion(){
        WebElement questionWrapElement;
        WebElement questionAnswerField;
        WebElement btnForm;

        String questionAnswer;

        questionWrapElement = null;
        questionWrapElement = this.needSecurityQuestion();

        if(questionWrapElement == null){
            return true;
        }else{
            questionAnswerField = this.WD.findElement(By.id("verify_securityQuestion"));

            btnForm = (new WebDriverWait(this.WD, 10)).until(
                    ExpectedConditions.presenceOfElementLocated(By.id("safeLoginbtn"))
            );

            questionAnswer = searchSecurityQuestionAnswer(this.getSecurityQuestion(questionWrapElement));

            if(!questionAnswer.isEmpty()){
                questionAnswerField.sendKeys(questionAnswer);
                btnForm.submit();
                return true;
            }else{
                System.out.println("answer not found!");

                return false;
            }
        }
    }

    public Boolean verifyIsLoggedSuccess(String url){
        String currentUrl;

        currentUrl = this.WD.getCurrentUrl();

        if(currentUrl.contains(url)){
            return true;
        }else{
            return false;
        }
    }
}