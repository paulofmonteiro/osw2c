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
    private WebElement formElement;
    private String frameID;

    public GTAXNewTicket(WebDriver webdriver, String ticketNumber, Boolean testEnviroment){
        this.WD = webdriver;
        TicketHelper.loadConfigFile(ticketNumber);

        if(this.openNewTicketTab()){
            System.out.println("ok");
            formElement = this.WD.findElement(By.id("editPage"));

            setUserName(ticketNumber, testEnviroment);
            setBrand(formElement);
            setProductTitle(formElement);
            setModule(formElement, ticketNumber);
            setInquiry(formElement, ticketNumber);
            setCallType(formElement, ticketNumber);
            setCountry(formElement, ticketNumber);
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
                this.frameID = newCaseFrames.get(i).getAttribute("id");
                this.WD.switchTo().frame(this.frameID);

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

    private void setUserName(String ticketNumber, Boolean testEnviroment){
        WebElement textField;
        textField = this.WD.findElement(By.id("cas3"));

        if(testEnviroment){
            textField.sendKeys("Luiz Andriolo");
        }else{
            textField.sendKeys(TicketHelper.getConfig(ticketNumber,"userName"));
        }



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

    private void setCountry(WebElement formElement, String ticket){
        Select selectElement;
        WebElement element;

        element = formElement.findElement(By.id("00N1400000At5M4"));
        System.out.println(element);
        selectElement = new Select(element);

       if(ConfigHelper.getConfig("general", "country") != null && !ConfigHelper.getConfig("general", "country").isEmpty()) {
            selectElement.selectByVisibleText(ConfigHelper.getConfig("general", "country"));
        }else{
            selectElement.selectByValue("Brazil");
        }

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

        if(ConfigHelper.getConfig("general", "status") != null && !ConfigHelper.getConfig("general", "status").isEmpty()) {
            selectElement.selectByValue(ConfigHelper.getConfig("general", "status"));
        }else{
            for(int i = 0; i < options.size(); i++) {
                if(options.get(i).getText().equals(TicketHelper.getConfig(ticket, "ticketStatus"))){
                    selectElement.selectByValue(options.get(i).getText());
                }
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

        if(ConfigHelper.getConfig("general", "stage") != null && !ConfigHelper.getConfig("general", "stage").equals("--None--")){
            selectElement.selectByValue(ConfigHelper.getConfig("general", "stage"));
        }else if(!TicketHelper.getConfig(ticket, "stage").equals("--None--")){
            for(int i = 0; i < options.size(); i++) {
                if(options.get(i).getText().equals(TicketHelper.getConfig(ticket, "stage"))){
                    selectElement.selectByValue(options.get(i).getText());
                }
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

    public String getNewTicketNumber(){
        String ticketNumber;

        WD.switchTo().defaultContent();

        if(PagesHelper.waitForPageLoaded(this.WD)) {
            try {
                WebElement newTab = this.WD.findElement(By.cssSelector("ul.x-tab-strip.x-tab-strip-top > li.x-tab-strip-closable span.tabText"));
                ticketNumber = newTab.getText();
            }catch(Exception e){
                ticketNumber = null;
            }
        }else{
            ticketNumber = null;
        }

        if(ticketNumber.equals("New Case")){
            ticketNumber = getValidationError();
        }

        return ticketNumber;
    }

    private String getValidationError(){
        WebElement errorWrap;
        String errorMsg = null;

        this.WD.switchTo().frame(this.frameID);

        if(PagesHelper.waitForPageLoaded(this.WD)) {
            try {
                errorWrap = formElement.findElement(By.className("errorMsg"));

                errorMsg = errorWrap.getText();
            } catch (Exception e) {
                errorMsg = "NOK";
            }
        }

        return errorMsg;
    }
}
