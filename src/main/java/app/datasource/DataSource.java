package app.datasource;

import app.utils.ConfigHelper;
import sun.security.krb5.Config;

import java.io.File;
import java.sql.*;

public class DataSource {

    private Connection dbCon;
    private Boolean locatedTNS;

    public DataSource(){
        dbCon = null;
        locatedTNS = locateOracleTNSFile();
    }

    private Boolean locateOracleTNSFile(){
        String tnsAdmin = System.getenv("TNS_ADMIN");

        if (tnsAdmin == null) {
            String oracleHome = System.getenv("ORACLE_HOME");
            if (oracleHome == null) {
                if(!ConfigHelper.getConfig("general", "dbTNSPath").isEmpty()){
                    tnsAdmin = ConfigHelper.getConfig("general", "dbTNSPath");
                }else{
                    return false;
                }
                //tnsAdmin = "C:\U6031188\product\11.2.0\client_1\network\admin";
            }else{
                tnsAdmin = oracleHome + File.separatorChar + "network" + File.separatorChar + "admin";
            }

        }

        ConfigHelper.setConfig("general", "dbTNSPath", tnsAdmin);
        ConfigHelper.setConfig("general", "dbLocatedTNS", "true");

        System.out.print(tnsAdmin);
        System.setProperty("oracle.net.tns_admin", tnsAdmin);

        return true;
    }

    public Connection conn(){
        String dbURL = "jdbc:oracle:thin:@" + ConfigHelper.getConfig("general", "dbTNS");
        String dbUsername = ConfigHelper.getConfig("general", "dbUsername");
        String dbPass = ConfigHelper.getConfig("general", "dbPass");

        locateOracleTNSFile();

        try {
            Class.forName ("oracle.jdbc.OracleDriver");

            dbCon =  DriverManager.getConnection(dbURL, dbUsername, dbPass);

            System.out.println("Database connection established");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error to establishe database connection");

            e.printStackTrace();
        }

        return dbCon;
    }

    public Boolean getLocatedTNS(){
        return locatedTNS;
    }

    public Connection getDbCon(){
        if(dbCon == null){
            conn();
        }
        return dbCon;
    }

}
