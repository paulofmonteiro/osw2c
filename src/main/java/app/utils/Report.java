package app.utils;

import app.wcc.WCCTicketModel;
import javafx.collections.ObservableList;

import javax.json.JsonObject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Report {

    public static void FinalReport(ObservableList<WCCTicketModel> tickets){
        FileWriter fileWriter;
        String text;

        Date date= new Date();

        long time = date.getTime();

        String fileName = "FinalReport_" + time + ".txt";

        try {
            BufferedWriter out = new BufferedWriter
                    (new OutputStreamWriter(new FileOutputStream(ConfigHelper.getConfig("general", "logFolder") + fileName),  StandardCharsets.ISO_8859_1));

            out.write("WCC, GTAX, JIRA" + System.getProperty("line.separator"));

            for (WCCTicketModel ticket : tickets) {
                text = ConfigHelper.getConfig(ticket.getWCCTicketNumber(), "ticketNumber") + "," + ConfigHelper.getConfig(ticket.getWCCTicketNumber(), "gtaxNumber") + ", " + ConfigHelper.getConfig(ticket.getWCCTicketNumber(), "jiraReference")  + System.getProperty("line.separator");
                out.append(text);
            }

            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
