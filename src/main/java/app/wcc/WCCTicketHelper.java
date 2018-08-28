package app.wcc;

import app.datasource.DataSource;
import app.utils.ConfigHelper;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WCCTicketHelper {

    public static Boolean store(WCCTicketModel ticket) {
        FileWriter file;
        File folder;
        JsonObject json;
        String ticketFolder;

        if (ticket.getWCCTicketNumber() != null) {
            ticketFolder = ConfigHelper.getConfig("general", "ticketsFolder") + ticket.getWCCTicketNumber() + "\\";
            json = WCCTicketHelper.buildJson(ticket);

            folder = new File(ticketFolder);

            if (!folder.exists()) {
                folder.mkdir();
            }

            System.out.println(ticketFolder);

            try {
                file = new FileWriter(ticketFolder + ticket.getWCCTicketNumber() + ".json");
                file.write(json.toString());
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(WCCTicket.class.getName()).log(Level.SEVERE, null, ex);
            }

            return true;
        } else {
            return false;
        }
    }

    private static JsonObject buildJson(WCCTicketModel ticket) {
        JsonObject ticketJson;

        ticketJson = Json.createObjectBuilder()
                .add("ticketNumber", ticket.getWCCTicketNumber())
                .add("jiraReference", ticket.getJiraReference())
                .add("project", ticket.getProject())
                .add("clientName", ticket.getClientName())
                .add("ticketTitle", ticket.getSubject())
                .add("userName", ticket.getContactName())
                .add("userEmail", "")
                .add("ticketDetails", ticket.getDescription())
                .add("ticketStatus", ticket.getTicketStatus())
                .add("inquiry", ticket.getInquiry())
                .add("callType", ticket.getCallType())
                .add("responsableGroup", "")
                .add("ticketLvl", ticket.getTicketLvl())
                .add("supportAnalyst", ticket.getSupportAnalyst())
                .add("stage", ticket.getStage())
                .add("country", ticket.getCountry())
                .add("id", ticket.getClientID())
                .add("gtaxNumber", ticket.getGTAXTicketNumber())
                .add("jiraStatus", ticket.getJiraStatus())
                .build();

        return ticketJson;
    }

    public static String toForTicketType(String type) {
        String inquiry = null;
        HashMap<String, String> type2Inquiry = new HashMap<>();

        type2Inquiry.put("Problema", "Technical Support");
        type2Inquiry.put("Problema/RM", "Technical Support");
        type2Inquiry.put("Problema/HF", "Technical Support");
        type2Inquiry.put("Dúvida", "Product Guidance");
        type2Inquiry.put("Consultoria On-line", "Direct Support");
        type2Inquiry.put("Suporte Hosting", "Cloud Request");
        type2Inquiry.put("Suporte Hosting", "Cloud Request");
        type2Inquiry.put("Dúvida Custom", "Customization Support");
        type2Inquiry.put("Problema Custom", "Customization Support");
        type2Inquiry.put("Customização", "Services Request");
        type2Inquiry.put("Consultoria", "Services Request");
        type2Inquiry.put("Sugestão de Melhoria", "Provide Feedback");

        if (type2Inquiry.containsKey(type)) {
            inquiry = type2Inquiry.get(type);
        }

        return inquiry;
    }

    public static String toForCallType(String type) {
        String callType = null;
        HashMap<String, String> type2CallType = new HashMap<>();

        type2CallType.put("Problema", "Technical Support - Error or Error Message");
        type2CallType.put("Problema/RM", "Technical Support - Error or Error Message");
        type2CallType.put("Problema/HF", "Technical Support - Software Access - Outage");
        type2CallType.put("Dúvida", "None");
        type2CallType.put("Consultoria On-line", "None");
        type2CallType.put("Suporte Hosting", "Cloud Request - Error or Error Message");
        type2CallType.put("Suporte Hosting", "Cloud Request - Change Request");
        type2CallType.put("Dúvida Custom", "Customization Support - General Support");
        type2CallType.put("Problema Custom", "Customization Support - Error");
        type2CallType.put("Customização", "Service Request - Customization");
        type2CallType.put("Consultoria", "Service Request - Consulting");
        type2CallType.put("Sugestão de Melhoria", "None");

        if (type2CallType.containsKey(type)) {
            callType = type2CallType.get(type);
        }

        return callType;
    }

    public static String toForStatus(String ticketStatus) {
        String status = null;
        HashMap<String, String> type2status = new HashMap<>();

        type2status.put("Aguardando Triagem", "New");
        type2status.put("Aguardando Suporte", "Open");
        type2status.put("Em Suporte", "Open");
        type2status.put("Pendencia do Requisitante", "Open");
        type2status.put("Encerrado", "Closed");

        if (type2status.containsKey(ticketStatus)) {
            status = type2status.get(ticketStatus);
        } else {
            status = "Open";
        }

        return status;
    }

    public static String toForStage(String ticketStatus, String gtaxStatus) {
        String stage;

        if (ticketStatus.equals("Aguardando Triagem") && gtaxStatus.equals("New")) {
            stage = "--None--";
        } else if (ticketStatus.equals("Aguardando Suporte") && gtaxStatus.equals("New")) {
            stage = "--None--";
        } else if (ticketStatus.equals("Em Suporte") && gtaxStatus.equals("Open")) {
            stage = "In Progress";
        } else if (ticketStatus.equals("Pendencia do Requisitante") && gtaxStatus.equals("Open")) {
            stage = "Waiting for Customer Reply";
        } else {
            stage = "--None--";
        }

        return stage;
    }

    public static String validateUser(DataSource db, WCCTicketModel ticket) {
        Statement queryExecutor;
        ResultSet queryResult;
        String query;
        String userName = null;
        String clientName = null;

        if (ticket.getContactName() == null || ticket.getContactName().isEmpty()) {
            if(ticket.getClientName().equals("Industria e Comercio de Cosmeticos Natura Ltda")){
                clientName = "NATURA COSMETICOS S.A";
            }else{
                clientName = ticket.getClientName();
            }

            query = "SELECT * FROM WCC_TO_GTAX_CLIENT V WHERE lower(V.projeto) like '%" + clientName.toLowerCase() + "%'";
        } else {
            query = "SELECT * FROM WCC_TO_GTAX_CLIENT V WHERE lower(V.usuario) like '%" + ticket.getContactName().toLowerCase() + "%'";
        }

        System.out.println(ticket.getContactName());
        System.out.println(query);

        try {
            queryExecutor = db.getDbCon().createStatement();

            queryResult = queryExecutor.executeQuery(query);

            if (queryResult.next()) {
                userName = queryResult.getString("usuario");
            }

            queryResult.close();
            queryExecutor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print(userName);

        return userName;
    }

    public static String validateUserID(DataSource db, WCCTicketModel ticket) {
        Statement queryExecutor;
        ResultSet queryResult;
        String query = null;
        String id = null;


        if (ticket.getContactName() != null || !ticket.getContactName().isEmpty()) {
            query = "SELECT * FROM WCC_TO_GTAX_CLIENT V WHERE lower(V.usuario) like '%" + ticket.getContactName().toLowerCase() + "%' AND lower(V.projeto) like '%" +ticket.getClientName().toLowerCase() + "%'";
        }

        System.out.println(query);

        try {
            queryExecutor = db.getDbCon().createStatement();

            queryResult = queryExecutor.executeQuery(query);

            if (queryResult.next()) {
                id = queryResult.getString("ID");
            }

            queryResult.close();
            queryExecutor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(id);

        return id;
    }
}
