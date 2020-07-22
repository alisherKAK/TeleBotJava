package config;

import java.io.FileReader;
import java.util.Properties;

public class Configurator {
    private static String _path;
    public Configurator(String path){
        _path = path;
    }

    public static Properties getConfig(){
        Properties prop = new Properties();
        try {
            FileReader reader = new FileReader(_path);
            prop.load(reader);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        return prop;
    }
}
