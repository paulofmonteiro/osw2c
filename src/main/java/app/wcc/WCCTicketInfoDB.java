package app.wcc;

import app.datasource.DataSource;
import app.utils.TicketHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class WCCTicketInfoDB {

    private DataSource db;

    public WCCTicketInfoDB(DataSource db){
        this.db = db;
    }

    public ObservableList<WCCTicketModel> getTicket(String[] ticketsNumber){
        ObservableList<WCCTicketModel> tickets = FXCollections.observableArrayList();

        Statement queryExecutor;
        ResultSet queryResult;

        String query = "SELECT * FROM VIEW_WCC_TO_GTAX V WHERE V.COD_CHAMADO IN(";
        String ticketIN = "";

        for(int i = 0; i < ticketsNumber.length; i++){
            ticketIN = ticketIN + "'";
            ticketIN = ticketIN + ticketsNumber[i];

            if(i == ticketsNumber.length - 1){
                ticketIN = ticketIN + "')";
            }else{
                ticketIN = ticketIN + "', ";
            }
        }

        query = query + ticketIN;
        System.out.println(query);

        try {
            queryExecutor = this.db.getDbCon().createStatement();

            queryResult = queryExecutor.executeQuery(query);

            while(queryResult.next()){
                WCCTicketModel ticket = new WCCTicketModel();

                ticket.setWCCTicketNumber(queryResult.getString("COD_CHAMADO"));
                ticket.setClientName(queryResult.getString("NOME_CLIENTE"));
                ticket.setSubject(queryResult.getString("SUBJECT"));
                ticket.setDescription(queryResult.getString("DESCRIPTION"));
                ticket.setContactName(queryResult.getString("CONTACT_NAME"));
                ticket.setProject(queryResult.getString("PRODUCT_FEATURE_MODULE"));
                ticket.setInquiry(WCCTicketHelper.toForTicketType(queryResult.getString("INQUIRY")));
                ticket.setCallType(WCCTicketHelper.toForCallType(queryResult.getString("INQUIRY")));
                ticket.setTicketLvl(queryResult.getString("PRIORIDADE"));
                ticket.setTicketStatus(WCCTicketHelper.toForStatus(queryResult.getString("STATUS")));
                ticket.setStage(WCCTicketHelper.toForStage(queryResult.getString("STATUS"), WCCTicketHelper.toForStatus(queryResult.getString("STATUS"))));
                ticket.setFat(queryResult.getString("MINUTOS_FAT"));
                ticket.setJiraReference(queryResult.getString("JIRA_REFERENCE"));
                ticket.setCountry(queryResult.getString("PAIS"));
                ticket.setSupportAnalyst(queryResult.getString("ANALISTA_TR_RESPONSAVEL"));

                ticket.setContactName(WCCTicketHelper.validateUser(this.db, ticket));

                tickets.add(ticket);

            }

            queryResult.close();
            queryExecutor.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(ticketIN);

        return  tickets;
    }
}
