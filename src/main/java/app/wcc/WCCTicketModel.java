package app.wcc;

public class WCCTicketModel {
    private String WCCTicketNumber;
    private String GTAXTicketNumber;
    private String jiraReference;
    private String project;
    private String clientName;
    private String subject;
    private String contactName;
    private String description;
    private String ticketStatus;
    private String inquiry;
    private String callType;
    private String responsableGroup;
    private String ticketLvl;
    private String supportAnalyst;
    private String stage;
    private String fat;
    private String country;

    public String getWCCTicketNumber() {
        return WCCTicketNumber;
    }

    public void setWCCTicketNumber(String WCCTicketNumber) {
        this.WCCTicketNumber = WCCTicketNumber;
    }

    public String getGTAXTicketNumber() {
        return GTAXTicketNumber;
    }

    public void setGTAXTicketNumber(String GTAXTicketNumber) {
        this.GTAXTicketNumber = GTAXTicketNumber;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        if(contactName == null){
            this.contactName = "";
        }else{
            this.contactName = contactName;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getInquiry() {
        return inquiry;
    }

    public void setInquiry(String inquiry) {
        this.inquiry = inquiry;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
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
        if(supportAnalyst == null){
            this.supportAnalyst = "";
        }else {
            this.supportAnalyst = supportAnalyst;
        }
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
