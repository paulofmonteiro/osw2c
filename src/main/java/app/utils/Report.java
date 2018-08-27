package app.utils;

import app.wcc.WCCTicketModel;
import javafx.collections.ObservableList;

import javax.json.JsonObject;
import java.io.FileWriter;
import java.io.IOException;

public class Report {

    public static void FinalReport(ObservableList<WCCTicketModel> tickets){
        FileWriter fileWriter;
        String text;
        String fileName = "FinalReport.txt";

        try {
            fileWriter = new FileWriter(ConfigHelper.getConfig("general", "logFolder") + fileName);

            fileWriter.write("");

            for (WCCTicketModel ticket : tickets) {
                text = "WCC: " + ticket.getWCCTicketNumber() + " | GTAX: " + ticket.getGTAXTicketNumber() + " | JIRA: " + ticket.getJiraReference() + System.getProperty("line.separator");
                fileWriter.append(text);
            }

            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
