package app.utils;

import javax.json.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigHelper {

    private static HashMap<String, JsonObject> configFiles;

    public static Boolean loadConfigFile(String fileName){
        InputStream is;
        JsonReader jReader;

        if(configFiles == null){
            configFiles = new HashMap<>();
        }

        if(!configFiles.containsKey(fileName)){
            try{
                is = ConfigHelper.class.getResourceAsStream("/config/" + fileName + ".json");
                jReader = Json.createReader(is);

                configFiles.put(fileName, jReader.readObject());

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

        if(configFiles.containsKey(file)){
            jObj = configFiles.get(file).getJsonObject(key);
        }else{
            jObj = null;
        }

       return jObj;
    }

    public static String getConfig(String file, String key) {
        String value;

        if(configFiles.containsKey(file)) {
            value = configFiles.get(file).getString(key);
        } else {
            value = null;
        }

        return value;
    }

    public static Boolean setConfig(String file, String key, String value){
        JsonObject jObj;
        JsonObject configFile;

        if(configFiles.containsKey(file)) {
            jObj = configFiles.get(file);

            configFile = Json.createObjectBuilder(jObj)
                .add(key, value)
                .build();
        }

        return true;
    }
}
