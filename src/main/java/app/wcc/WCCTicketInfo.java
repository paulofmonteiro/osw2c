package app.wcc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class WCCTicketInfo {

    WebDriver WD;
    WCCTicket ticket;

    WebElement webElement;
    List<WebElement> listOfElements;

    String baseUrl;

    String ticketNumber;

    public WCCTicketInfo(WebDriver webdriver, String ticketNumber, String url){
        this.WD = webdriver;
        this.ticket = new WCCTicket();
        this.baseUrl = url;
        this.ticketNumber = ticketNumber;
    }

    private Boolean openTicketPage(String ticketNumber){
        String url = this.baseUrl + ticketNumber;

        this.WD.get(url);

        if(this.WD.getCurrentUrl().contentEquals(url)){
            return true;
        }else{
            return false;
        }
    }

    private Boolean OpenGeneralPage(String ticketNumber){
        String url = "https://secure.softcomex.com.br/pls/callcenter/tela_ctrl_chamado?cod_ativ=" + ticketNumber;

        this.WD.get(url);

        if(this.WD.getCurrentUrl().contentEquals(url)){
            return true;
        }else{
            return false;
        }
    }

    private Boolean getAllTextFields(){
        this.listOfElements = this.WD.findElements(By.className("texto5"));

        if(this.listOfElements == null){
            return false;
        }else{
            return true;
        }
    }

    public Boolean getAllTicketPageInfo(){
        Boolean status;

        if(this.openTicketPage(this.ticketNumber)){
            if(this.getAllTextFields()){

                this.ticket.setTicketNumber(this.ticketNumber);
                this.ticket.setJiraReference(this.getJiraRefence());
                this.ticket.setProject(this.getProject());
                this.ticket.setClientName(this.getClientName());
                this.ticket.setTicketTitle(this.getTicketTitle());
                this.ticket.setTicketDetails(this.getTicketDetails());
                this.ticket.setTicketStatus(this.getTicketStatus());
                this.ticket.setTicketType(this.getTicketType());
                this.ticket.setResponsableGroup(this.getResponsableGroup());
                this.ticket.setSupportAnalyst(this.getSupportAnalyst());
                this.ticket.setTicketLvl(this.getTicketLvl());

                status = true;
            }else{
                status = false;
            }
        }else{
            status = false;
        }

        if(this.OpenGeneralPage(this.ticketNumber)){
            if(this.getAllTextFields()){
                this.ticket.setUserName(this.getUserName());
                this.ticket.setUserEmail(this.getUserMail());

                status = true;
            }else{
                status = false;
            }
        }else{
            status = false;
        }

        if(status){
            if(this.ticket.store()){
                status = true;
            }else{
                status = false;
            }
        }else{
            status = false;
        }

        return status;
    }

    public void setTicketNumber(String ticketNumber){
        this.ticketNumber = ticketNumber;
    }

    private String getJiraRefence(){
        this.webElement = this.listOfElements.get(1);

        return webElement.getText();
    }

    private String getProject(){
        String[] rawText;
        String text;

        text = "";

        this.webElement = this.listOfElements.get(3);
        rawText = webElement.getText().split(" - ");

        for(int i = 0; i < rawText.length - 1; i++){
            text = text + rawText[i];
        }

        return text;
    }

    private String getClientName(){
        String[] rawText;

        this.webElement = this.listOfElements.get(3);
        rawText = webElement.getText().split(" - ");

        return rawText[rawText.length - 1];
    }

    private String getTicketTitle(){
        this.webElement = this.listOfElements.get(5);

        return this.webElement.getText();
    }

    private String getTicketDetails(){
        this.webElement = this.listOfElements.get(7);

        return this.webElement.getText();
    }

    private String getTicketStatus(){
        this.webElement = this.listOfElements.get(13);

        return this.webElement.getText();
    }

    private String getTicketType(){
        Select selectElement;

        this.webElement = this.WD.findElement(By.id("id_tipo_chamado"));

        selectElement = new Select(this.webElement);

        return selectElement.getFirstSelectedOption().getText();
    }

    private String getResponsableGroup(){
        Select selectElement;

        this.webElement = this.WD.findElement(By.id("id_grupo"));

        selectElement = new Select(this.webElement);

        return selectElement.getFirstSelectedOption().getText();
    }

    private String getSupportAnalyst(){
        Select selectElement;

        this.webElement = this.WD.findElement(By.id("p_anal_suporte"));

        selectElement = new Select(this.webElement);

        return selectElement.getFirstSelectedOption().getText();
    }

    private String getTicketLvl(){
        Select selectElement;

        this.webElement = this.WD.findElement(By.id("id_prioridade"));

        selectElement = new Select(this.webElement);

        return selectElement.getFirstSelectedOption().getText();
    }

    private String getUserName(){
        this.webElement = this.listOfElements.get(3);

        return this.webElement.getText();
    }

    private String getUserMail(){
        WebElement link;
        String email;
        String[] hrefText;

        this.webElement = this.listOfElements.get(3);
        link = this.webElement.findElement(By.tagName("a"));

        email = link.getAttribute("href");
        hrefText = email.split(":");

        email = hrefText[1];

        return email;
    }
}
