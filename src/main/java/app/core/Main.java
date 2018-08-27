package app.core;

import app.datasource.DataSource;
import app.utils.ConfigHelper;
import app.webdriver.WebDriverHelper;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.openqa.selenium.WebDriver;

public class Main extends Application {

    public static DataSource db;

    @Override
    public void init(){

        if(ConfigHelper.loadConfigFile("general") && ConfigHelper.loadConfigFile("gtax")) {
            ConfigHelper.loadConfigFile("wcc");
            ConfigHelper.loadConfigFile("jira");
            ConfigHelper.loadConfigFile("safe");
        }

        db = new DataSource();
    }

    @Override
    public void start(Stage mainStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/views/main.fxml"));

        mainStage.setTitle("One Ticket Transfer");
        //mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.setAlwaysOnTop(true);
        mainStage.setScene(new Scene(root));
        mainStage.setOnCloseRequest((event) -> {
            WebDriverHelper.getWebDriver("default").close();
            WebDriverHelper.getWebDriver("default").quit();
        });


        mainStage.show();
    }

    public static void main(String[] args) {
        LauncherImpl.launchApplication(Main.class, AppPreloader.class, args);
    }
}
