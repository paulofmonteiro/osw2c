package app.wcc;

import app.webdriver.WebDriverHelper;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.openqa.selenium.WebDriver;

public class WCCLoginService extends Service<Boolean> {

    private WebDriver WD;
    private WCCLogin wccLogin;
    private String url;

    public WCCLoginService(WebDriver webdriver, String url) {
        WD = webdriver;
        wccLogin = new WCCLogin(WD);
        this.url = url;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return wccLogin.login(url);
            }
        };
    }
}
