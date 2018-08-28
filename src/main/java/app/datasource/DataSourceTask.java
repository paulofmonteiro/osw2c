package app.datasource;

import app.utils.ConfigHelper;
import javafx.concurrent.Task;

import java.sql.Connection;

public class DataSourceTask extends Task<Connection> {
    @Override
    protected Connection call() throws Exception {
        DataSource db =  new DataSource();

        if(Boolean.valueOf(ConfigHelper.getConfig("general", "dbLocatedTNS"))){
            return db.getDbCon();
        }else{
            return null;
        }
    }
}
