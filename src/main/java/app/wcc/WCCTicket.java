package app.wcc;

import app.utils.ConfigHelper;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WCCTicket {

    private String ticketNumber;
    private String jiraReference;
    private String project;
    private String clientName;
    private String ticketTitle;
    private String userName;
    private String userEmail;
    private String ticketDetails;
    private String ticketStatus;
    private String ticketType;
    private String responsableGroup;
    private String ticketLvl;
    private String supportAnalyst;

    public WCCTicket(){}

    public WCCTicket(String ticketNumber, String jiraReference, String project, String clientName, String ticketTitle, String userName, String userEmail, String ticketDetails, String ticketStatus, String ticketType, String responsableGroup, String ticketLvl, String supportAnalyst) {
        this.ticketNumber = ticketNumber;
        this.jiraReference = jiraReference;
        this.project = project;
        this.clientName = clientName;
        this.ticketTitle = ticketTitle;
        this.userName = userName;
        this.userEmail = userEmail;
        this.ticketDetails = ticketDetails;
        this.ticketStatus = ticketStatus;
        this.ticketType = ticketType;
        this.responsableGroup = responsableGroup;
        this.ticketLvl = ticketLvl;
        this.supportAnalyst = supportAnalyst;
    }

    public Boolean store(){
        FileWriter file;
        File folder;
        JsonObject json;
        String ticketFolder;

        if(this.ticketNumber != null){
            ticketFolder = ConfigHelper.getConfig("general","ticketsFolder") + this.ticketNumber + "\\";
            json = this.buildJson();

            folder = new File(ticketFolder);

            if(! folder.exists()){
                folder.mkdir();
            }

            System.out.println(ticketFolder);

            try {
                file = new FileWriter(ticketFolder  + this.ticketNumber + ".json");
                file.write(json.toString());
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(WCCTicket.class.getName()).log(Level.SEVERE, null, ex);
            }

            return true;
        }else{
            return false;
        }
    }

    private JsonObject buildJson(){
        JsonObject ticket;

        ticket = Json.createObjectBuilder()
                .add("ticketNumber", this.ticketNumber)
                .add("jiraReference", this.jiraReference)
                .add("project", this.project)
                .add("clientName", this.clientName)
                .add("ticketTitle", this.ticketTitle)
                .add("userName", this.userName)
                .add("userEmail", this.userEmail)
                .add("ticketDetails", this.ticketDetails)
                .add("ticketStatus", this.ticketStatus)
                .add("ticketType", this.ticketType)
                .add("responsableGroup", this.responsableGroup)
                .add("ticketLvl", this.ticketLvl)
                .add("supportAnalyst", this.supportAnalyst)
                .build();

        return ticket;
    }


    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getJiraReference() {
        return jiraReference;
    }

    public void setJiraReference(String jiraReference) {
        this.jiraReference = jiraReference;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTicketDetails() {
        return ticketDetails;
    }

    public void setTicketDetails(String ticketDetails) {
        this.ticketDetails = ticketDetails;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getResponsableGroup() {
        return responsableGroup;
    }

    public void setResponsableGroup(String responsableGroup) {
        this.responsableGroup = responsableGroup;
    }

    public String getTicketLvl() {
        return ticketLvl;
    }

    public void setTicketLvl(String ticketLvl) {
        this.ticketLvl = ticketLvl;
    }

    public String getSupportAnalyst() {
        return supportAnalyst;
    }

    public void setSupportAnalyst(String supportAnalyst) {
        this.supportAnalyst = supportAnalyst;
    }

}
