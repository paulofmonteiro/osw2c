package app.core;

import app.gtax.GTAXLogin;
import app.gtax.GTAXLoginService;
import app.gtax.GTAXNewTicket;
import app.jira.Jira;
import app.utils.ConfigHelper;
import app.utils.Report;
import app.wcc.*;
import app.webdriver.WebDriverHelper;
import com.jfoenix.controls.*;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private ObservableList<WCCTicketModel> tickets;

    @FXML
    private AnchorPane loginPanel;

    @FXML
    private AnchorPane ticketPanel;

    @FXML
    private AnchorPane optionsPane;

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

    @FXML
    private JFXComboBox<Label> comboBoxStatus;

    @FXML
    private JFXComboBox<Label> comboBoxStage;

    @FXML
    private JFXComboBox<Label> comboBoxCountry;

    @FXML
    private JFXTextField txtTNSPath;

    public void translateAnimationX(double duration, Node node, double byX){
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), node);
        translateTransition.setByX(byX);
        translateTransition.play();
    }

    public void translateAnimationY(double duration, Node node, double byY){
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), node);
        translateTransition.setByY(byY);
        translateTransition.play();
    }

    public void buildStatusComboBox(){
        comboBoxStatus.getItems().add(new Label("Open"));
        comboBoxStatus.getItems().add(new Label("ReOpen"));
        comboBoxStatus.getItems().add(new Label("Closed"));
        comboBoxStatus.getItems().add(new Label("New"));
        comboBoxStatus.getItems().add(new Label("Closed - Duplicate"));

        comboBoxStatus.setPromptText("Status");
    }

    public void buildStageComboBox(){
        comboBoxStage.getItems().add(new Label("--None--"));
        comboBoxStage.getItems().add(new Label("In Progress"));
        comboBoxStage.getItems().add(new Label("Callback"));
        comboBoxStage.getItems().add(new Label("Customer Needs to Upgrade"));
        comboBoxStage.getItems().add(new Label("Email/VMResolution"));
        comboBoxStage.getItems().add(new Label("Referral Made"));
        comboBoxStage.getItems().add(new Label("Fix Sent - Pending Close"));
        comboBoxStage.getItems().add(new Label("TFS"));
        comboBoxStage.getItems().add(new Label("Waiting for Approval"));
        comboBoxStage.getItems().add(new Label("Waiting for Customer Reply"));
        comboBoxStage.getItems().add(new Label("Waiting on Process Completion"));
        comboBoxStage.getItems().add(new Label("Waiting - Outbound Chat"));

        comboBoxStage.setPromptText("Stage");
    }

    public void buildComboBoxCountry() {
        comboBoxCountry.getItems().add(new Label("US"));
        comboBoxCountry.getItems().add(new Label("Canada"));
        comboBoxCountry.getItems().add(new Label("North America"));
        comboBoxCountry.getItems().add(new Label("Brazil"));
        comboBoxCountry.getItems().add(new Label("UK"));
        comboBoxCountry.getItems().add(new Label("Europe"));
        comboBoxCountry.getItems().add(new Label("Turkey"));
        comboBoxCountry.getItems().add(new Label("MENA"));
        comboBoxCountry.getItems().add(new Label("African countries"));
        comboBoxCountry.getItems().add(new Label("Africa"));
        comboBoxCountry.getItems().add(new Label("India"));
        comboBoxCountry.getItems().add(new Label("China"));
        comboBoxCountry.getItems().add(new Label("Other Asian countries"));
        comboBoxCountry.getItems().add(new Label("Asia"));
        comboBoxCountry.getItems().add(new Label("ANZ"));
        comboBoxCountry.getItems().add(new Label("Japan"));
        comboBoxCountry.getItems().add(new Label("North LATAM"));
        comboBoxCountry.getItems().add(new Label("South LATAM"));
        comboBoxCountry.getItems().add(new Label("Belgium"));
        comboBoxCountry.getItems().add(new Label("France"));
        comboBoxCountry.getItems().add(new Label("Switzerland"));
        comboBoxCountry.getItems().add(new Label("Singapore"));
        comboBoxCountry.getItems().add(new Label("Ireland"));
        comboBoxCountry.getItems().add(new Label("Netherlands"));
        comboBoxCountry.getItems().add(new Label("Malaysia"));
        comboBoxCountry.getItems().add(new Label("Hong Kong"));
        comboBoxCountry.getItems().add(new Label("Korea"));

        comboBoxCountry.setPromptText("Country");
    }

    public void initTxtTNSPath(){
        if(Boolean.valueOf(ConfigHelper.getConfig("general", "dbLocatedTNS"))){
            txtTNSPath.setText(ConfigHelper.getConfig("general", "dbTNSPath"));
            txtTNSPath.setDisable(true);
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        WebDriverHelper.getWebDriver("default");

        buildStatusComboBox();
        buildStageComboBox();
        buildComboBoxCountry();
        initTxtTNSPath();

        translateAnimationX(0.5, ticketPanel, 600);
        translateAnimationY(0.5, optionsPane, -600);
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

                    translateAnimationX(0.5, ticketPanel, -600);
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
    void showOptionsPane(ActionEvent event) {
        translateAnimationY(0.5, optionsPane, 600);
    }

    @FXML
    void getOptions(ActionEvent event) {
        translateAnimationY(0.5, optionsPane, -600);

        if(comboBoxStatus.getSelectionModel().getSelectedItem() != null){
            ConfigHelper.setConfig("general", "status", comboBoxStatus.getSelectionModel().getSelectedItem().getText());
        }

        if(comboBoxStage.getSelectionModel().getSelectedItem() != null){
            ConfigHelper.setConfig("general", "stage", comboBoxStage.getSelectionModel().getSelectedItem().getText());
        }

        if(comboBoxCountry.getSelectionModel().getSelectedItem() != null){
            ConfigHelper.setConfig("general", "country", comboBoxCountry.getSelectionModel().getSelectedItem().getText());
        }

        if(!txtTNSPath.getText().isEmpty()){
            ConfigHelper.setConfig("general", "dbTNSPath", txtTNSPath.getText());
            Main.db.getDbCon();
        }

    }

    @FXML
    void getTickets(ActionEvent event) {
        if(!tickerNumbers.getText().isEmpty()){

            //verifica se foi encontrado o TNS
            if(Boolean.valueOf(ConfigHelper.getConfig("general", "dbLocatedTNS"))){
                WCCTicketInfoDB wccTicketInfo = new WCCTicketInfoDB(Main.db);

                String ticketsNumber[] = tickerNumbers.getText().split("\n");
                Boolean ticketsOk[];
                String newTickets[];

                tickets = wccTicketInfo.getTicket(ticketsNumber);

                for (WCCTicketModel ticket : tickets) {
                    WCCTicketHelper.store(ticket);
                }

                GTAXLogin gtaxLogin = new GTAXLogin(WebDriverHelper.getWebDriver("default"), toggleTestEnvironment.selectedProperty().getValue());

                if(gtaxLogin.login()){
                    for (WCCTicketModel ticket : tickets) {
                        String result = new GTAXNewTicket(WebDriverHelper.getWebDriver("default"), ticket.getWCCTicketNumber(), toggleTestEnvironment.selectedProperty().getValue()).getNewTicketNumber();
                        ticket.setGTAXTicketNumber(result);
                        WCCTicketHelper.store(ticket);
                        gtaxLogin.closeAllTicketFrames();
                    }
                }

                Jira jira = new Jira(WebDriverHelper.getWebDriver("default"));

                if(jira.login()){
                    for (WCCTicketModel ticket : tickets) {
                        jira.insertGTAXnumber(ticket.getJiraReference(), ticket.getGTAXTicketNumber());
                    }
                }

                Report.FinalReport(tickets);

                mainProgressBar.visibleProperty().setValue(true);

            }else{
                translateAnimationY(0.5, optionsPane, 600);
            }
        }
    }

}
