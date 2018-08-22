package app.core;

import app.webdriver.WebDriverHelper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private JFXTextField txtGTAXUsername;

    @FXML
    private JFXPasswordField txtGTAXPassword;

    @FXML
    private JFXButton btnGTAXLogin;

    public void initialize(URL location, ResourceBundle resources) {
       this.txtGTAXUsername.setText("Paulo");
    }

    @FXML
    void verifyGTAXLoginInfo(ActionEvent event) {
        WebDriverHelper.getWebDriver("default");
    }
}
