package app.gtax;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.openqa.selenium.WebDriver;

public class GTAXLoginService extends Service<Boolean> {

    private GTAXLogin gtaxLogin;

    public GTAXLoginService(WebDriver webdriver, Boolean testEnvironment) {
        gtaxLogin = new GTAXLogin(webdriver, testEnvironment);
    }

    @Override
    protected Task<Boolean> createTask() {

        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return gtaxLogin.login();
            }
        };
    }
}
