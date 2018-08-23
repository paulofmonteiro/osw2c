package app.gtax;

import app.utils.ConfigHelper;
import app.utils.PagesHelper;
import app.utils.TicketHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class GTAXNewTicket {

    private WebDriver WD;

    public GTAXNewTicket(WebDriver webdriver, String ticketNumber){
        this.WD = webdriver;
        TicketHelper.loadConfigFile(ticketNumber);

        if(this.openNewTicketTab()){
            System.out.println("ok");
            WebElement formElement = this.WD.findElement(By.id("editPage"));

            setUserName();
            setBrand(formElement);
            setProductTitle(formElement);
            setModule(formElement, ticketNumber);
            setInquiry(formElement, ticketNumber);
            setCallType(formElement, ticketNumber);
            setCountry(formElement);
            setCaseOrigin(formElement);
            setDescription(formElement, ticketNumber);
            setPriority(formElement, ticketNumber);
            setJiraRefence(formElement, ticketNumber);
            setSubject(formElement, ticketNumber);
            setStatus(formElement, ticketNumber);
            setStage(formElement, ticketNumber);

            if(saveTicket(formElement)){
                getNewTicketNumber();
            };

        }else{
            System.out.println("nok");
        }
    }

    private Boolean openNewTicketTab(){
        WebElement caseTab;
        WebElement newCaseBtn;
        WebElement newCaseTab;
        List<WebElement> newCaseFrames;

        Boolean tryAgain;

        tryAgain = true;

        newCaseTab = null;
        newCaseFrames = null;

        caseTab = this.WD.findElement(By.id("ext-comp-1006"));
        caseTab.click();

        this.WD.switchTo().frame("ext-comp-1005");

        newCaseBtn = this.WD.findElement(By.name("new_case"));
        newCaseBtn.click();

        this.WD.switchTo().defaultContent();

        if(PagesHelper.waitForPageLoaded(this.WD)){

            while(tryAgain){
                try{
                    Thread.sleep(1000);

                    newCaseTab = this.WD.findElement(By.cssSelector("ul.x-tab-strip.x-tab-strip-top > li.x-tab-strip-closable"));
                    JavascriptExecutor executor = (JavascriptExecutor)this.WD;
                    executor.executeScript("arguments[0].click();", newCaseTab);

                    tryAgain = false;
                }catch(Exception e){
                    System.out.println("tab nÃo selecionada");
                }
            }

            tryAgain = true;

            while(tryAgain){
                try{
                    newCaseFrames = this.WD.findElements(By.cssSelector("div > iframe.x-border-panel"));

                    tryAgain = false;
                }catch(Exception e){
                    System.out.println("frame nÃo encontrado");
                }
            }

            System.out.println(newCaseFrames.size());

            for(int i = 0; i < newCaseFrames.size(); i++){
                System.out.println(newCaseFrames.get(i).getAttribute("id"));
                System.out.println(newCaseFrames.get(i).getAttribute("name"));

                this.WD.switchTo().frame(newCaseFrames.get(i).getAttribute("id"));

                try{
                    WebElement formElement = this.WD.findElement(By.id("editPage"));

                    return true;
                }catch(Exception e){
                    return false;
                }
            }
        }
        return false;
    }

    private void setUserName(){
        WebElement textField;
        textField = this.WD.findElement(By.id("cas3"));
        textField.sendKeys("Luiz Andriolo");
    }

    private void setHiddenField(){
        WebElement textField;
        JavascriptExecutor executor = (JavascriptExecutor)this.WD;
        executor.executeScript("document.querySelector(#cas3_lkid').value='0031O000031Cx3h'");
        textField = this.WD.findElement(By.id("cas3_lkid"));
        textField.sendKeys("0031O000031Cx3h");

        textField = this.WD.findElement(By.id("cas3_lkold"));
        textField.sendKeys("Luiz Andriolo");
        executor.executeScript("document.getElementById('cas3_lkold').value='Luiz Andriolo'");
        textField = this.WD.findElement(By.id("cas3_mod"));
        textField.sendKeys("1");
        executor.executeScript("document.getElementById('cas3_mod').value='1'");
    }

    private void setBrand(WebElement formElement){
        Select selectElement;
        WebElement element;

        element = formElement.findElement(By.id("00Nw0000008RA0b"));

        System.out.println(element);
        selectElement = new Select(element);
        selectElement.selectByValue("ONESOURCE");

    }

    private void setProductTitle(WebElement formElement){
        Select selectElement;
        WebElement element;

        element = formElement.findElement(By.id("00Nw0000008RA0o"));
        System.out.println(element);
        selectElement = new Select(element);
        selectElement.selectByValue("Global Trade Management");
    }

    private void setModule(WebElement formElement, String ticket){
        String module;
        Select selectElement;
        List<WebElement> options;
        WebElement element;

        element = formElement.findElement(By.id("00Nw0000008RA0p"));
        selectElement = new Select(element);

        options = selectElement.getOptions();

        for(int i = 0; i < options.size(); i++) {
            if(options.get(i).getText().equals(TicketHelper.getConfig(ticket, "project"))){
                selectElement.selectByValue(options.get(i).getText());
            }
        }
    }

    private void setInquiry(WebElement formElement, String ticket){
        String module;
        Select selectElement;
        List<WebElement> options;
        WebElement element;

        element = formElement.findElement(By.id("00Nw0000008RA0U"));
        selectElement = new Select(element);

        options = selectElement.getOptions();

        for(int i = 0; i < options.size(); i++) {
            if(options.get(i).getText().equals(TicketHelper.getConfig(ticket, "inquiry"))){
                selectElement.selectByValue(options.get(i).getText());
            }
        }
    }

    private void setCallType(WebElement formElement, String ticket){
        String module;
        Select selectElement;
        List<WebElement> options;
        WebElement element;

        element = formElement.findElement(By.id("00Nw0000008RA0E"));
        selectElement = new Select(element);

        options = selectElement.getOptions();

        for(int i = 0; i < options.size(); i++) {
            if(options.get(i).getText().equals(TicketHelper.getConfig(ticket, "callType"))){
                selectElement.selectByValue(options.get(i).getText());
            }
        }
    }

    private void setCountry(WebElement formElement){
        Select selectElement;
        WebElement element;

        element = formElement.findElement(By.id("00N1400000At5M4"));
        System.out.println(element);
        selectElement = new Select(element);
        selectElement.selectByValue("Brazil");
    }

    private void setSubject(WebElement formElement, String ticket){
        WebElement textField;
        textField = this.WD.findElement(By.id("cas14"));
        textField.sendKeys(TicketHelper.getConfig(ticket, "ticketTitle") + " - (WCC " + ticket + ")");
    }

    private void setDescription(WebElement formElement, String ticket){
        WebElement textField;
        textField = this.WD.findElement(By.id("cas15"));
        textField.sendKeys(TicketHelper.getConfig(ticket, "ticketDetails"));
    }

    private void setJiraRefence(WebElement formElement, String ticket){
        WebElement textField;
        textField = this.WD.findElement(By.id("00Nw0000008RA14"));
        textField.sendKeys(TicketHelper.getConfig(ticket, "jiraReference"));
    }

    private void setCaseOrigin(WebElement formElement){
        Select selectElement;
        WebElement element;

        element = formElement.findElement(By.id("cas11"));
        System.out.println(element);
        selectElement = new Select(element);
        selectElement.selectByValue("Other");
    }

    private void setPriority(WebElement formElement, String ticket){
        String module;
        Select selectElement;
        List<WebElement> options;
        WebElement element;

        element = formElement.findElement(By.id("cas8"));
        selectElement = new Select(element);

        options = selectElement.getOptions();

        for(int i = 0; i < options.size(); i++) {
            if(options.get(i).getText().equals(TicketHelper.getConfig(ticket, "ticketLvl"))){
                selectElement.selectByValue(options.get(i).getText());
            }
        }
    }

    private void setStatus(WebElement formElement, String ticket){
        String module;
        Select selectElement;
        List<WebElement> options;
        WebElement element;

        element = formElement.findElement(By.id("cas7"));
        selectElement = new Select(element);

        options = selectElement.getOptions();

        for(int i = 0; i < options.size(); i++) {
            if(options.get(i).getText().equals(TicketHelper.getConfig(ticket, "ticketStatus"))){
                selectElement.selectByValue(options.get(i).getText());
            }
        }
    }

    private void setStage(WebElement formElement, String ticket){

        Select selectElement;
        List<WebElement> options;
        WebElement element;

        element = formElement.findElement(By.id("00Nw0000008RA12"));
        selectElement = new Select(element);

        options = selectElement.getOptions();

        for(int i = 0; i < options.size(); i++) {
            if(options.get(i).getText().equals(TicketHelper.getConfig(ticket, "stage"))){
                selectElement.selectByValue(options.get(i).getText());
            }
        }
    }

    private Boolean saveTicket(WebElement formElement){
        WebElement element;

        element = formElement.findElement(By.id("bottomButtonRow"));
        element = element.findElement(By.name("save"));
        element.click();

        return true;
    }

    private void getNewTicketNumber(){
        WD.switchTo().defaultContent();

        if(PagesHelper.waitForPageLoaded(this.WD)) {
            WebElement newTab = this.WD.findElement(By.cssSelector("ul.x-tab-strip.x-tab-strip-top > li.x-tab-strip-closable span.tabText"));
            System.out.println(newTab.getText());
        }
    }
}
