package app.core;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AppPreloader extends Preloader {

    private Stage preStage;
    private Scene preScene;

    @Override
    public void init() throws Exception{

        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/views/splash.fxml"));

                    preScene = new Scene(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void start(Stage primaryStage) throws Exception {
        this.preStage = primaryStage;

        this.preStage.initStyle(StageStyle.UNDECORATED);
        // Set preloader scene and show stage.
        this.preStage.setScene(this.preScene);
        this.preStage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        // Handle state change notifications.
        StateChangeNotification.Type type = info.getType();
        switch (type) {
            case BEFORE_LOAD:

                break;
            case BEFORE_INIT:

                break;
            case BEFORE_START:

                this.preStage.hide();
                break;
        }
    }
}
