package com.hospitalcrud.config;

import com.hospitalcrud.utils.Constantes;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Log4j2
@Getter
@Component
public class Configuration {
    private static Configuration instance=null;
    private Properties p;

    private Configuration() {
        try {
            p = new Properties();
            p.loadFromXML(Configuration.class.getClassLoader().getResourceAsStream(Constantes.CONFIG_XML));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Configuration getInstance() {
        if (instance==null) {
            instance=new Configuration();
        }
        return instance;
    }

    public String getProperty(String key) {
        return p.getProperty(key);
    }

    public void setProperty(String key, String value) {
        p.setProperty(key, value);
    }
}
