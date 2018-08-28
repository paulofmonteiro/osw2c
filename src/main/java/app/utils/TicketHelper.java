package app.utils;

import app.wcc.WCCTicketModel;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketHelper {

    private static HashMap<String, JsonObject> ticketFiles;

    public static Boolean loadConfigFile(String fileName){
        InputStream is;
        JsonReader jReader;

        if(ticketFiles == null){
            ticketFiles = new HashMap<>();
        }

        if(!ticketFiles.containsKey(fileName)){
            try{
                is = new FileInputStream("C:\\wcc2gtax\\tickets\\" + fileName + "\\" + fileName + ".json");
                jReader = Json.createReader(is);

                ticketFiles.put(fileName, jReader.readObject());

                jReader.close();
            }catch (Exception e){
                Logger.getLogger(ConfigHelper.class.getName()).log(Level.SEVERE, null, e);

                return false;
            }
        }

        return true;
    }

    public static JsonObject getConfigAsJson(String file, String key){
        JsonObject jObj;

        if(ticketFiles.containsKey(file)){
            jObj = ticketFiles.get(file).getJsonObject(key);
        }else{
            jObj = null;
        }

        return jObj;
    }

    public static String getConfig(String file, String key) {
        String value;

        if(ticketFiles.containsKey(file)) {
            value = ticketFiles.get(file).getString(key);
        } else {
            value = null;
        }

        return value;
    }

    public static Boolean setConfig(String file, String key, String value){
        JsonObject jObj;
        JsonObject configFile;

        if(ticketFiles.containsKey(file)) {
            jObj = ticketFiles.get(file);

            configFile = Json.createObjectBuilder(jObj)
                    .add(key, value)
                    .build();

            ticketFiles.put(file, configFile);

            return true;
        }else{
            return false;
        }
    }

    public static Boolean store(String file){
        FileWriter fileWriter;
        JsonObject configFile;

        if(ticketFiles.containsKey(file)){
            configFile = ticketFiles.get(file);

            try {
                BufferedWriter out = new BufferedWriter
                        (new OutputStreamWriter(new FileOutputStream(ticketFiles.get("general").getString("configFolder") + file + ".json"),  StandardCharsets.ISO_8859_1));

                out.write(configFile.toString());
                out.close();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
