package app.wcc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.HashMap;
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
                this.ticket.setProject(this.toForProject(this.getProject()));
                this.ticket.setClientName(this.getClientName());
                this.ticket.setTicketTitle(this.getTicketTitle());
                this.ticket.setTicketDetails(this.getTicketDetails());
                this.ticket.setTicketStatus(this.toForStatus(this.getTicketStatus()));
                this.ticket.setInquiry(this.toForTicketType(this.getTicketType()));
                this.ticket.setCallType(this.toForCallType(this.getTicketType()));
                this.ticket.setResponsableGroup(this.getResponsableGroup());
                this.ticket.setSupportAnalyst(this.getSupportAnalyst());
                this.ticket.setTicketLvl(this.toForPriority(this.getTicketLvl()));
                this.ticket.setStage(this.toForStage(this.getTicketStatus(), this.toForStatus(this.getTicketStatus())));

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

    private String getProject() {
        String[] rawText;
        String text;

        text = "";

        this.webElement = this.listOfElements.get(3);

        if (webElement.getText().contains("CAMBIO")) {
            if (webElement.getText().contains("IMPORTACAÇÃO")) {
                text = "Cambio Imp";
            } else {
                text = "Cambio Exp";
            }
        }else if(webElement.getText().contains("DRAWBACK")){
            text = "Special Programs - Drawback";
        }else if(webElement.getText().contains("EV")){
            text = "Special Programs - EV";
        }else if(webElement.getText().contains("RECOF")){
            text = "Special Programs - RECOF";
        }else if(webElement.getText().contains("REPLAT")){
            text = "Special Programs - REPLAT";
        }else if(webElement.getText().contains("REPETRO")){
            text = "Special Programs - REPETRO";
        }else{
            rawText = webElement.getText().split(" - ");

            for(int i = 0; i < rawText.length - 1; i++){
                if(rawText[i].contains("SYS")){
                    rawText[i] = rawText[i].substring(0, rawText[i].indexOf("SYS"));
                }

                if(rawText[i].equals("INTEGRAÇÃO")){
                    text = text + "Integration";
                }else if(rawText[i].equals("SAP")){
                    text = text + " - SAP";
                }else if(rawText[i].equals("TXT")){
                    text = text + " - Legacy";
                }else{
                    text = text + rawText[i];
                }

            }
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

    private String toForProject(String project){
        List<String> projects = new ArrayList<>();
        String foundProjet = null;

        projects.add("Broker");
        projects.add("Broker Sisco");
        projects.add("Broker Siscomex WEB");
        projects.add("Cambio Exp");
        projects.add("Cambio Imp");
        projects.add("Classifier");
        projects.add("ComexContent");
        projects.add("ComexData");
        projects.add("ComexDataQA");
        projects.add("Easy Account Control");
        projects.add("Easy Drawback Control");
        projects.add("Easy Export Control");
        projects.add("Easy Financing Control");
        projects.add("Easy Import Control");
        projects.add("Easy Siscoserv Control");
        projects.add("Export");
        projects.add("Export Sisco");
        projects.add("Export SMB");
        projects.add("Extrator");
        projects.add("FTA");
        projects.add("FTM Export SMB");
        projects.add("FTM Import SMB");
        projects.add("Import");
        projects.add("Import SMB");
        projects.add("Infrastructure");
        projects.add("INOUT");
        projects.add("Integration - Legacy");
        projects.add("Integration - Oracle BPEL");
        projects.add("Integration - Oracle DBLink");
        projects.add("Integration - SAP");
        projects.add("Integration - SAP PI");
        projects.add("Masterdata");
        projects.add("NOVOEX");
        projects.add("Platform");
        projects.add("RPS");
        projects.add("Siscoserv");
        projects.add("Special Programs");
        projects.add("Special Programs - BW");
        projects.add("Special Programs - DE");
        projects.add("Special Programs - Drawback");
        projects.add("Special Programs - EV");
        projects.add("Special Programs - IMMEX");
        projects.add("Special Programs - RAF");
        projects.add("Special Programs - Recof");
        projects.add("Special Programs - RegInt");
        projects.add("Special Programs - Repetro");
        projects.add("Special Programs - Replat");
        projects.add("Special Programs - ZPE");

        if(projects.contains(project)){
           for(int i = 0; i < projects.size(); i++){
               if(projects.get(i).equals(project)){
                   foundProjet = projects.get(i);
               }
           }
        }

        return foundProjet;
    }

    private String toForTicketType(String type){
        String inquiry = null;
        HashMap<String, String> type2Inquiry = new HashMap<>();

        type2Inquiry.put("Problema", "Technical Support");
        type2Inquiry.put("Problema/RM", "Technical Support");
        type2Inquiry.put("Problema/HF", "Technical Support");
        type2Inquiry.put("Dúvida", "Product Guidance");
        type2Inquiry.put("Consultoria Online", "Direct Support");
        type2Inquiry.put("Suporte Hosting", "Cloud Request");
        type2Inquiry.put("Suporte Hosting", "Cloud Request");
        type2Inquiry.put("Dúvida Custom", "Customization Support");
        type2Inquiry.put("Problema Custom", "Customization Support");
        type2Inquiry.put("Customização", "Services Request");
        type2Inquiry.put("Consultoria", "Services Request");
        type2Inquiry.put("Sugestão de Melhoria", "Provide Feedback");

        if(type2Inquiry.containsKey(type)){
            inquiry = type2Inquiry.get(type);
        }

        return inquiry;
    }

    private String toForCallType(String type){
        String callType = null;
        HashMap<String, String> type2CallType = new HashMap<>();

        type2CallType.put("Problema", "Technical Support - Error or Error Message");
        type2CallType.put("Problema/RM", "Technical Support - Error or Error Message");
        type2CallType.put("Problema/HF", "Technical Support - Software Access - Outage");
        type2CallType.put("Dúvida", "None");
        type2CallType.put("Consultoria Online", "None");
        type2CallType.put("Suporte Hosting", "Cloud Request - Error or Error Message");
        type2CallType.put("Suporte Hosting", "Cloud Request - Change Request");
        type2CallType.put("Dúvida Custom", "Customization Support - General Support");
        type2CallType.put("Problema Custom", "Customization Support - Error");
        type2CallType.put("Customização", "Service Request - Customization");
        type2CallType.put("Consultoria", "Service Request - Consulting");
        type2CallType.put("Sugestão de Melhoria", "None");

        if(type2CallType.containsKey(type)){
            callType = type2CallType.get(type);
        }

        return callType;
    }

    private String toForPriority(String ticketLvl){
        String priority = null;
        HashMap<String, String> type2Priority = new HashMap<>();

        type2Priority.put("Baixa", "Low");
        type2Priority.put("Média", "Medium");
        type2Priority.put("Alta", "High");
        type2Priority.put("Operação Parada", "Critical");


        if(type2Priority.containsKey(ticketLvl)){
            priority = type2Priority.get(ticketLvl);
        }

        return priority;
    }

    private String toForStatus(String ticketStatus){
        String status = null;
        HashMap<String, String> type2status = new HashMap<>();

        type2status.put("Aguardando Triagem", "New");
        type2status.put("Aguardando Suporte", "Open");
        type2status.put("Em Suporte", "Open");
        type2status.put("Pendencia do Requisitante", "Open");
        type2status.put("Encerrado", "Closed");

        if(type2status.containsKey(ticketStatus)){
            status = type2status.get(ticketStatus);
        }else{
            status = "Closed";
        }

        return status;
    }

    private String toForStage(String ticketStatus, String gtaxStatus){
        String stage;

        if(ticketStatus.equals("Aguardando Triagem") && gtaxStatus.equals("New")){
            stage = "none";
        }else if(ticketStatus.equals("Aguardando Suporte") && gtaxStatus.equals("New")){
            stage = "none";
        }else if(ticketStatus.equals("Em Suporte") && gtaxStatus.equals("Open")){
            stage = "In Progress";
        }else if(ticketStatus.equals("Pendencia do Requisitante") && gtaxStatus.equals("Open")){
            stage = "Waiting for Customer Reply";
        }else{
            stage = "Closed";
        }

        return stage;
    }

}
