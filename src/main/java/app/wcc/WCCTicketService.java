package app.wcc;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.openqa.selenium.WebDriver;

public class WCCTicketService extends Service<Boolean> {

    private WebDriver WD;
    private String ticketNumber;
    private String url;
    private WCCTicketInfo wccTicketInfo;

    public WCCTicketService(WebDriver webdriver, String ticketNumber, String url){

        WD = webdriver;
        this.ticketNumber = ticketNumber;
        this.url = url;
        wccTicketInfo = new WCCTicketInfo(WD,ticketNumber, url);

    }

    public void setTicketNumber(String ticketNumber){
        wccTicketInfo.setTicketNumber(ticketNumber);
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return wccTicketInfo.getAllTicketPageInfo();
            }
        };
    }
}
