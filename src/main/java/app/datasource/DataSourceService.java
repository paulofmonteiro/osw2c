package app.datasource;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.sql.Connection;

public class DataSourceService extends Service<Connection> {
    @Override
    protected Task<Connection> createTask() {
        return new DataSourceTask();
    }
}
