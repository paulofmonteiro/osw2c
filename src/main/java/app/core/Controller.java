package app.core;

import app.gtax.GTAXLogin;
import app.gtax.GTAXLoginService;
import app.gtax.GTAXNewTicket;
import app.utils.ConfigHelper;
import app.wcc.WCCLogin;
import app.wcc.WCCLoginService;
import app.wcc.WCCTicketInfo;
import app.wcc.WCCTicketService;
import app.webdriver.WebDriverHelper;
import com.jfoenix.controls.*;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private AnchorPane loginPanel;

    @FXML
    private AnchorPane ticketPanel;

    @FXML
    private JFXTextField txtGTAXUsername;

    @FXML
    private JFXPasswordField txtGTAXPassword;

    @FXML
    private JFXButton btnGTAXLogin;

    @FXML
    private JFXToggleButton toggleTestEnvironment;

    @FXML
    private JFXTextArea tickerNumbers;

    @FXML
    private JFXProgressBar mainProgressBar;

    public void translateAnimation(double duration, Node node, double byX){
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), node);
        translateTransition.setByX(byX);
        translateTransition.play();
    }

    public void initialize(URL location, ResourceBundle resources) {
        WebDriverHelper.getWebDriver("default");
        //translateAnimation(0.5, ticketPanel, 600);
    }

    @FXML
    void verifyGTAXLoginInfo(ActionEvent event) {
        String userName;
        String password;

        userName = txtGTAXUsername.getText();
        password = txtGTAXPassword.getText();

        ConfigHelper.setConfig("gtax", "username", userName);
        ConfigHelper.setConfig("gtax", "password", password);

        mainProgressBar.visibleProperty().setValue(true);

        Service login = new GTAXLoginService(WebDriverHelper.getWebDriver("default"), toggleTestEnvironment.selectedProperty().getValue());
        login.start();

        login.setOnSucceeded(e -> {
            Object value = login.getValue();

            if((Boolean) value){
                if(ConfigHelper.store("gtax")){
                    System.out.println("logado com sucesso");

                    translateAnimation(0.5, ticketPanel, -600);
                    mainProgressBar.visibleProperty().setValue(false);
                }else {
                    System.out.println("erro ao salvar arquivo de configuração gtax");
                }
            }else{
                System.out.println("erro ao logar");
            }
        });

        login.setOnFailed(e -> {
            Object value = login.getValue();
            System.out.println("Login falhou | " + value);
        });
    }

    @FXML
    void getTickets(ActionEvent event) {
        if(!tickerNumbers.getText().isEmpty()){
            String tickets[] = tickerNumbers.getText().split("[^\n]*\n+");
            Boolean ticketsOk[];

            GTAXLogin gtaxLogin = new GTAXLogin(WebDriverHelper.getWebDriver("default"), true);

            if(gtaxLogin.login()){

                        new GTAXNewTicket(WebDriverHelper.getWebDriver("default"), tickets[0]);

                        //gtaxLogin.closeAllTicketFrames();

            }
/*
            mainProgressBar.visibleProperty().setValue(true);

            if(new WCCLogin(WebDriverHelper.getWebDriver("default")).login(ConfigHelper.getConfig("wcc", "url"))){
                ticketsOk = new Boolean[tickets.length];

                for(int i = 0; i < tickets.length; i++) {
                    ticketsOk[i] = new WCCTicketInfo(WebDriverHelper.getWebDriver("default"), tickets[i], ConfigHelper.getConfig("wcc", "ticketUrl")).getAllTicketPageInfo();
                }

                GTAXLogin gtaxLogin = new GTAXLogin(WebDriverHelper.getWebDriver("default"), toggleTestEnvironment.selectedProperty().getValue());

                if(gtaxLogin.login()){
                    for(int i = 0; i < ticketsOk.length; i++) {
                        if( ticketsOk[i]){
                            new GTAXNewTicket(WebDriverHelper.getWebDriver("default"), tickets[i]);

                            //gtaxLogin.closeAllTicketFrames();
                        }
                    }
                }


            }

*/
            mainProgressBar.visibleProperty().setValue(false);

        }
    }
}
